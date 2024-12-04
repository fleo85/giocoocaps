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

		Iterator<TipoLinkComprendere> it = partita.getLinkComprendere()
				.iterator();
		while (it.hasNext()) {
			Giocatore g = it.next().getGiocatore();
			EsecuzioneEnvironment.addListener(g);
			Environment.aggiungiEvento(new Inizio(null, g, this.partita));
		}
		EsecuzioneEnvironment.addListener(partita);

		Environment.aggiungiEvento(new LancioDado(null, partita
				.getLinkComprendere().get(0).getGiocatore()));
		EsecuzioneEnvironment.attivaListener();
	}

	public synchronized boolean estEseguita() {
		return eseguita;
	}
}