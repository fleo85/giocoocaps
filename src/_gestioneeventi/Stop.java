package _gestioneeventi;

public class Stop extends Evento {

	public Stop(Listener m, Listener d) {
		super(m, d);
	}

	public boolean equals(Object o) {
		return super.equals(o);
	}

	public int hashCode() {
		return super.hashCode() + getClass().hashCode();
	}

	public String toString() {
		return "Stop(" + getMittente() + " -> " + getDestinatario() + ")";
	}
}
