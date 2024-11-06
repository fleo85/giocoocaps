package casella;

public class Casella {

	protected final String disegno;
	protected Casella successore;

	public Casella(String d) {
		disegno = d;
		successore = null;
	}

	public String getDisegno() {
		return disegno;
	}

	public void inserisciLinkSuccessore(Casella c) {
		if (c != null)
			successore = c;
	}

	public void eliminaLinkSuccessore(Casella c) {
		if (c != null)
			successore = null;
	}

	public Casella getLinkSuccessore() { // se non ha successore restituisce
											// null
		return successore;
	}

	public String toString() {

		String aux = "Casella " + disegno;
		if (successore != null)
			aux = aux + " seguita da " + successore.getDisegno() + "\n";
		return aux;
	}
}
