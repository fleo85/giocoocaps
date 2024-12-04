package _gestioneeventi;

public class Evento {
	private Listener mittente;
	private Listener destinatario;

	public Evento(Listener m, Listener d) {
		this.mittente = m;
		this.destinatario = d;
	}

	public Listener getMittente() {
		return mittente;
	}

	public Listener getDestinatario() {
		return destinatario;
	}

	@Override
	public boolean equals(Object o) {
		if (o == null)
			return false;
		if (! getClass().equals(o.getClass()))
			return false;
		Evento e = (Evento) o;
		return this.mittente == e.mittente && this.destinatario == e.destinatario;
	}
 
	@Override
	public int hashCode() {
		return this.mittente.hashCode() + this.destinatario.hashCode();  
	}

	@Override
	public String toString() {
		return "(Evento " + this.mittente + " -> " + this.destinatario + ")";
	}
}

