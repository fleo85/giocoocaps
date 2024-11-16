package attivita_composte;

import attivita_io.AttesaComando;
import partita.Partita;

//import attivita_io.AttesaComando;

public class AttivitaSottoramo2 implements Runnable {
	private Partita p;

	private boolean eseguita = false;
	
	public AttivitaSottoramo2(Partita p) {
		this.p = p;
	}

	public synchronized void run() {
		if (eseguita == true)
			return;
		eseguita = true;

		AttesaComando.segnalefine(p);

	}

	public synchronized boolean estEseguita() {
		return eseguita;
	}
}
