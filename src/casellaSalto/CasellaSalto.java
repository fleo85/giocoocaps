package casellaSalto;

import casella.*;

public class CasellaSalto extends Casella {

	private final String nome;
	private Casella salto;

	public CasellaSalto(String n, String d) {
		super(d);
		nome = n;
	}

	public String getNome() {
		return nome;
	}

	public void inserisciLinkSaltare(Casella c) {
		if (c != null)
			salto = c;
	}

	public void eliminaLinkSaltare(Casella c) {
		if (c != null)
			salto = null;
	}

	public Casella getLinkSaltare() {
		if (salto == null)
			throw new RuntimeException(
					"Eccezione molteplicita': la molteplicita' deve essere 1..1");
		return salto;
	}

	public String toString() {
		return "Casella di salto " + nome + ":" + this.getDisegno()
				+ " seguita da casella " + successore.getDisegno()
				+ " con salto in casella " + salto.getDisegno() + "\n";
	}
}
