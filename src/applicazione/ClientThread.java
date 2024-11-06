package applicazione;

import java.io.*;
import java.net.*;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.NoSuchElementException;
import java.util.Scanner;

import _gestioneeventi.Environment;
import _gestioneeventi.EsecuzioneEnvironment;
import _gestioneeventi.Stop;
import attivita_composte.AttivitaPrincipale;
import casella.Casella;
import casellaSalto.CasellaSalto;
import giocatore.Giocatore;

public class ClientThread implements Runnable {

	private Socket sock;
	private boolean fired = false;
	private SenderThread st = null;
	private boolean running = false;
	private int temperatura;
	Server parent;
	
	LinkedList<Casella> tabellone = new LinkedList<Casella>();

	public ClientThread(Socket s, Server parent) {
		sock = s;
		this.temperatura = 0;
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
		Scanner in = null;
		PrintWriter pw = null;
		try {
			in = new Scanner(sock.getInputStream());
			pw = new PrintWriter(sock.getOutputStream());
		} catch (IOException e) {
			parent.removeFromCTList(this);
			e.printStackTrace();
		}
		try {
			HashSet<Giocatore> giocatoriConnessione = new HashSet<Giocatore>();
			while (running) {
				String cmd = in.nextLine();
				System.out.println("Ricevuto: " + cmd);
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
						}
					}
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
						Environment.notifyStream.put(g, pw);
					}
					Thread attivitaPrincipale = new Thread(new AttivitaPrincipale(tabellone, giocatori));
					attivitaPrincipale.start();
				} else if (cmd.equals("INTERRUPT")) {
					pw.println("END");
					pw.flush();
					for (Giocatore g: giocatoriConnessione) {
						Environment.notifyStream.remove(g);
						EsecuzioneEnvironment.disattivaListener(g);
					}
				} else {
					parent.removeFromCTList(this);
					running = false;
				}
			}
			for (Giocatore g: giocatoriConnessione) {
				Environment.notifyStream.remove(g);
				EsecuzioneEnvironment.disattivaListener(g);
			}
			sock.close();
			pw.close();
			in.close();
			
		} catch (IOException e1) {
			e1.printStackTrace();
			parent.removeFromCTList(this);
		} catch (NoSuchElementException e2) {
			e2.printStackTrace();
			parent.removeFromCTList(this);
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