package attivita_composte;

import java.util.List;
import java.util.logging.Logger;

import _gestioneeventi.Environment;
import _gestioneeventi.EsecuzioneEnvironment;
import _log.Log;
import casella.Casella;
import casellaSalto.CasellaSalto;
import comprendere.TipoLinkComprendere;
import eventi.FinePartita;
import eventi.LancioDado;
import eventi.NuovaPosizione;
import giocatore.Giocatore;
import partita.Partita;

public class AttivitaMovimento implements Runnable {
	static Logger log = Log.creaLogger(EsecuzioneEnvironment.class.toString());
	private boolean eseguita = false;
	private Giocatore g;
	private int lancio;
	
	public AttivitaMovimento(Giocatore g, int lancio) {
		this.g = g;
		this.lancio = lancio;
	}

	@Override
	public synchronized void run() {
		if (eseguita == true)
			return;
		eseguita = true;
		
		Casella next = null;
		Boolean fine = false;
		for (int i = 1; i <= lancio && !fine; i++) {
			next = g.getLinkOccupare().getLinkSuccessore();

			if (next == null) {
				Environment.aggiungiEvento(new FinePartita(g, g.getLinkComprendere().getPartita(), g));
				fine = true;
			} else {
				g.inserisciLinkOccupare(next);
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
					break;
				}
				Environment.aggiungiEvento(new NuovaPosizione(g, null, next));
				log.info(g.getNome() + " transita in " + next.getDisegno());
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
				Environment.aggiungiEvento(new FinePartita(g, g.getLinkComprendere().getPartita(), g));
				fine = true;
			}
		}

		if (!fine) {
			log.info(g.getNome() + " si trova ora nella casella "
					+ next.getDisegno());
			Partita p = g.getLinkComprendere().getPartita();
			List<TipoLinkComprendere> links = p.getLinkComprendere();
			int i1 = links.indexOf(new TipoLinkComprendere(g, p));
			Giocatore prossimo = links.get(
					(i1 + 1) % p.getLinkComprendere().size())
					.getGiocatore();
			Environment.aggiungiEvento(new LancioDado(g, prossimo));
		} else
			log.info(g.getNome() + " ha vinto !!");
	}
	
	public synchronized boolean estEseguita() {
		return eseguita;
	}

}
