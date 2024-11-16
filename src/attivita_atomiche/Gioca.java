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

	public synchronized void esegui(Executor e) {

		if (e == null || eseguita == true)
			return;
		eseguita = true;

		Iterator<TipoLinkComprendere> it = partita.getLinkComprendere()
				.iterator();
		while (it.hasNext()) {
			Giocatore g = it.next().getGiocatore();
			EsecuzioneEnvironment.addListener(g);
			Environment.aggiungiEvento(new Inizio(null, g, this.partita));
		}
		EsecuzioneEnvironment.addListener(partita);

		Random generator = new Random((long) 1);

		Environment.aggiungiEvento(new LancioDado(null, partita
				.getLinkComprendere().get(0).getGiocatore(), generator
				.nextInt(6) + 1));
		EsecuzioneEnvironment.attivaListener();
	}

	public synchronized boolean estEseguita() {
		return eseguita;
	}
}