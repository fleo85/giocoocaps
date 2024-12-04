package applicazione;

import java.io.*;
import java.net.*;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.NoSuchElementException;
import java.util.logging.Logger;

import _framework.TaskExecutor;
import _gestioneeventi.Environment;
import _gestioneeventi.EsecuzioneEnvironment;
import _log.Log;
import attivita_atomiche.CreaPartita;
import attivita_composte.AttivitaPrincipale;
import casella.Casella;
import casellaSalto.CasellaSalto;
import eventi.FinePartita;
import eventi.NuovaPosizione;
import giocatore.Giocatore;
import partita.Partita;

public class ClientThread implements Runnable {
	static Logger log = Log.creaLogger(EsecuzioneEnvironment.class.toString());
	private Socket sock;
	private boolean fired = false;
	private SenderThread st = null;
	private boolean running = false;
	Server parent;
	
	LinkedList<Casella> tabellone = new LinkedList<Casella>();

	public ClientThread(Socket s, Server parent) {
		sock = s;
		this.parent = parent;
	}
	
	public void setInitialState() {
		// Creazione del tabellone di gioco

		Casella c1 = new Casella("1");
		Casella c2 = new Casella("2");
		CasellaSalto c3 = new CasellaSalto("Salto", "3");
		Casella c4 = new Casella("4");
		Casella c5 = new Casella("5");
		Casella c6 = new Casella("6");
		Casella c7 = new Casella("7");
		Casella c8 = new Casella("8");
		CasellaSalto c9 = new CasellaSalto("Salto", "9");
		Casella c10 = new Casella("10");

		c9.inserisciLinkSaltare(c5);
		c3.inserisciLinkSaltare(c8);

		c1.inserisciLinkSuccessore(c2);
		c2.inserisciLinkSuccessore(c3);
		c3.inserisciLinkSuccessore(c4);
		c4.inserisciLinkSuccessore(c5);
		c5.inserisciLinkSuccessore(c6);
		c6.inserisciLinkSuccessore(c7);
		c7.inserisciLinkSuccessore(c8);
		c8.inserisciLinkSuccessore(c9);
		c9.inserisciLinkSuccessore(c10);

		tabellone.add(c1);
		tabellone.add(c2);
		tabellone.add(c3);
		tabellone.add(c4);
		tabellone.add(c5);
		tabellone.add(c6);
		tabellone.add(c7);
		tabellone.add(c8);
		tabellone.add(c9);
		tabellone.add(c10);
	}

	@Override
	public void run() {
		if (fired)
			return;
		this.setInitialState();
		fired = true;
		running = true;
		BufferedReader in = null;
		PrintWriter pw = null;
		try {
			in = new BufferedReader(new InputStreamReader(sock.getInputStream()));
			pw = new PrintWriter(sock.getOutputStream());
		} catch (IOException e) {
			parent.removeFromCTList(this);
			e.printStackTrace();
		}
		HashSet<Giocatore> giocatoriConnessione = new HashSet<Giocatore>();
		Thread attivitaPrincipale = null;
		Partita partita = null;
		try {
			while (running) {
				String cmd = in.readLine();
				log.info("Ricevuto: " + cmd);
				if (cmd.startsWith("START")) {
					HashSet<Giocatore> giocatori = new HashSet<Giocatore>();
					String users = cmd.substring(cmd.indexOf(" ")).trim();
					if (users.equals("")) {
						pw.println("END");
						pw.flush();
						running = false;
						continue;
					} else {
						String[] userSplit = users.split(",");
						for (String s: userSplit) {
							Giocatore g = new Giocatore(s.trim());
							giocatori.add(g);
							giocatoriConnessione.add(g);
							EsecuzioneEnvironment.addListener(g);
						}
					}
					CreaPartita cp = new CreaPartita(tabellone, giocatori);
					TaskExecutor.getInstance().perform(cp);
					partita = cp.getResult();
					EsecuzioneEnvironment.addListener(partita);
					st = new SenderThread(pw, tabellone);
					Thread t = new Thread(st);
					t.start();
					try {
						t.join();
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					for (Giocatore g: giocatori) {
						Environment.addRemoteEventLogger(null, g, NuovaPosizione.class, pw);
					}
					Environment.addRemoteEventLogger(partita, null, FinePartita.class, pw);
					attivitaPrincipale = new Thread(new AttivitaPrincipale(tabellone, partita));
					attivitaPrincipale.start();
				} else if (cmd.equals("INTERRUPT")) {
					Environment.aggiungiEvento(new FinePartita(null, partita, null));
					if (attivitaPrincipale != null) {
						try {
							attivitaPrincipale.join();
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
					for (Giocatore g: giocatoriConnessione) {
						Environment.removeRemoteEventLogger(null, g, NuovaPosizione.class, pw);
						EsecuzioneEnvironment.disattivaListener(g);
					}
					Environment.removeRemoteEventLogger(partita, null, FinePartita.class, pw);
					EsecuzioneEnvironment.disattivaListener(partita);
					pw.println("END");
					pw.flush();
				} else {
					parent.removeFromCTList(this);
					running = false;
				}
			}
		} catch (IOException e1) {
			e1.printStackTrace();
			parent.removeFromCTList(this);
		} catch (NoSuchElementException e2) {
			e2.printStackTrace();
			parent.removeFromCTList(this);
		} finally {
			try {
				sock.close();
				pw.close();
				in.close();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
			for (Giocatore g: giocatoriConnessione) {
				Environment.removeRemoteEventLogger(null, g, NuovaPosizione.class, pw);
				EsecuzioneEnvironment.disattivaListener(g);
			}
			Environment.removeRemoteEventLogger(null, partita, FinePartita.class, pw);
			log.info("RIMUOVO TUTTI I GIOCATORI");
		}
	}

	public void stop() throws IOException {
		if (sock != null)
			sock.close();
		running = false;
	}

	public boolean isRunning() {
		return running;
	}
}