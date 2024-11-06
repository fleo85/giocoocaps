package _gestioneeventi;

import java.io.PrintWriter;
import java.util.*;
import java.util.concurrent.*;

import eventi.Fine;
import eventi.NuovaPosizione;
import giocatore.Giocatore;
import partita.Partita;

public final class Environment { // NB con final non si possono definire
	// sottoclassi
	private Environment() { // NB non si possono costruire oggetti Environment
	}

	private static ConcurrentHashMap<Listener, LinkedBlockingQueue<Evento>> codeEventiDeiListener = new ConcurrentHashMap<Listener, LinkedBlockingQueue<Evento>>();
	public static HashMap<Giocatore, PrintWriter> notifyStream = new HashMap<Giocatore, PrintWriter>();

	public static void addListener(Listener lr, EsecuzioneEnvironment e) {
		if (e == null)
			return;
		codeEventiDeiListener.put(lr, new LinkedBlockingQueue<Evento>());
		// Nota Listener inserito ma non attivo
	}

	public static Set<Listener> getInsiemeListener() {
		return codeEventiDeiListener.keySet();
	}

	public static void aggiungiEvento(Evento e) {
		// unico meccanismo per aggiungere eventi
		if (e == null)
			return;
		Listener destinatario = e.getDestinatario();
		
		// MODIFICA AD ENVIRONMENT PER SUPPORTARE TRASMISSIONE EVENTI
		if (e.getMittente() != null && e.getMittente().getClass().equals(Giocatore.class)) {
			Giocatore g = (Giocatore) e.getMittente();
			if (notifyStream.containsKey(g)) {
				PrintWriter pw = notifyStream.get(g);
				if (e.getClass().equals(NuovaPosizione.class)) {
					NuovaPosizione eCast = (NuovaPosizione) e;
					pw.println(g.getNome() + " IN " + eCast.getNuovaPosizione().getDisegno());
					pw.flush();
				} else if (e.getClass().equals(Fine.class)) {
					pw.println(g.getNome() + " FINE");
					pw.flush();
				}
			}
		}
		// FINE MODIFICA AD ENVIRONMENT CLASSICO
		
		if (destinatario != null
				&& codeEventiDeiListener.containsKey(destinatario)) {
			// evento per un destinatario attivo
			try {
				codeEventiDeiListener.get(destinatario).put(e);
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
		} else if (destinatario == null) { // evento in broadcasting
			Iterator<Listener> itn = codeEventiDeiListener.keySet().iterator();
			while (itn.hasNext()) {
				Listener lr = itn.next();
				try {
					codeEventiDeiListener.get(lr).put(e);
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
			}
		}
	}

	public static Evento prossimoEvento(Listener lr)
			throws InterruptedException {
		// nota NON deve essere synchronized!!!
		return codeEventiDeiListener.get(lr).take();
	}

}