package eventi;

import _gestioneeventi.*;
import giocatore.Giocatore;

public class Fine extends Evento {
	public Fine(Listener m, Listener d) {
		super(m, d);
	}

	public boolean equals(Object o) {
		return super.equals(o);
	}

	public int hashCode() {
		return super.hashCode() + getClass().hashCode();
	}

	public String toString() {
		return "Fine(" + getMittente() + " -> " + getDestinatario() + ")";
	}
}
