package applicazione;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.HashSet;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import attivita_composte.AttivitaPrincipale;
import casella.Casella;
import casellaSalto.CasellaSalto;

public class GUI_Main extends JFrame implements ActionListener{
	/**
	 * 
	 */
	private static final long serialVersionUID = -4522882138892979962L;

	private JPanel centralPanel;
	private JPanel southPanel;
	private JPanel topPanel;
	private JButton startBtn;
	private JButton stopBtn;
	private JTextField portTxt;
	private JLabel portLbl;
	private JLabel clientsLabel;

	private Server serv;

	public GUI_Main() {

		// Widgets
		startBtn = new JButton("Start");
		stopBtn = new JButton("Stop");
		
		portTxt = new JTextField(10);
		portLbl = new JLabel("Port");
		portTxt.setText("4400");
		
		// Layout
		topPanel = new JPanel();
		topPanel.add(portLbl);
		topPanel.add(portTxt);

		centralPanel = new JPanel();
		centralPanel.add(startBtn);
		centralPanel.add(stopBtn);
		
		southPanel = new JPanel();
		clientsLabel = new JLabel("0");
		clientsLabel.setHorizontalAlignment(SwingConstants.CENTER);
		southPanel.add(clientsLabel);

		Container mainCont = this.getContentPane();
		mainCont.add(centralPanel, BorderLayout.CENTER);
		mainCont.add(topPanel, BorderLayout.NORTH);
		mainCont.add(southPanel, BorderLayout.SOUTH);
		
		// Listeners
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		startBtn.setActionCommand("start");
		startBtn.addActionListener(this);
		stopBtn.setActionCommand("stop");
		stopBtn.addActionListener(this);
		
		// Initial state
		stopBtn.setEnabled(false);
		
		setSize(220,120);
	}

	
	public static void main(String[] args) {
		GUI_Main gm = new GUI_Main();
		gm.setVisible(true);
	}


	@Override
	public void actionPerformed(ActionEvent e) {
		String cmd = e.getActionCommand();
		if (cmd.equals("start")) {
			try {
				serv = new Server(portTxt.getText(), clientsLabel);
			} catch (NumberFormatException ex) {
				JOptionPane.showMessageDialog(this, "Formato porta errato", "Errore", JOptionPane.ERROR_MESSAGE);
				return;
			}
			Thread avv = new Thread(serv);
			avv.start();

			startBtn.setEnabled(false);
			stopBtn.setEnabled(true);
			setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		}
		else if(cmd.equals("stop")){
			try {
				serv.stop();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			startBtn.setEnabled(true);
			stopBtn.setEnabled(false);
			setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		}		
	}
}
