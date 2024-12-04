package _gestioneeventi;

import java.util.*;
import java.util.concurrent.*;
import java.util.logging.Logger;

import _gestioneeventi.EsecuzioneEnvironment.Stato;
import _log.Log;

/*
 * Serve ad aggiungere i singoli listener, ed ad attivarli e disattivali (tutti insieme)
 * Nota: implementa il seguente diagramma degli stati e delle transizioni :
 * 
 *    addListener
 *        __
 *       |  |   --attivaListener-> 
 *  --> Attesa                      Esecuzione 
 *            <--disattivaListener- 
 */

public final class EsecuzioneEnvironment { // NB con final non si possono
											// definire sottoclassi
	
	static Logger log = Log.creaLogger(EsecuzioneEnvironment.class.toString());
	private EsecuzioneEnvironment() {
	}

	public static enum Stato {
		Attesa, Esecuzione
	};

	private static Stato statocorrente = Stato.Attesa;

	private static ConcurrentHashMap<Listener, Thread> listenerAttivi = null;
	
	public static synchronized void addListener(Listener lr) {
		log.info("inserimento listener: " + lr);
		if (statocorrente == Stato.Attesa) {
			Environment.addListener(lr, new EsecuzioneEnvironment());
		}
		else {
			Environment.addListener(lr, new EsecuzioneEnvironment());
			attivaSingolo(lr);
		}
	}

	public static void attivaSingolo(Listener listener) {
		Thread t;
		log.info("attivazione listener " + listener);
		t = new Thread(new EsecuzioneListener(listener));
		listenerAttivi.put(listener, t);
		listenerAttivi.get(listener).start();
		listenerAttivi.get(listener).setName("Thread di " + listener.toString());
		log.info(listener + ": " + t);
	}

	public static synchronized void attivaListener() {
		if (statocorrente == Stato.Attesa) {
			statocorrente = Stato.Esecuzione;
			log.info("Ora attiviamo i listener");
			listenerAttivi = new ConcurrentHashMap<Listener, Thread>();
			Iterator<Listener> it = Environment.getInsiemeListener().iterator();
			while (it.hasNext()) {
				Listener listener = it.next();
				listenerAttivi.put(listener, new Thread(new EsecuzioneListener(
						listener)));
			}
			Iterator<Listener> i = listenerAttivi.keySet().iterator();
			while (i.hasNext()) {
				Listener l = i.next();
				listenerAttivi.get(l).start();
			}
		}
	}
	
	//TODO: Verificare disattiva listener in corsa
	public static synchronized void disattivaListener(Listener l) {
		Environment.aggiungiEvento(new Stop(null, l));
		try {
			Thread thread = listenerAttivi.get(l);
			thread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public static synchronized void disattivaListener() {
		if (statocorrente == Stato.Esecuzione) {
			statocorrente = Stato.Attesa;
			log.info("Ora fermiano i listener");
			Environment.aggiungiEvento(new Stop(null, null));
			// NB: a questo punto i listener non sono ancora fermi
			// ma l'evento Stop e' stato inserito nella coda di ciascuno di loro
			// e questo evento quando processato li disattivera'
			Iterator<Listener> it = listenerAttivi.keySet().iterator();
			while (it.hasNext()) {
				Listener listener = it.next();
				try {
					Thread thread = listenerAttivi.get(listener);
					thread.join();
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
			}
		}
	}

	public static synchronized Stato getStato() {
		return statocorrente;
	}
}
