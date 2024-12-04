package attivita_atomiche;

import java.util.*;

import _framework.*;
import casella.*;
import casellaSalto.CasellaSalto;

public class Verifica implements Task {

	private boolean eseguita = false;
	private List<Casella> tabellone;
	private Boolean esito = false;

	public Verifica(List<Casella> tabellone) {
		this.tabellone = tabellone;
	}

	public synchronized void esegui() {

		if (eseguita == true)
			return;
		eseguita = true;

		esito = verificaNormale(tabellone) && verificaSuccessore(tabellone)
				&& verificaSalti(tabellone);
	}

	public synchronized boolean estEseguita() {
		return eseguita;
	}

	public synchronized Boolean getResult() {
		if (!eseguita)
			return null;
		return esito;
	}

	private synchronized Boolean verificaNormale(List<Casella> tabellone) {
		Iterator<Casella> it = tabellone.iterator();
		while (it.hasNext()) {
			Casella c = it.next();
			if (c.getClass() == Casella.class)
				return true;
		}
		return false;
	}

	private synchronized Boolean verificaSuccessore(List<Casella> tabellone) {
		Iterator<Casella> it = tabellone.iterator();

		while (it.hasNext()) {
			Casella succ = it.next().getLinkSuccessore();
			if ((succ != null) && !tabellone.contains(succ))
				return false;
		}
		return true;
	}

	private synchronized Boolean verificaSalti(List<Casella> tabellone) {
		Iterator<Casella> it = tabellone.iterator();
		while (it.hasNext()) {
			Casella c = it.next();
			if (c.getClass() == CasellaSalto.class) {
				CasellaSalto cs = (CasellaSalto) c;
				Casella target = cs.getLinkSaltare();
				if (!tabellone.contains(target))
					return false;
			}
		}
		return true;
	}

}