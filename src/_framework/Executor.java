package _framework;

//Nota e' importantissimo che perform sia synchronized in Executor!
//Serve per avere un accesso controllato agli oggetti del diagramma delle classi
public final class Executor {
	private Executor() {
	}

	public synchronized static void perform(Task t) {
		t.esegui(new Executor());
	}
}
