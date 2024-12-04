package attivita_atomiche;

import java.util.*;

import comprendere.*;
import _framework.*;
import _gestioneeventi.Environment;
import _gestioneeventi.EsecuzioneEnvironment;
import eventi.Inizio;
import eventi.LancioDado;
import giocatore.*;
import partita.*;

public class Gioca implements Task {

	private boolean eseguita = false;
	private Partita partita;

	public Gioca(Partita p) {
		this.partita = p;
	}

	public synchronized void esegui() {

		if (eseguita == true)
			return;
		eseguita = true;
		
		Environment.aggiungiEvento(new Inizio(null, this.partita));
	}

	public synchronized boolean estEseguita() {
		return eseguita;
	}
}