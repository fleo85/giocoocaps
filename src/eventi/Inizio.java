package eventi;

import _gestioneeventi.*;
import partita.Partita;

public class Inizio extends Evento {
	public Inizio(Listener m, Listener d) {
		super(m, d);
	}

	public boolean equals(Object o) {
		return super.equals(o);
	}

	public int hashCode() {
		return super.hashCode() + getClass().hashCode();
	}

	public String toString() {
		return "Inizio(" + getMittente() + " -> " + getDestinatario() + ")";
	}
}
