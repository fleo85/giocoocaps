package giocatore;

import comprendere.*;
import partita.Partita;
import casella.*;
import _framework.*;
import _gestioneeventi.*;

public class Giocatore implements Listener {
	private final String nome;
	private String avatar;
	private Casella link;
	private TipoLinkComprendere partita;

	public Giocatore(String n) {
		nome = n;
	}

	public String getNome() {
		return nome;
	}

	public String getAvatar() {
		return avatar;
	}

	public void setAvatar(String avatar) {
		if (statocorrente != Stato.ALLENAMENTO)
			throw new RuntimeException(
					"Non si puo' modificare il giocatore se non e' in allenamento");
		this.avatar = avatar;
	}

	public void inserisciLinkComprendere(TipoLinkComprendere t) {
		if (t != null && t.getGiocatore() == this)
			ManagerComprendere.inserisci(t);
	}

	public void eliminaLinkComprendere(TipoLinkComprendere t) {
		if (t != null && t.getGiocatore() == this)
			ManagerComprendere.elimina(t);
	}

	public TipoLinkComprendere getLinkComprendere() {
		return partita;
	}

	public void inserisciPerManagerComprendere(ManagerComprendere a) {
		if (a != null)
			partita = a.getLink();
	}

	public void eliminaPerManagerComprendere(ManagerComprendere a) {
		if (a != null)
			partita = null;
	}

	public void inserisciLinkOccupare(Casella c) {
		if (c != null)
			link = c;
	}

	public void eliminaLinkOccupare(Casella c) {
		if (c != null)
			link = null;
	}

	public Casella getLinkOccupare() {
		return link;
	}

	// gestione stato

	public static enum Stato {
		ALLENAMENTO, INPARTITA
	}

	Stato statocorrente = Stato.ALLENAMENTO;
	public Partita partitaCorrente = null;
	Thread attivitaMovimento = null;

	public Stato getStato() {
		return statocorrente;
	}

	public void fired(Evento e) {
		TaskExecutor.getInstance().perform(new GiocatoreFired(this, e));
	}

	public String toString() {
		return nome;
	}
}
