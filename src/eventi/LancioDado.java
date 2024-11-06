package eventi;

import _gestioneeventi.*;

public class LancioDado extends Evento {
	private int numerocaselle;

	public LancioDado(Listener m, Listener d, int valore) {
		super(m, d);
		numerocaselle = valore;
	}

	public boolean equals(Object o) {
		if (super.equals(o)) {
			LancioDado p = (LancioDado) o;
			return this.numerocaselle == p.numerocaselle;
		}
		return false;
	}

	public int hashCode() {
		return super.hashCode() + getClass().hashCode() + numerocaselle;
	}

	public String toString() {
		return "Lancio del Dado(" + getMittente() + " -> " + getDestinatario()
				+ ": " + numerocaselle + " )";
	}

	public int getLancio() {
		return numerocaselle;
	}
}
