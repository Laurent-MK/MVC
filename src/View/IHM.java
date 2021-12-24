package View;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import UtilitairesMK.Mutex;
import controler.Controler;
import model.Constantes;

import javax.swing.JButton;
import javax.swing.JTextPane;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.concurrent.Semaphore;
import java.awt.event.ActionEvent;
import javax.swing.JLabel;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JScrollPane;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.ListModel;
import javax.swing.JTextArea;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.JSeparator;
import javax.swing.SwingConstants;
import java.awt.Color;
import java.awt.Font;
import javax.swing.JRadioButton;
import javax.swing.JProgressBar;

public class IHM extends JFrame implements Constantes {

	/**
	 * proprietes de la classe
	 */

	// proprietes utilisees pour les widgets
	private static final long serialVersionUID = 1L;
	private final JPanel contentPane;
	private final JButton btnGo = new JButton("Go");
	private final JButton btnCreerThread = new JButton("Creer Threads");
	private final JTextPane txtTest = new JTextPane();
	private final JLabel lblNewLabel = new JLabel("");
	private final String chemin = "image.jpg";
	private final JLabel lblZoneConsole = new JLabel("Affichage des messages venant de l'application");
	private final JButton btnDel = new JButton("Del");
	private JTextField txtNbProducteur;
	private JTextField txtNbConsommateur;

	private JLabel lblEtatBuffer = new JLabel("0");
	private JLabel lblMaxLigneConsole = new JLabel(Long.toString(MAX_MSG_CONSOLE));

	

	// proprietes utilisees pour la gestion de l'IHM
	private static boolean isClicOnBtn_GO = false;
	private static boolean isclicOnBtn_CreationThread = false;
	private static boolean isClicOnBtnCreerSemaphore = false;
	private static boolean isClicOnBtnCreerMutex = false;
	
	
	private Controler controleur = null;
	
	
	private JTextArea textAreaConsole = new JTextArea();
	private JTextArea textAreaAffichageEtatThread = new JTextArea();
	private JTextArea textAreaTestMutex = new JTextArea();


	
	private Mutex sem;
	private JTextField txtNbJetons;
	private JTextField txtNbrThreads;

	private JTextArea textAreaTestSemaphore;
	private JButton btnTstSemaphore = new JButton("Test Semaphore");
	private JButton btnTstMutex = new JButton("Test Mutex");
	
	private JProgressBar progressBarConsole = new JProgressBar(0, MAX_MSG_CONSOLE);
	private JTextField textFieldFreqProd = new JTextField();
	private int freqProd;
	private JTextField textFieldTailleConsole = new JTextField();
	private int tailleConsole;


	
	/**
	 * fct executee lors du clic sur le bouton "GO"
	 * @param e
	 * @throws InterruptedException 
	 */
	private void btnGo_clic(ActionEvent e) throws InterruptedException {
		
		if (isclicOnBtn_CreationThread) {
			if (isClicOnBtn_GO) {
				JOptionPane.showMessageDialog(null, "Application deja lancee");
				return;
			}
				
			txtTest.setText("C'est parti !");
				
			controleur.dmdIHMGo();
			isClicOnBtn_GO = true;
			}
		else {
			JOptionPane.showMessageDialog(null, "cliquer d'abord sur le bouton de creation des Threads");
		}
	}
	
	/**
	 * methode applee lors du clic sur le bouton "Creer Threads"
	 * @param e
	 * @throws InterruptedException 
	 */
	private void btnCreerThread_clic(ActionEvent e) throws InterruptedException {
		
		if (isclicOnBtn_CreationThread) {
			JOptionPane.showMessageDialog(null, "Threads deja crees !");		
		}
		else {
//			JOptionPane.showMessageDialog(null, "Creation des threads : OK");
			isclicOnBtn_CreationThread = true;
			
			tailleConsole = Integer.parseInt(textFieldTailleConsole.getText());
			freqProd = Integer.parseInt(textFieldFreqProd.getText());
			
			lblMaxLigneConsole.setText(Long.toString(tailleConsole));
			
			
			controleur.dmdIHMCreationThread();
		}
	}
	
	
	
