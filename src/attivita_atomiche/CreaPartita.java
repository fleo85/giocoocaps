package attivita_atomiche;

import java.util.*;

import comprendere.*;
import _framework.*;
import casella.*;
import giocatore.*;
import partita.*;

public class CreaPartita implements Task {

	private boolean eseguita = false;
	private List<Casella> tabellone;
	private HashSet<Giocatore> giocatori;
	private Partita result;

	public CreaPartita(List<Casella> tabellone, HashSet<Giocatore> giocatori) {
		this.tabellone = tabellone;
		this.giocatori = giocatori;
	}

	public synchronized void esegui(Executor e) {

		if (e == null || eseguita == true)
			return;
		eseguita = true;

		result = new Partita("Nuova partita tra professori");

		/*
		 * Random generator = new Random ( (long) 0.3 ); int position = 0;
		 * Boolean goodPosition;
		 */

		Object[] aux = tabellone.toArray();

		Iterator<Giocatore> it = giocatori.iterator();
		while (it.hasNext()) {
			Giocatore current = it.next();

			// codice per rendere il posizionamento random
			// � significativo solo con tabelloni grandi

			/*
			 * goodPosition = false; while (!goodPosition) { position =
			 * generator.nextInt(tabellone.size()); goodPosition =
			 * !(aux[position].getClass() == CasellaSalto.class) ||
			 * !(((Casella)aux[position]).getLinkPrecedenza() == null); }
			 * current.inserisciLinkOccupare((Casella)aux[position]);
			 */

			current.inserisciLinkOccupare((Casella) aux[0]);
			TipoLinkComprendere l = new TipoLinkComprendere(current, result);
			result.inserisciLinkComprendere(l);
		}
	}

	public synchronized boolean estEseguita() {
		return eseguita;
	}

	public synchronized Partita getResult() {
		if (!eseguita)
			return null;
		return result;
	}
}