package comprendere;

import giocatore.*;
import partita.*;

public class TipoLinkComprendere {
	private final Giocatore ilGiocatore;
	private final Partita laPartita;

	public TipoLinkComprendere(Giocatore x, Partita y) {
		if (x == null || y == null) // CONTROLLO PRECONDIZIONI
			throw new RuntimeException(
					"Gli oggetti devono essere inizializzati");
		ilGiocatore = x;
		laPartita = y;
	}

	public boolean equals(Object o) {
		if (o != null && getClass().equals(o.getClass())) {
			TipoLinkComprendere b = (TipoLinkComprendere) o;
			return b.ilGiocatore == ilGiocatore && b.laPartita == laPartita;
		} else
			return false;
	}

	public int hashCode() {
		return laPartita.hashCode() + ilGiocatore.hashCode();
	}

	public Partita getPartita() {
		return laPartita;
	}

	public Giocatore getGiocatore() {
		return ilGiocatore;
	}
}
