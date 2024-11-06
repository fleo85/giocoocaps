package _gestioneeventi;

class EsecuzioneListener implements Runnable { // NB: non e' pubblica, serve
												// solo nel package
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
				if (e.getClass() == Stop.class)
					return;
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