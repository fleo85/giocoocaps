package eventi;

import _gestioneeventi.Evento;
import _gestioneeventi.Listener;
import _gestioneeventi.Environment;
import casella.Casella;
import giocatore.Giocatore;

public class NuovaPosizione extends Evento implements Environment.SerializableEvent {
	private Casella nuovaPosizione;
	public NuovaPosizione(Listener m, Listener d, Casella nuovaPosizione) {
		super(m, d);
		this.nuovaPosizione = nuovaPosizione;
	}
	public Casella getNuovaPosizione() {
		return this.nuovaPosizione;
	}
	public boolean equals(Object o) {
		if (super.equals(o)) {
			NuovaPosizione p = (NuovaPosizione) o;
			return this.nuovaPosizione == p.nuovaPosizione;
		}
		return false;
	}

	public int hashCode() {
		return super.hashCode() + getClass().hashCode() + nuovaPosizione.hashCode();
	}
	
	public String toString() {
		return "Nuova Posizione(" + getMittente() + " -> " + getDestinatario()
				+ ": " + nuovaPosizione + " )";
	}
	@Override
	public String toSerializableString() {
		Giocatore g = (Giocatore) this.getMittente();
		return g.getNome() + " IN " + this.getNuovaPosizione().getDisegno();
	}
}