	/**
	 * fct exécutée lors du clic sur le bouton "Del"
	 * @param e
	 */
	private void btnDel_clic(ActionEvent e) {
		// A FAIRE !!!
	}
	
	
	private void btnClicCreerSemaphore(ActionEvent e) {
		isClicOnBtnCreerSemaphore = true;

		textAreaTestSemaphore.append("1 semaphore demande\n");
	}
	
	private void btnClicCreerMutex(ActionEvent e) {
		isClicOnBtnCreerMutex = true;
		
		textAreaTestSemaphore.append("demande de creation d'un MUTEX\n");
	}

	

	/**
	 * Remplir la zone d'affichage des threads presents
	 */
	public void affichageEtatThreads(String msg) {
	
		textAreaAffichageEtatThread.setText(msg);
	}
	
	
	
	public void affichageEtatThreads(ArrayList<String> listeEtatThreads) {
		
		textAreaAffichageEtatThread.removeAll();
		textAreaAffichageEtatThread.setText("");
		
		for(String message : listeEtatThreads) {
			textAreaAffichageEtatThread.append(message + "\n");
		}
	}
	
	
	
	/**
	 * obtenir le nombre de threads Producteurs a creer
	 */
	public int getNbThreadP() {
	
		return Integer.parseInt(txtNbProducteur.getText());
	}

	/**
	 * obtenir le nombre de threads Consommateurs a creer
	 */
	public int getNbThreadC() {
		
		return Integer.parseInt(txtNbConsommateur.getText());
	}

	public int getTailleBufferConsole() {
		return (tailleConsole);
	}
	
	public int getFreqProd() {
		return (freqProd);
	}
	

	/**
	 * affichage d'un nouveau message dans la zone de console de l'IHM
	 * @param msg
	 */
	public void affichageConsole(String msg) {
		textAreaConsole.append(msg);
		
//		textAreaTestMutex.append("\n" + Long.toString(textAreaConsole.getLineCount()));
		

		lblEtatBuffer.setText(Long.toString(textAreaConsole.getLineCount()));
		progressBarConsole.setValue(textAreaConsole.getLineCount());
		
		if (textAreaConsole.getLineCount() > this.tailleConsole) {
			textAreaConsole.setText("");
		}
	}
	
	
	/**
	 * remplir la zone avec les messages dans la liste
	 * @param message
	 */
	public void affichageConsole(ArrayList<String> messageConsole) {

		for(String message : messageConsole) {
			String ligne;
			
			ligne = message + "\n";
		}
		
		textAreaTestMutex.append(Long.toString(textAreaConsole.getRows()));
		
		if (textAreaConsole.getLineCount() > this.tailleConsole) {
			textAreaConsole.setText("");
		}
	}

	
	
	
	/**
	 * pour initialiser à des valeurs par defaut des champs de saisie de l'IHM
	 */
	private void initIHM() {
		txtNbProducteur.setText(Integer.toString(DEFAULT_NB_THREAD_PROD)); 		// on fixe le mini
		txtNbConsommateur.setText(Integer.toString(DEFAULT_NB_THREAD_CONS)); 	// on fixe le mini
		
 /*   	msgQ_Console = new ArrayBlockingQueue<String>(ihmApplication.getTailleBufferConsole());

    	
    	// lancement du thread de gestion de la console
        console = new ConsoleMK("Console", NUMERO_CONSOLE, PRIORITE_CONSOLE, msgQ_Console, ihmApplication, sem);
        new Thread(console).start();
       
    	console.sendMsgToConsole("creation et lancement du thread de console");
*/
	
		textAreaTestMutex.append("init. zone : textAreaTestMutex reussie !");
	}
	
	
	/**======================================== IHM ===============================================
	 * Create the frame.
	 * @param sem 
	 */
	public IHM(Controler controleur, Mutex sem) {
		
		this.controleur = controleur;
		this.sem = sem;
		
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 1228, 892);
		
