package applicazione;

import java.io.IOException;
import java.net.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Logger;

import javax.swing.JLabel;
import javax.swing.JOptionPane;

import _gestioneeventi.EsecuzioneEnvironment;
import _log.Log;

public class Server implements Runnable {
	static Logger log = Log.creaLogger(EsecuzioneEnvironment.class.toString());
	private ServerSocket lis = null;
	private String port;
	private List<ClientThread> ctList;
	private JLabel clientsLabel;
	
	public Server(){
		port = "4400";
		ctList = new LinkedList<ClientThread>();
	}
	
	public Server(String text, JLabel clientsLabel) {
		port = text;
		ctList = new ArrayList<ClientThread>();
		this.clientsLabel = clientsLabel;
	}

	public void run() {
		EsecuzioneEnvironment.attivaListener();
		try {
			lis = new ServerSocket(Integer.parseInt(port));
		} catch (IOException e1) {
			e1.printStackTrace();
			JOptionPane.showMessageDialog(null, "Errore nella creazione del ServerSocket, applicazione dismessa", null,
					0);
			System.exit(1);
		}
		log.info("Server Avviato");
		Socket sock = null;

		while (true) {
			try {
				sock = lis.accept();
			} catch (IOException e) {
				break;
			}
			log.info("Socket creata, connessione accettata");
			ClientThread cl = new ClientThread(sock, this);
			Thread tr = new Thread(cl);
			tr.start();
			ctList.add(cl);
			clientsLabel.setText(""+ctList.size());
		}
		EsecuzioneEnvironment.disattivaListener();
	}
	
	public void stop() throws IOException{
		lis.close();
		for (ClientThread clientThread : ctList) {
			clientThread.stop();
		}
		ctList = new LinkedList<ClientThread>();
	}
	
	
	public void removeFromCTList(ClientThread ct){
		synchronized (ctList) {
			ctList.remove(ct);
			clientsLabel.setText(""+ctList.size());
		}
	}
	
}
