package partita;

import _framework.Executor;
import _framework.Task;
import _gestioneeventi.Environment;
import _gestioneeventi.Evento;
import comprendere.TipoLinkComprendere;
import eventi.FinePartita;
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
	public synchronized void esegui(Executor exec) {
		if (eseguita || exec == null
				|| (e.getDestinatario() != p && e.getDestinatario() != null))
			return;
		eseguita = true;
		switch (p.getStato()) {
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
