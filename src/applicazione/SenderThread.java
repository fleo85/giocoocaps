package applicazione;

import java.io.PrintWriter;
import java.util.HashSet;
import java.util.List;

import casella.Casella;
import casellaSalto.CasellaSalto;
import giocatore.Giocatore;

public class SenderThread implements Runnable {

	private PrintWriter pw;
	private List<Casella> tabellone;


	public SenderThread(PrintWriter pw, List<Casella> tabellone) {
		this.pw = pw;
		this.tabellone = tabellone;
	}

	@Override
	public void run() {
		pw.println("TABELLONE");
		pw.println(this.tabellone.size());
		for (Casella c: this.tabellone) {
			pw.println(c.getDisegno());
		}
		pw.flush();
	}

}
