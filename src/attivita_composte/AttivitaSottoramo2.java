package attivita_composte;

//import attivita_io.AttesaComando;

public class AttivitaSottoramo2 implements Runnable {

	private boolean eseguita = false;

	public synchronized void run() {
		if (eseguita == true)
			return;
		eseguita = true;

		//AttesaComando.segnalefine();

	}

	public synchronized boolean estEseguita() {
		return eseguita;
	}
}
