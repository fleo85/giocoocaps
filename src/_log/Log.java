package _log;

import java.util.logging.Logger;
import java.util.logging.Level;
import java.util.logging.SimpleFormatter;
import java.util.logging.Handler;

public class Log {
	static String logFormat = "%4$-8.8s %2$-34.34s %5$s\n";

	public static Logger creaLogger(String name, Level level) {
		Logger log = Logger.getLogger(name);
		System.setProperty("java.util.logging.SimpleFormatter.format", logFormat);
		SimpleFormatter f = new SimpleFormatter();
		Handler[] h = log.getParent().getHandlers();
		h[0].setFormatter(f);
		h[0].setLevel(Level.ALL);
		log.setLevel(level);
		return log;
	}

	public static Logger creaLogger(String name) {
		return creaLogger(name, Level.ALL);
	}
}
