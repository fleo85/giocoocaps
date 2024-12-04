package attivita_composte;

import _framework.TaskExecutor;
import partita.*;
import attivita_atomiche.*;

public class AttivitaSottoramo1 implements Runnable {

	private boolean eseguita = false;
	private Partita partita;

	public AttivitaSottoramo1(Partita partita) {
		this.partita = partita;
	}

	public synchronized void run() {
		if (eseguita == true)
			return;
		eseguita = true;

		Gioca g = new Gioca(partita);
		TaskExecutor.getInstance().perform(g);
	}

	public synchronized boolean estEseguita() {
		return eseguita;
	}
}
