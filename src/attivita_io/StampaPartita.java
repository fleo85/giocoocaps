package attivita_io;

import partita.*;
import casella.*;
import giocatore.*;
import java.util.*;
import comprendere.*;

public class StampaPartita {

	public static void perform(List<Casella> tabellone, Partita partita) {

		System.out.println("== TABELLONE ==");
		System.out.println(tabellone);

		System.out.println("== PARTITA ==");
		System.out.println(partita.getNome() + ": ");

		Iterator<TipoLinkComprendere> it = partita.getLinkComprendere()
				.iterator();
		while (it.hasNext()) {
			Giocatore g = it.next().getGiocatore();
			System.out.print(g);
			System.out.println(" parte da " + g.getLinkOccupare().getDisegno());

		}

	}
}
