package partita;

import java.util.Iterator;

import _framework.Task;
import _gestioneeventi.Environment;
import _gestioneeventi.EsecuzioneEnvironment;
import _gestioneeventi.Evento;
import comprendere.TipoLinkComprendere;
import eventi.FinePartita;
import eventi.Inizio;
import eventi.LancioDado;
import giocatore.Giocatore;
import eventi.Fine;

class PartitaFired implements Task {
	private boolean eseguita = false;
	private Partita p;
	private Evento e;

	public PartitaFired(Partita p, Evento e) {
		this.p = p;
		this.e = e;
	}

	@Override
	public synchronized void esegui() {
		if (eseguita || (e.getDestinatario() != p && e.getDestinatario() != null))
			return;
		eseguita = true;
		switch (p.getStato()) {
		case STANDBY:
			if (e.getClass() == Inizio.class) {
				Iterator<TipoLinkComprendere> it = p.getLinkComprendere()
						.iterator();
				while (it.hasNext()) {
					Giocatore g = it.next().getGiocatore();
					Environment.aggiungiEvento(new Inizio(null, g));
				}
				Environment.aggiungiEvento(new LancioDado(null, p
						.getLinkComprendere().get(0).getGiocatore()));
				p.statocorrente = Partita.Stato.INIZIATA;
			}
			break;
		case INIZIATA:
			if (e.getClass() == FinePartita.class) {
				for (TipoLinkComprendere g: p.getLinkComprendere()) {
					Environment.aggiungiEvento(new Fine(p, g.getGiocatore()));
				}
				synchronized(p) {
					p.statocorrente = Partita.Stato.FINITA;
					p.notifyAll();
				}
			}
		case FINITA:
			break;
		default:
			throw new RuntimeException("Stato corrente non riconosciuto.");
		}
	}
}
