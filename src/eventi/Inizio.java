package eventi;

import _gestioneeventi.*;
import partita.Partita;

public class Inizio extends Evento {
	private Partita p;
	
	public Inizio(Listener m, Listener d, Partita p) {
		super(m, d);
		this.p = p;
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
	
	public Partita getPartita() {
		return this.p;
	}
}
