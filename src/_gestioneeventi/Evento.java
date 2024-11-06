package _gestioneeventi;

public class Evento {
	private Listener mittente;
	private Listener destinatario;

	public Evento(Listener m, Listener d) {
		mittente = m; // se null, il mittente non e' rilevante (eg, l'evento e'
		// esogeno)
		destinatario = d; // se null, il messaggio e' in broadcasting
	}

	public Listener getMittente() {
		return mittente;
	}

	public Listener getDestinatario() {
		return destinatario;
	}

	public boolean equals(Object o) {
		if (o != null && getClass().equals(o.getClass())) {
			Evento e = (Evento) o;
			return mittente == e.mittente && destinatario == e.destinatario;
		} else
			return false;
	}

	public int hashCode() {
		return mittente.hashCode() + destinatario.hashCode();
	}
}
