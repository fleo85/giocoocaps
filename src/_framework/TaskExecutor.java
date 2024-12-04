package _framework;

public final class TaskExecutor {
	public static TaskExecutor instance = new TaskExecutor();
	private TaskExecutor() {
	}
	public static TaskExecutor getInstance() {
		return instance;
	}
	public synchronized void perform(Task t) {
		t.esegui();
	}
}
