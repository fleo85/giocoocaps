package partita;

import java.util.*;

import _framework.TaskExecutor;
import _gestioneeventi.Evento;
import _gestioneeventi.Listener;
import comprendere.*;
import giocatore.Giocatore.Stato;

public class Partita implements Listener {
	private final String nome;
	private ArrayList<TipoLinkComprendere> insiemeLink;
	public final int MINCOMPRENDERE = 2;

	public Partita(String n) {
		nome = n;
		insiemeLink = new ArrayList<TipoLinkComprendere>();
	}

	public String getNome() {
		return nome;
	}

	public void inserisciLinkComprendere(TipoLinkComprendere t) {
		if (t != null && t.getPartita() == this)
			ManagerComprendere.inserisci(t);
	}

	public void eliminaLinkComprendere(TipoLinkComprendere t) {
		if (t != null && t.getPartita() == this)
			ManagerComprendere.elimina(t);
	}

	@SuppressWarnings("unchecked")
	public List<TipoLinkComprendere> getLinkComprendere() {
		if (quantiGiocatori() < MINCOMPRENDERE)
			throw new RuntimeException("Eccezione Molteplicita'");
		return (ArrayList<TipoLinkComprendere>) insiemeLink.clone();
	}

	public void inserisciPerManagerComprendere(ManagerComprendere a) {
		if (a != null && !insiemeLink.contains(a.getLink()))
			insiemeLink.add(a.getLink());
	}

	public void eliminaPerManagerComprendere(ManagerComprendere a) {
		if (a != null)
			insiemeLink.remove(a.getLink());
	}

	public int quantiGiocatori() {
		return insiemeLink.size();
	}
	
	public static enum Stato {
		INIZIATA, FINITA
	}

	Stato statocorrente = Stato.INIZIATA;

	public Stato getStato() {
		return statocorrente;
	}

	@Override
	public void fired(Evento e) {
		TaskExecutor.getInstance().perform(new PartitaFired(this, e));
	}

}
