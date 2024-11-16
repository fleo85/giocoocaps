package attivita_composte;

import giocatore.Giocatore;

import java.util.*;

import casella.*;
import partita.*;
import attivita_atomiche.*;
import attivita_io.*;
import _framework.*;

public class AttivitaPrincipale implements Runnable {

	private boolean eseguita = false;
	private Partita partita;
	private List<Casella> tabellone;

	public AttivitaPrincipale(List<Casella> tabellone, Partita p) {
		this.tabellone = tabellone;
		this.partita = p;
	}

	public synchronized void run() {
		if (eseguita == true)
			return;
		eseguita = true;
		Boolean valido = false;

		Verifica vs = new Verifica(this.tabellone);
		Executor.perform(vs);
		valido = vs.getResult();

		if (!valido)
			return;
		else {
			StampaPartita.perform(tabellone, partita);

			AttivitaSottoramo1 a1 = new AttivitaSottoramo1(partita);
			Thread ramo1 = new Thread(a1);
			ramo1.start();

			AttivitaSottoramo2 a2 = new AttivitaSottoramo2(partita);
			Thread ramo2 = new Thread(a2);
			ramo2.start();

			try {
				ramo1.join();
				ramo2.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			System.out.println("PARTITA FINITA IN QUALCHE MANIERA");
		}

	}

	public synchronized boolean estEseguita() {
		return eseguita;
	}
}
