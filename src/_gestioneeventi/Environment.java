package _gestioneeventi;

import java.io.PrintWriter;
import java.util.*;
import java.util.concurrent.*;

public final class Environment { // NB con final non si possono definire
	// sottoclassi
	private Environment() { // NB non si possono costruire oggetti Environment
	} 

	private static ConcurrentHashMap<Listener, LinkedBlockingQueue<Evento>> codeEventiDeiListener = new ConcurrentHashMap<Listener, LinkedBlockingQueue<Evento>>();
	
	public static interface SerializableEvent {
		public String toSerializableString();
	}
	
	private static class LoggableEvent {
		private Listener destination;
		private Listener source;
		private Class<? extends SerializableEvent> eventClass;
		public LoggableEvent(Listener destination, Listener source, Class<? extends SerializableEvent> eventClass) {
			this.destination = destination;
			this.eventClass = eventClass;
			this.source = source;
		}
		public Listener getDestination() {
			return this.destination;
		}
		public Class<? extends SerializableEvent> getEventClass() {
			return this.eventClass;
		}
		public Listener getSource() {
			return this.source;
		}
		@Override
		public boolean equals(Object o) {
			if (o != null && getClass().equals(o.getClass())) {
				LoggableEvent b = (LoggableEvent) o;
				return (this.destination == null || b.destination == this.destination) &&
						(this.source == null || b.source == this.source) &&
						b.eventClass.equals(this.eventClass);
			} else
				return false;
		}
		@Override
		public int hashCode() {
			return this.eventClass.hashCode();
		}
	}
	
	private static HashMap<LoggableEvent, HashSet<PrintWriter>> remoteLogging = new HashMap<LoggableEvent, HashSet<PrintWriter>>();

	public static void addRemoteEventLogger(Listener destination, Listener source, Class<? extends SerializableEvent> eventClass, PrintWriter pw) {
		LoggableEvent le = new Environment.LoggableEvent(destination, source, eventClass);
		if (!remoteLogging.containsKey(le)) {
			remoteLogging.put(le, new HashSet<PrintWriter>());
		}
		remoteLogging.get(le).add(pw);
	}
	
	public static void removeRemoteEventLogger(Listener destination, Listener source, Class<? extends SerializableEvent> eventClass, PrintWriter pw) {
		LoggableEvent le = new Environment.LoggableEvent(destination, source, eventClass);
		if (remoteLogging.containsKey(le)) {
			remoteLogging.get(le).remove(pw);
		}
	}
	
	public static void addListener(Listener lr, EsecuzioneEnvironment e) {
		if (e == null)
			return;
		codeEventiDeiListener.put(lr, new LinkedBlockingQueue<Evento>());
		// Nota Listener inserito ma non attivo
	}

	public static Set<Listener> getInsiemeListener() {
		return codeEventiDeiListener.keySet();
	}

	public static void aggiungiEvento(Evento e) {
		// unico meccanismo per aggiungere eventi
		if (e == null)
			return;
		Listener destinatario = e.getDestinatario();
		
		// MODIFICA AD ENVIRONMENT PER SUPPORTARE TRASMISSIONE EVENTI
		if (e instanceof SerializableEvent) {
			SerializableEvent castedE = (SerializableEvent) e;
			LoggableEvent le = new Environment.LoggableEvent(e.getDestinatario(), e.getMittente(), castedE.getClass());
			if (remoteLogging.containsKey(le)) {
				for (PrintWriter pw: remoteLogging.get(le)) {
					String toSend = castedE.toSerializableString();
					if (toSend != null) {
						//La println su un printwriter chiuso non da errore, non c'è bisogno di try/catch
						pw.println(toSend);
						pw.flush();
					} else {
						System.out.println("WARNING: MESSAGGIO NULL NON INVIATO");
					}
				}
			}
		}
		// FINE MODIFICA AD ENVIRONMENT CLASSICO
		
		if (destinatario != null
				&& codeEventiDeiListener.containsKey(destinatario)) {
			// evento per un destinatario attivo
			try {
				codeEventiDeiListener.get(destinatario).put(e);
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
		} else if (destinatario == null) { // evento in broadcasting
			Iterator<Listener> itn = codeEventiDeiListener.keySet().iterator();
			while (itn.hasNext()) {
				Listener lr = itn.next();
				try {
					codeEventiDeiListener.get(lr).put(e);
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
			}
		}
	}

	public static Evento prossimoEvento(Listener lr)
			throws InterruptedException {
		// nota NON deve essere synchronized!!!
		return codeEventiDeiListener.get(lr).take();
	}

}