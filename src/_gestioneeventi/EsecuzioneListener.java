package _gestioneeventi;

import java.util.logging.Logger;

class EsecuzioneListener implements Runnable {
	Logger log = Logger.getLogger(EsecuzioneListener.class.toString());
	private boolean eseguita = false;
	private Listener listener;

	public EsecuzioneListener(Listener l) {
		listener = l;
	}

	public synchronized void run() {
		if (eseguita)
			return;
		eseguita = true;
		while (true) {
			try {
				Evento e = Environment.prossimoEvento(listener);
				if (e.getClass() == Stop.class) {
					log.info("terminazione " + Thread.currentThread().getName());
					return;
				}
				log.info("invio evento " + e);
				listener.fired(e);
			} catch (InterruptedException ex) {
				ex.printStackTrace();
			}
		}
	}

	public synchronized boolean estEseguita() {
		return eseguita;
	}
}
