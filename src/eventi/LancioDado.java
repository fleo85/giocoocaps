package eventi;

import _gestioneeventi.*;

public class LancioDado extends Evento {

	public LancioDado(Listener m, Listener d) {
		super(m, d);
	}

	public boolean equals(Object o) {
		if (super.equals(o)) {
			LancioDado p = (LancioDado) o;
			return true;
		}
		return false;
	}

	public int hashCode() {
		return super.hashCode() + getClass().hashCode();
	}

	public String toString() {
		return "Lancio del Dado(" + getMittente() + " -> " + getDestinatario() + " )";
	}
}
