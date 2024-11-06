package comprendere;

import giocatore.*;

public final class ManagerComprendere {
	private ManagerComprendere(TipoLinkComprendere x) {
		link = x;
	}

	private TipoLinkComprendere link;

	public TipoLinkComprendere getLink() {
		return link;
	}

	public static void inserisci(TipoLinkComprendere y) {
		if (y != null) {
			if (y.getGiocatore().getStato() != Giocatore.Stato.ALLENAMENTO)
				throw new RuntimeException(
						"Non si puo' modificare un giocatore mentre sta giocando");
			ManagerComprendere k = new ManagerComprendere(y);
			y.getGiocatore().inserisciPerManagerComprendere(k);
			y.getPartita().inserisciPerManagerComprendere(k);
		}
	}

	public static void elimina(TipoLinkComprendere y) {
		if (y != null) {
			if (y.getGiocatore().getStato() != Giocatore.Stato.ALLENAMENTO)
				throw new RuntimeException(
						"Non si puo' modificare un giocatore mentre sta giocando");
			ManagerComprendere k = new ManagerComprendere(y);
			y.getGiocatore().eliminaPerManagerComprendere(k);
			y.getPartita().eliminaPerManagerComprendere(k);
		}
	}
}
