package giocatore;

import java.util.*;
import comprendere.*;
import partita.*;
import casella.*;
import casellaSalto.*;
import _framework.*;
import _gestioneeventi.*;
import eventi.*;
import giocatore.Giocatore.Stato;

class GiocatoreFired implements Task {
	private boolean eseguita = false;
	private Giocatore g;
	private Evento e;

	public GiocatoreFired(Giocatore g, Evento e) {
		this.g = g;
		this.e = e;
	}

	// usati per simulare il lancio del dado
	private Random generator = new Random((long) 195689.987);

	private int randomNumber() {
		return this.generator.nextInt(6) + 1;
	}

	public synchronized void esegui(Executor exec) {
		if (eseguita || exec == null
				|| (e.getDestinatario() != g && e.getDestinatario() != null))
			return;
		eseguita = true;
		switch (g.getStato()) {
		case ALLENAMENTO:
			if (e.getClass() == Inizio.class) {
				g.statocorrente = Stato.INPARTITA;
			}
			break;
		case INPARTITA:
			if (e.getClass() == LancioDado.class) {
				LancioDado d = (LancioDado) e;

				System.out.print(g.getNome() + " ha lanciato " + d.getLancio()
						+ " e si sta muovendo ...");

				Casella next = null;
				Boolean fine = false;
				for (int i = 1; i <= d.getLancio() && !fine; i++) {
					next = g.getLinkOccupare().getLinkSuccessore();

					if (next == null) {
						Environment.aggiungiEvento(new Fine(g, null));
						fine = true;
					} else {
						g.inserisciLinkOccupare(next);
						try {
							Thread.sleep(1000);
						} catch (InterruptedException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
						Environment.aggiungiEvento(new NuovaPosizione(g, null, next));
						System.out.print(".." + next.getDisegno());
					}
				}
				while (!fine && next.getClass() == CasellaSalto.class) {
					next = ((CasellaSalto) g.getLinkOccupare())
							.getLinkSaltare();
					g.inserisciLinkOccupare(next);
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					Environment.aggiungiEvento(new NuovaPosizione(g, null, next));
					if (next.getLinkSuccessore() == null) {
						Environment.aggiungiEvento(new Fine(g, null));
						fine = true;
					}
				}

				if (!fine) {
					System.out.println("... si trova ora nella casella "
							+ next.getDisegno());
					Partita p = g.getLinkComprendere().getPartita();
					List<TipoLinkComprendere> links = p.getLinkComprendere();
					int i1 = links.indexOf(new TipoLinkComprendere(g, p));
					Giocatore prossimo = links.get(
							(i1 + 1) % p.getLinkComprendere().size())
							.getGiocatore();
					Environment.aggiungiEvento(new LancioDado(g, prossimo, this
							.randomNumber()));
				} else
					System.out.println("... ha vinto !!");
			} else if (e.getClass() == Fine.class)
				g.statocorrente = Stato.ALLENAMENTO;
			break;
		default:
			throw new RuntimeException("Stato corrente non riconosciuto.");
		}
	}

	public synchronized boolean estEseguita() {
		return eseguita;
	}
}