		contentPane = new JPanel();		// conteneur des objets graphiques
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);	// integration du contentPane dans le conteneur
		contentPane.setLayout(null);
		
		/**
		 * Ajout d'une zone de texte
		 */
		txtTest.setBounds(180, 222, 144, 23);
		contentPane.add(txtTest);
		
		
		/**
		 * ajout d'un bouton
		 */
		btnGo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// action executee lors du clic
				try {
					btnGo_clic(e);
				} catch (InterruptedException e1) {
					// TODO Bloc catch généré automatiquement
					e1.printStackTrace();
				}
			}
		});
		btnGo.setBounds(108, 222, 58, 23);
		contentPane.add(btnGo);
		

		/**
		 * ajout du bouton de creation des threads
		 */
		btnCreerThread.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// action executee lors du clic
				try {
					btnCreerThread_clic(e);
				} catch (InterruptedException e1) {
					// TODO Bloc catch généré automatiquement
					e1.printStackTrace();
				}
			}
		});
		btnCreerThread.setBounds(425, 280, 155, 23);
		contentPane.add(btnCreerThread);

		
		
		
		/**
		 * ajout d'une image
		 */
		String ressource = getClass().getClassLoader().getResource(chemin).getPath();
		lblNewLabel.setIcon(new ImageIcon(ressource));
		lblNewLabel.setBounds(596, 0, 117, 61);
		contentPane.add(lblNewLabel);
		lblZoneConsole.setBounds(23, 284, 355, 14);
		
		contentPane.add(lblZoneConsole); 		// description de la zone de texte
		btnDel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				btnDel_clic(e);
			}
		});
		btnDel.setBounds(414, 68, 89, 23);

		
		txtNbProducteur = new JTextField();
		txtNbProducteur.setBounds(225, 154, 86, 20);
		contentPane.add(txtNbProducteur);
		txtNbProducteur.setColumns(10);
		
		JLabel lblNewLabelProdcuteur = new JLabel("Nombre de producteurs");
		lblNewLabelProdcuteur.setBounds(22, 157, 188, 14);
		contentPane.add(lblNewLabelProdcuteur);
		
		
		
		txtNbConsommateur = new JTextField();
		txtNbConsommateur.setBounds(225, 179, 86, 20);
		contentPane.add(txtNbConsommateur);
		txtNbConsommateur.setColumns(10);


		JLabel lblNewLabelConsommateurs = new JLabel("Nombre de consommateurs");
		lblNewLabelConsommateurs.setBounds(23, 182, 218, 14);
		contentPane.add(lblNewLabelConsommateurs);

		/**
		 * initialisation des variables de l'IHM
		 */
//		initIHM();
				
		// bouton "Del"
		contentPane.add(btnDel);
		
		// zone d'afichage (avec un scroll) des messages venant de l'application
		JScrollPane scrollPaneConsole = new JScrollPane();
		scrollPaneConsole.setBounds(25, 379, 374, 463);
		contentPane.add(scrollPaneConsole);
		
