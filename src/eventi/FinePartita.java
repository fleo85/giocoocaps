package eventi;

import _gestioneeventi.Environment;
import _gestioneeventi.Evento;
import _gestioneeventi.Listener;
import giocatore.Giocatore;

public class FinePartita extends Evento implements Environment.SerializableEvent {
	private Giocatore vincitore;
	public FinePartita(Listener m, Listener d, Giocatore vincitore) {
		super(m, d);
		this.vincitore = vincitore;
	}

	public boolean equals(Object o) {
		return super.equals(o);
	}

	public int hashCode() {
		return super.hashCode() + getClass().hashCode();
	}

	public String toString() {
		return "FinePartite(" + getMittente() + " -> " + getDestinatario() + ")";
	}
	
	public Giocatore getVincitore() {
		return vincitore;
	}
			
	@Override
	public String toSerializableString() {
		if (vincitore != null) {
			return vincitore.getNome() + " FINE";
		} else {
			return null;
		}
	}
}
