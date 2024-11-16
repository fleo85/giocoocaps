package attivita_io;

import javax.swing.JOptionPane;

import _gestioneeventi.EsecuzioneEnvironment;
import partita.Partita;

public class AttesaComando {
	public static void segnalefine(Partita p) {
		synchronized(p) {
			try {
				while (p.getStato() != Partita.Stato.FINITA) {
					p.wait();
				}
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
