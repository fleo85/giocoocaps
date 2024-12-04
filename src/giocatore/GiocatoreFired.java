package giocatore;

import java.util.*;
import java.util.logging.Logger;

import comprendere.*;
import partita.*;
import casella.*;
import casellaSalto.*;
import _framework.*;
import _gestioneeventi.*;
import _log.Log;
import attivita_composte.AttivitaMovimento;
import eventi.*;
import giocatore.Giocatore.Stato;

class GiocatoreFired implements Task {
	static Logger log = Log.creaLogger(EsecuzioneEnvironment.class.toString());
	private boolean eseguita = false;
	private Giocatore g;
	private Evento e;

	public GiocatoreFired(Giocatore g, Evento e) {
		this.g = g;
		this.e = e;
	}

	// usati per simulare il lancio del dado
	private Random generator = new Random();

	private int randomNumber() {
		return this.generator.nextInt(6) + 1;
	}

	public synchronized void esegui() {
		if (eseguita || (e.getDestinatario() != g && e.getDestinatario() != null))
			return;
		eseguita = true;
		switch (g.getStato()) {
		case ALLENAMENTO:
			if (e.getClass() == Inizio.class) {
				Inizio i = (Inizio) e;
				if (g.getLinkComprendere() != null) {
					g.statocorrente = Stato.INPARTITA;
				}
			}
			break;
		case INPARTITA:
			if (e.getClass() == LancioDado.class) {
				int lancio = this.randomNumber();

				log.info(g.getNome() + " ha lanciato " + lancio
						+ " e si sta muovendo ...");

				g.attivitaMovimento = new Thread(new AttivitaMovimento(g, lancio));
				g.attivitaMovimento.start();
			} else if (e.getClass() == Fine.class) {
				if (g.attivitaMovimento != null && g.attivitaMovimento.isAlive()) {
					g.attivitaMovimento.interrupt();
				}
				g.statocorrente = Stato.ALLENAMENTO;
			}
			break;
		default:
			throw new RuntimeException("Stato corrente non riconosciuto.");
		}
	}

	public synchronized boolean estEseguita() {
		return eseguita;
	}
}