//		JTextArea textAreaConsole = new JTextArea();
		scrollPaneConsole.setViewportView(textAreaConsole);
		
		JScrollPane scrollPaneEtatThread = new JScrollPane();
		scrollPaneEtatThread.setBounds(426, 379, 258, 463);
		contentPane.add(scrollPaneEtatThread);
		
		scrollPaneEtatThread.setViewportView(textAreaAffichageEtatThread);
		
		JLabel lblNewLabel_1 = new JLabel("Etat des threads  : true = en vie / false = mort");
		lblNewLabel_1.setBounds(426, 354, 272, 14);
		contentPane.add(lblNewLabel_1);
		
		JSeparator separator = new JSeparator();
		separator.setForeground(Color.BLUE);
		separator.setOrientation(SwingConstants.VERTICAL);
		separator.setBounds(723, 0, 2, 842);
		contentPane.add(separator);
		

		/*****************************/
		btnTstSemaphore.setBounds(749, 222, 144, 23);
		contentPane.add(btnTstSemaphore);
		
		btnTstMutex.setBounds(1005, 222, 127, 23);
		contentPane.add(btnTstMutex);
		
		txtNbJetons = new JTextField();
		txtNbJetons.setText("1");
		txtNbJetons.setBounds(749, 87, 40, 20);
		contentPane.add(txtNbJetons);
		txtNbJetons.setColumns(10);
		
		JLabel lblNewLabel_2 = new JLabel("Nbr jetons dans le semaphore");
		lblNewLabel_2.setBounds(749, 66, 242, 14);
		contentPane.add(lblNewLabel_2);
		
		JLabel lblNewLabel_3 = new JLabel("TESTS DES SEMAPHORES ET MUTEX");
		lblNewLabel_3.setFont(new Font("Tahoma", Font.BOLD, 20));
		lblNewLabel_3.setBounds(794, 28, 367, 18);
		contentPane.add(lblNewLabel_3);
		
		JLabel lblNewLabel_3_1 = new JLabel("Test du multithreading Producteur / Consommateur");
		lblNewLabel_3_1.setFont(new Font("Tahoma", Font.BOLD, 20));
		lblNewLabel_3_1.setBounds(44, 28, 640, 18);
		contentPane.add(lblNewLabel_3_1);
		
		JButton btnCreerSemaphore = new JButton("Creer Semaphore");
		btnCreerSemaphore.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				btnClicCreerSemaphore(e);
			}
		});
		btnCreerSemaphore.setBounds(749, 178, 160, 23);
		contentPane.add(btnCreerSemaphore);
		
		JButton btnCreerMutex = new JButton("Creer MUTEX");
		btnCreerMutex.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				btnClicCreerMutex(e);
			}
		});
		btnCreerMutex.setBounds(1005, 178, 117, 23);
		contentPane.add(btnCreerMutex);
		
		JLabel lblNewLabel_4 = new JLabel("Nbr threads concurents");
		lblNewLabel_4.setBounds(749, 122, 194, 14);
		contentPane.add(lblNewLabel_4);
		
		txtNbrThreads = new JTextField();
		txtNbrThreads.setText("2");
		txtNbrThreads.setBounds(749, 137, 40, 20);
		contentPane.add(txtNbrThreads);
		txtNbrThreads.setColumns(10);
		
		JScrollPane scrollPaneConsoleSemaphore = new JScrollPane();
		scrollPaneConsoleSemaphore.setBounds(735, 293, 218, 549);
		contentPane.add(scrollPaneConsoleSemaphore);
		
		textAreaTestSemaphore = new JTextArea();
		scrollPaneConsoleSemaphore.setViewportView(textAreaTestSemaphore);

		JScrollPane scrollPaneConsoleMutex = new JScrollPane();
		scrollPaneConsoleMutex.setBounds(974, 293, 214, 549);
		contentPane.add(scrollPaneConsoleMutex);
		
		scrollPaneConsoleMutex.setViewportView(textAreaTestMutex);
		
		progressBarConsole.setBounds(23, 354, 288, 14);
		contentPane.add(progressBarConsole);
		
		JLabel lblNewLabel_5 = new JLabel("Etat du buffer interne");
		lblNewLabel_5.setBounds(25, 333, 160, 14);
		contentPane.add(lblNewLabel_5);
		

		lblEtatBuffer.setBounds(154, 332, 46, 17);
		contentPane.add(lblEtatBuffer);
		
		JLabel lblNewLabel_6 = new JLabel("/");
		lblNewLabel_6.setBounds(194, 333, 27, 14);
		contentPane.add(lblNewLabel_6);
		
		lblMaxLigneConsole.setBounds(210, 333, 46, 14);
		contentPane.add(lblMaxLigneConsole);
		
		JLabel lblNewLabel_7 = new JLabel("Frequence de production (msec)");
		lblNewLabel_7.setBounds(23, 90, 162, 14);
		contentPane.add(lblNewLabel_7);
		
//		textFieldFreqProd = new JTextField();
		textFieldFreqProd.setText("500");
		textFieldFreqProd.setBounds(225, 87, 86, 20);
		contentPane.add(textFieldFreqProd);
		textFieldFreqProd.setColumns(10);
		
		JLabel lblNewLabel_8 = new JLabel("Taille de la console (nbr de lignes)");
		lblNewLabel_8.setBounds(22, 122, 188, 14);
		contentPane.add(lblNewLabel_8);
		
//		textFieldTailleConsole = new JTextField();
		textFieldTailleConsole.setText("100");
		textFieldTailleConsole.setBounds(225, 119, 86, 20);
		contentPane.add(textFieldTailleConsole);
		textFieldTailleConsole.setColumns(10);
	
		initIHM();

	}
}
