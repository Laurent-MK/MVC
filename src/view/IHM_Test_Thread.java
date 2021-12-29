package view;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import controler.ControlerTestThread;
import model.ClientSocket;
import model.Constantes;
import utilitairesMK.Mutex;
import utilitairesMK.MsgToConsole;

import javax.swing.JButton;
import javax.swing.JTextPane;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.awt.event.ActionEvent;
import javax.swing.JLabel;
import javax.swing.ImageIcon;
import javax.swing.JScrollPane;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JTextArea;
import javax.swing.JSeparator;
import javax.swing.SwingConstants;
import java.awt.Color;
import java.awt.Font;
import javax.swing.JProgressBar;


/**
 * META KONSULTING
 * 
 * Classe de gestion de l'IHM
 * 
 * @author balou
 *
 */

public class IHM_Test_Thread extends JFrame implements Constantes, IHM {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7783961162733122279L;

	/**
	 * proprietes de la classe
	 */

	// proprietes utilisees pour la création de l'IHM (bouton, labels, zone d'affichage ...
	private final JPanel contentPane;

	private final String logo = "image.jpg";
	
	private final JTextPane txtTest = new JTextPane();
	
	private final JLabel lblNewLabel = new JLabel("");
	private final JLabel lblZoneConsole = new JLabel("Affichage des messages venant de l'application");
	private final JLabel lblEtatBuffer = new JLabel("0");
	private final JLabel lblMaxLigneConsole = new JLabel(Long.toString(MAX_MSG_CONSOLE));
	private final JLabel lblTailleMQConsole = new JLabel(Long.toString(TAILLE_MSG_Q_CONSOLE));
	private final JLabel lblEtatMQ = new JLabel("0");

	private final JTextArea textAreaConsole = new JTextArea();
	private final JTextArea textAreaAffichageEtatThread = new JTextArea();
	private final JTextArea textAreaTestMutex = new JTextArea();
	private final JTextArea textAreaTestSemaphore = new JTextArea();
	private final JTextArea textAreaTestPool = new JTextArea();
	
	private final JTextField textFieldNbProducteur = new JTextField();
	private final JTextField textFieldNbConsommateur = new JTextField();
	private final JTextField textFieldFreqProd = new JTextField();
	private final JTextField textFieldMaxLigneConsole = new JTextField();
	private final JTextField textFieldTailleQueueConsole = new JTextField();
	private final JTextField textFieldNbTestMutex = new JTextField();
	private final JTextField textFieldNbTestSem = new JTextField();
	private final JTextField textFieldAdresseIP = new JTextField();
	


	private final JProgressBar progressBarConsole = new JProgressBar(0, MAX_MSG_CONSOLE);
	private final JProgressBar progressBarMQ = new JProgressBar(0, TAILLE_MSG_Q_CONSOLE);

	// proprietes utilisees pour la gestion de l'IHM
	private boolean isClicOnBtnGO = false;
	private boolean isclicOnBtnInitAppli = false;
	private Mutex mutexSynchroIHM_Controler;
	private JTextField txtNbJetons;
	private JTextField txtNbrThreadsTestSem;
	private ControlerTestThread controleur;
	private int freqProd;
	private int tailleConsole;
	private int tailleMqConsole;
	private JTextField txtNbrThreadTestMutex;
	private JTextField textField;
	
	
	
	//+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
	/**
	 * ============================ methodes appelees sur clic sur les boutons de l'IHM =======================================
	 */
	
	/**
	 * methode d'initialisation des threads de tests Producteurs / Consommateurs
	 * et du thread de la console 
	 */
	private void initAppli(ActionEvent e) {
		
		if (! isclicOnBtnInitAppli) {
			try {
				btnInitAppli(e); // on commence par initialiser l'IHM
			} catch (InterruptedException e1) {
				// TODO Bloc catch généré automatiquement
				e1.printStackTrace();
				}
			}
	}
	
	/**
	 * fct executee lors du clic sur le bouton "GO"
	 * @param e
	 * @throws InterruptedException 
	 */
	private void btnGo_clic(ActionEvent e) throws InterruptedException {
		
		if (isclicOnBtnInitAppli) {
			if (isClicOnBtnGO) {
				JOptionPane.showMessageDialog(null, "Application deja lancee");
				return;
				}
		}

		txtTest.setText("C'est parti !");
		initAppli(e);	// initialisation de l'application
		
		controleur.dmdIHMGo();
		isClicOnBtnGO = true;
//			JOptionPane.showMessageDialog(null, "cliquer d'abord sur le bouton de creation des Threads");
		}

	
	
	/**
	 * methode applee lors du clic sur le bouton "Creer Threads"
	 * @param e
	 * @throws InterruptedException 
	 */
	private void btnInitAppli(ActionEvent e) throws InterruptedException {
		
		if (isclicOnBtnInitAppli) {
			JOptionPane.showMessageDialog(null, "Threads deja crees !");		
		}
		else {
//			JOptionPane.showMessageDialog(null, "Creation des threads : OK");
			isclicOnBtnInitAppli = true;
			
			tailleConsole = Integer.parseInt(textFieldMaxLigneConsole.getText());
			freqProd = Integer.parseInt(textFieldFreqProd.getText());
			this.tailleMqConsole = Integer.parseInt(textFieldTailleQueueConsole.getText());
			
			lblMaxLigneConsole.setText(Long.toString(tailleConsole));
			progressBarConsole.setMaximum(tailleConsole);
			
			lblTailleMQConsole.setText(Long.toString(tailleMqConsole));
			progressBarMQ.setMaximum(tailleMqConsole);
			
			mutexSynchroIHM_Controler.mutexRelease(); 	// liberation du mutex de synchro de l'IHM et du controleur
			
			Thread.yield();		// relache le processeur
			Thread.sleep(10);	// pour laisser le temps au thread controleur de s'initialiser
		
			mutexSynchroIHM_Controler.mutexGet();

			controleur.dmdIHMCreationThread();
		}
	}
		
	
	/**
	 * fct exécutée lors du clic sur le bouton "STOP"
	 * @param e
	 */
	private void btnClicStop(ActionEvent e) {
		// A FAIRE !!!
	}
	
	// clic sur bouton de lancement du test des pools de thread
	private void btnClicTestPoolThread(ActionEvent e) {
		initAppli(e);		// initialisation de l'application
		controleur.dmdIHMLanceTestPool();
	}

	
	// clic sur bouton de lancement du test des semaphores
	private void btnClicTestSemaphore(ActionEvent e) {
		initAppli(e);		// initialisation de l'application

		controleur.dmdIHMLanceTestSem(Integer.parseInt(txtNbJetons.getText()), Integer.parseInt(this.txtNbrThreadsTestSem.getText()), Integer.parseInt(textFieldNbTestSem.getText()));
		}
	
	// clic sur bouton de RAZ de la zone d'affichage du test des semaphores
	private void btnClicRAZTestSemaphore(ActionEvent e) {
		this.textAreaTestSemaphore.setText("");
	}
	
	// clic sur bouton de lancement du test des MUTEX
	private void btnClicTestMutex(ActionEvent e) {
		initAppli(e);		// initialisation de l'application
		
		controleur.dmdIHMLanceTestMutex(Integer.parseInt(textFieldNbTestMutex.getText()), Integer.parseInt(txtNbrThreadTestMutex.getText()));
	}
	
	// clic sur bouton de RAZ de la zone d'affichage du test des semaphores
	private void btnClicRAZTestMutex(ActionEvent e) throws UnknownHostException, ClassNotFoundException, IOException  {
		this.textAreaTestMutex.setText("");
		
		initAppli(e);
		ClientSocket client = new ClientSocket(getAdresseIPConsoleDistante(), NUMERO_PORT_SERVEUR_TCP, this, new MsgToConsole(0, false, "message"));
	}
	
	
	//------------------------------------------------------------------------------------------------------------

	
	//+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
	/**
	 * ===================== methodes d'accès aux listes d'affichage et initialisation de l'IHM ===============================
	 */
	

	/**
	 * Remplir la zone d'affichage des threads presents avec la String recue en parametre
	 * 
	 * @param msg
	 */
	public void affichageEtatThreads(String msg) {
	
		textAreaAffichageEtatThread.setText(msg);
	}
	
	/**
	 * Remplir la zone d'affichage des threads presents avec liste de String recue en parametre
	 * 
	 * @param listeEtatThreads
	 */
	public void affichageEtatThreads(ArrayList<String> listeEtatThreads) {
		
		textAreaAffichageEtatThread.removeAll();
		textAreaAffichageEtatThread.setText("");
		
		for(String message : listeEtatThreads) {
			textAreaAffichageEtatThread.append(message + "\n");
		}
	}
	
	/**
	 * affichage d'un nouveau message simple dans la zone de console de l'IHM
	 * 
	 * @param msg
	 */
	public void affichageConsole(MsgToConsole msg) {
				
		if (msg.getNumConsoleDest() == NUM_CONSOLE_CONSOLE) {

			// on ajoute a la liste d'affichage (un widget "textArea") le message recu en parametre
			textAreaConsole.append(msg.getMsg());
			
			// gestion du bargraphe de progression du remplissage de la zone d'affichage
			lblEtatBuffer.setText(Long.toString(textAreaConsole.getLineCount()));
			progressBarConsole.setValue(textAreaConsole.getLineCount());
			if (progressBarConsole.getPercentComplete() > SEUIL_CHGT_COULEUR_PROGRESS_BAR_CONSOLE)
				progressBarConsole.setForeground(Color.RED);	// on passe le baregraphe en rouge

			// si on arrive au nbr max de messages stockes dans la textArea, on l'efface (pas de conso memoire inutile)
			if (textAreaConsole.getLineCount() > this.tailleConsole) {
				textAreaConsole.setText("");
				progressBarConsole.setForeground(Color.GREEN);
			}			
		}
		else if (msg.getNumConsoleDest() == NUM_CONSOLE_TEST_SEMAPHORE) {
			// on ajoute a la liste d'affichage (un widget "textArea") le message recu en parametre
			textAreaTestSemaphore.append(msg.getMsg());

			// si on arrive au nbr max de messages stockes dans la textArea, on l'efface (pas de conso memoire inutile)
			if (textAreaTestSemaphore.getLineCount() > this.tailleConsole)
				textAreaTestSemaphore.setText("");	
		}
		else if (msg.getNumConsoleDest() == NUM_CONSOLE_TEST_MUTEX) {
			// on ajoute a la liste d'affichage (un widget "textArea") le message recu en parametre
			textAreaTestMutex.append(msg.getMsg());
			System.out.println(msg.getMsg() + " destine a la console numero : " + msg.getNumConsoleDest());


			// si on arrive au nbr max de messages stockes dans la textArea, on l'efface (pas de conso memoire inutile)
			if (textAreaTestMutex.getLineCount() > this.tailleConsole)
				textAreaTestMutex.setText("");
		}
		else if (msg.getNumConsoleDest() == NUM_CONSOLE_TEST_POOL) {
			// on ajoute a la liste d'affichage (un widget "textArea") le message recu en parametre
			textAreaTestPool.append(msg.getMsg());

			// si on arrive au nbr max de messages stockes dans la textArea, on l'efface (pas de conso memoire inutile)
			if (textAreaTestPool.getLineCount() > this.tailleConsole)
				textAreaTestPool.setText("");
		}
/*		
		try {
			ClientSocket client = new ClientSocket(getAdresseIPConsoleDistante(), NUMERO_PORT_SERVEUR_TCP, this, msg);
		} catch (ClassNotFoundException | IOException e) {
			// TODO Bloc catch g�n�r� automatiquement
			e.printStackTrace();
		}
	*/	
		}
	
	
	/**
	 * remplir la zone d'affichage de la console avec une liste de messages recue en paramètre
	 * 
	 * @param messageConsole
	 */
	public void affichageConsole(ArrayList<String> messageConsole, int numProducteur, int numConsole, boolean ajouterNumMessage) {

		for(String message : messageConsole) {
			MsgToConsole msg = new MsgToConsole(numConsole, ajouterNumMessage, message);
			affichageConsole(msg);
//			affichageConsole(new MsgToConsole(numConsole, ajouteNumMessage, message));
		}
	}
	
	
	/**
	 * Methode utilisee pour afficher dans une scroll bar l'utilisation du buffer
	 * d'affichage utilise pour la console
	 */
	public void affichageRemplissageMQ_Console(int remplissageMQ) {
		
		this.lblEtatMQ.setText(Long.toString(remplissageMQ));

		progressBarMQ.setValue(remplissageMQ);
		
		if (progressBarMQ.getPercentComplete() > SEUIL_CHGT_COULEUR_PROGRESS_BAR_MQ_CONSOLE)
			progressBarMQ.setForeground(Color.RED);
		else
			progressBarMQ.setForeground(Color.GREEN);
	}
	
	/**
	 * pour initialiser à des valeurs par defaut des champs de saisie de l'IHM
	 */
	public void initIHM() {
		textFieldNbProducteur.setText(Integer.toString(DEFAULT_NB_THREAD_PROD)); 		// nbr de producteurs a creer
		textFieldNbConsommateur.setText(Integer.toString(DEFAULT_NB_THREAD_CONS)); 		// nbr de consommateurs a creer
		textFieldFreqProd.setText(Integer.toString(FREQ_PRODUCTION));					// frequence de production des producteurs
		textFieldMaxLigneConsole.setText(Integer.toString(MAX_MSG_CONSOLE));			// nombre max sauvegardes dans la console
		textFieldTailleQueueConsole.setText(Integer.toString(TAILLE_MSG_Q_CONSOLE));	// taille de la MQ du thread de gestion de la console
		textFieldAdresseIP.setText(ADR_IP_SERVEUR_TCP);

		progressBarConsole.setForeground(Color.GREEN);									// la progressBar est en gris au début
		progressBarMQ.setBackground(Color.WHITE);
		progressBarMQ.setForeground(Color.GREEN);
		textAreaTestMutex.setFont(new Font("Dialog", Font.PLAIN, 10));
		
		textAreaTestMutex.append("init. zone : textAreaTestMutex OK !\n");
		textAreaTestSemaphore.setFont(new Font("Dialog", Font.PLAIN, 10));
		textAreaTestSemaphore.append("init. zone : textAreaSemaphore OK !\n");
		textAreaConsole.setFont(new Font("Dialog", Font.PLAIN, 10));
		textAreaConsole.append("init. zone : textAreaConsole OK !\n");
		textAreaTestPool.append("init. zone : textAreaTestPool OK !\n");
		textAreaAffichageEtatThread.setFont(new Font("Dialog", Font.PLAIN, 10));
		textAreaAffichageEtatThread.append("init. zone : textAreaEtatThread OK !\n");
		}
	//-------------------------------------------------------------------------------------------------------------------------

	
	//+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
	/**
	 * ============================ methodes d'accès aux proprietes de cette classe =======================================
	 */
	
	/**
	 * obtenir le nombre de threads Producteurs a creer
	 */
	public int getNbThreadP() {
	
		return Integer.parseInt(textFieldNbProducteur.getText());
	}

	/**
	 * obtenir le nombre de threads Consommateurs a creer
	 */
	public int getNbThreadC() {
		
		return Integer.parseInt(textFieldNbConsommateur.getText());
	}

	public int getTailleBufferConsole() {
		return (this.tailleMqConsole);
	}
	
	public int getFreqProd() {
		return (freqProd);
	}
	
	public String getAdresseIPConsoleDistante() {
		return this.textFieldAdresseIP.getText();
	}
	//-------------------------------------------------------------------------------------------------------------------------
	


	
	
	
	
	/**======================================== IHM ===============================================
	 * Create the frame.
	 * @param sem 
	 */
	public IHM_Test_Thread(ControlerTestThread controleur, Mutex sem) {
		
		this.controleur = controleur;
		this.mutexSynchroIHM_Controler = sem;
		
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 1376, 999);
		
		contentPane = new JPanel();		// conteneur des objets graphiques
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);	// integration du contentPane dans le conteneur
		contentPane.setLayout(null);
		
		/**
		 * Ajout d'une zone de texte
		 */
		txtTest.setBounds(138, 246, 144, 23);
		contentPane.add(txtTest);
		
		
		/**
		 * ------------------------------------------------------------------------------
		 * Gestion des clics sur les différents boutons de l'IHM
		 */
		
		// ajout d'un bouton "Go" et d'une fonction sur le clic du bouton
		JButton btnGo = new JButton("Go");
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
		btnGo.setBounds(55, 246, 58, 23);
		contentPane.add(btnGo);
		

		// ajout d'un bouton "Creer Thread" et d'une fonction sur le clic du bouton
		JButton btnInitAppli = new JButton("Init Appli");
		btnInitAppli.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// action executee lors du clic
				try {
					btnInitAppli(e);
				} catch (InterruptedException e1) {
					// TODO Bloc catch généré automatiquement
					e1.printStackTrace();
				}
			}
		});
		btnInitAppli.setBounds(513, 367, 108, 23);
		contentPane.add(btnInitAppli);

		// ajout du bouton "Del" et d'une fonction sur le clic du bouton
		JButton btnDel = new JButton("STOP");
		btnDel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				btnClicStop(e);
			}
		});
		btnDel.setBounds(302, 246, 89, 23);

		// ajout d'un bouton "Test Semaphore" et d'une fonction sur le clic du bouton
		JButton btnTstSemaphore = new JButton("Test Semaphore");
		btnTstSemaphore.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
					btnClicTestSemaphore(e);
			}
		});

		// ajout du bouton "Test Mutex" et d'une fonction sur le clic du bouton
		btnTstSemaphore.setBounds(814, 222, 160, 23);
		contentPane.add(btnTstSemaphore);
		JButton btnTstMutex = new JButton("Test Mutex");	
		btnTstMutex.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				btnClicTestMutex(e);
			}
		});
		
		// ajout d'un bouton de lancement du test de creation d'un pool de threads
		JButton btnTestPoolThread = new JButton("Test pool de Threads");
		btnTestPoolThread.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				btnClicTestPoolThread(e);
			}
		});
		btnTestPoolThread.setBounds(454, 80, 188, 25);
		contentPane.add(btnTestPoolThread);
		

		// ajout d'un bouton de RAZ de la zone d'affichage des tests de semaphore
		JButton btnRazZoneTestSem = new JButton("RAZ");
		btnRazZoneTestSem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				btnClicRAZTestSemaphore(e);
			}
		});
		btnRazZoneTestSem.setBounds(842, 925, 86, 25);
		contentPane.add(btnRazZoneTestSem);
		
		// ajout d'un bouton de RAZ de la zone d'affichage des tests des MUTEX
		JButton btnRazZoneTestMutex = new JButton("RAZ");
		btnRazZoneTestMutex.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					btnClicRAZTestMutex(e);
				} catch (ClassNotFoundException | IOException e1) {
					// TODO Bloc catch généré automatiquement
					e1.printStackTrace();
				}
			}
		});
		btnRazZoneTestMutex.setBounds(1161, 925, 76, 25);
		contentPane.add(btnRazZoneTestMutex);

		
		
		//ajout du logo
		String ressource = getClass().getClassLoader().getResource(logo).getPath();
		lblNewLabel.setIcon(new ImageIcon(ressource));
		lblNewLabel.setBounds(666, -7, 117, 61);
		contentPane.add(lblNewLabel);
		lblZoneConsole.setBounds(23, 302, 355, 14);
		
		contentPane.add(lblZoneConsole); 		// description de la zone de texte
		
		textFieldNbProducteur.setBounds(292, 178, 86, 20);
		contentPane.add(textFieldNbProducteur);
		textFieldNbProducteur.setColumns(10);
		
		JLabel lblNewLabelProdcuteur = new JLabel("Nombre de producteurs");
		lblNewLabelProdcuteur.setBounds(23, 180, 188, 14);
		contentPane.add(lblNewLabelProdcuteur);
		
		
		textFieldNbConsommateur.setBounds(292, 204, 86, 20);
		contentPane.add(textFieldNbConsommateur);
		textFieldNbConsommateur.setColumns(10);


		JLabel lblNewLabelConsommateurs = new JLabel("Nombre de consommateurs");
		lblNewLabelConsommateurs.setBounds(23, 206, 218, 14);
		contentPane.add(lblNewLabelConsommateurs);

		contentPane.add(btnDel);
		
		// zone d'afichage (avec un scroll) des messages venant de l'application
		JScrollPane scrollPaneConsole = new JScrollPane();
		scrollPaneConsole.setBounds(27, 428, 374, 506);
		contentPane.add(scrollPaneConsole);
		
		scrollPaneConsole.setViewportView(textAreaConsole);
		
		JScrollPane scrollPaneEtatThread = new JScrollPane();
		scrollPaneEtatThread.setBounds(414, 428, 297, 506);
		contentPane.add(scrollPaneEtatThread);
		
		scrollPaneEtatThread.setViewportView(textAreaAffichageEtatThread);
		
		JLabel lblNewLabel_1 = new JLabel("Etat des threads  : true = en vie / false = mort");
		lblNewLabel_1.setFont(new Font("Dialog", Font.BOLD, 10));
		lblNewLabel_1.setBounds(420, 402, 272, 14);
		contentPane.add(lblNewLabel_1);
		
		JSeparator separator = new JSeparator();
		separator.setForeground(Color.BLUE);
		separator.setOrientation(SwingConstants.VERTICAL);
		separator.setBounds(723, 0, 14, 962);
		contentPane.add(separator);
		

		/*****************************/

		
		btnTstMutex.setBounds(1124, 222, 127, 23);
		contentPane.add(btnTstMutex);
		
		txtNbJetons = new JTextField();
		txtNbJetons.setText("2");
		txtNbJetons.setBounds(749, 87, 40, 20);
		contentPane.add(txtNbJetons);
		txtNbJetons.setColumns(10);
		
		JLabel lblNewLabel_2 = new JLabel("Nbr jetons dans le semaphore");
		lblNewLabel_2.setBounds(749, 66, 242, 14);
		contentPane.add(lblNewLabel_2);
		
		JLabel lblNewLabel_3 = new JLabel("TESTS DES SEMAPHORES ET MUTEX");
		lblNewLabel_3.setFont(new Font("Tahoma", Font.BOLD, 20));
		lblNewLabel_3.setBounds(794, 28, 457, 18);
		contentPane.add(lblNewLabel_3);
		
		JLabel lblNewLabel_3_1 = new JLabel("Test du multithreading Producteur / Consommateur");
		lblNewLabel_3_1.setFont(new Font("Tahoma", Font.BOLD, 20));
		lblNewLabel_3_1.setBounds(55, 15, 640, 31);
		contentPane.add(lblNewLabel_3_1);
		
	
		JLabel lblNewLabel_4 = new JLabel("Nbr threads concurrents");
		lblNewLabel_4.setBounds(749, 127, 194, 14);
		contentPane.add(lblNewLabel_4);
		
		txtNbrThreadsTestSem = new JTextField();
		txtNbrThreadsTestSem.setText("4");
		txtNbrThreadsTestSem.setBounds(749, 146, 40, 20);
		contentPane.add(txtNbrThreadsTestSem);
		txtNbrThreadsTestSem.setColumns(10);
		
		JScrollPane scrollPaneConsoleSemaphore = new JScrollPane();
		scrollPaneConsoleSemaphore.setBounds(749, 257, 293, 656);
		contentPane.add(scrollPaneConsoleSemaphore);
		
		scrollPaneConsoleSemaphore.setViewportView(textAreaTestSemaphore);

		JScrollPane scrollPaneConsoleMutex = new JScrollPane();
		scrollPaneConsoleMutex.setBounds(1066, 257, 272, 656);
		contentPane.add(scrollPaneConsoleMutex);
		
		scrollPaneConsoleMutex.setViewportView(textAreaTestMutex);
		progressBarConsole.setForeground(Color.GRAY);
		progressBarConsole.setBackground(Color.WHITE);
		progressBarConsole.setBounds(23, 354, 288, 14);
		contentPane.add(progressBarConsole);
		
		JLabel lblNewLabel_5 = new JLabel("Buffer console");
		lblNewLabel_5.setBounds(22, 328, 160, 14);
		contentPane.add(lblNewLabel_5);
		

		lblEtatBuffer.setBounds(207, 327, 46, 17);
		contentPane.add(lblEtatBuffer);
		
		JLabel lblNewLabel_6 = new JLabel("/");
		lblNewLabel_6.setBounds(239, 328, 27, 14);
		contentPane.add(lblNewLabel_6);
		
		lblMaxLigneConsole.setBounds(265, 328, 46, 14);
		contentPane.add(lblMaxLigneConsole);
		
		JLabel lblNewLabel_7 = new JLabel("Frequence de production (msec)");
		lblNewLabel_7.setBounds(23, 90, 251, 14);
		contentPane.add(lblNewLabel_7);
		
		textFieldFreqProd.setText("500");
		textFieldFreqProd.setBounds(292, 87, 86, 20);
		contentPane.add(textFieldFreqProd);
		textFieldFreqProd.setColumns(10);
		
		JLabel lblNewLabel_8 = new JLabel("Taille de la console (nbr de lignes)");
		lblNewLabel_8.setBounds(22, 122, 252, 14);
		contentPane.add(lblNewLabel_8);
		
		textFieldMaxLigneConsole.setText("100");
		textFieldMaxLigneConsole.setBounds(292, 123, 86, 20);
		contentPane.add(textFieldMaxLigneConsole);
		textFieldMaxLigneConsole.setColumns(10);
		
		JLabel lblTailleDeMessagequeueconsole = new JLabel("Taille de messageQueueConsole");
		lblTailleDeMessagequeueconsole.setBounds(23, 147, 249, 15);
		contentPane.add(lblTailleDeMessagequeueconsole);
		
		textFieldTailleQueueConsole.setBounds(292, 147, 86, 19);
		contentPane.add(textFieldTailleQueueConsole);
		textFieldTailleQueueConsole.setColumns(10);
		
		JSeparator separator_1 = new JSeparator();
		separator_1.setOrientation(SwingConstants.VERTICAL);
		separator_1.setBounds(1051, 82, 17, 852);
		contentPane.add(separator_1);
		
		progressBarMQ.setBounds(23, 404, 285, 14);
		contentPane.add(progressBarMQ);
		
		lblEtatMQ.setBounds(204, 380, 70, 15);
		contentPane.add(lblEtatMQ);
		
		JLabel lbl_80 = new JLabel("Remplissage de la MQ ");
		lbl_80.setBounds(23, 381, 177, 14);
		contentPane.add(lbl_80);
		
		JLabel lbl = new JLabel("/");
		lbl.setBounds(239, 380, 14, 15);
		contentPane.add(lbl);
		
		lblTailleMQConsole.setBounds(265, 380, 57, 15);
		contentPane.add(lblTailleMQConsole);
		
		JScrollPane scrollPaneTestPoolThread = new JScrollPane();
		scrollPaneTestPoolThread.setBounds(414, 121, 278, 227);
		contentPane.add(scrollPaneTestPoolThread);
		textAreaTestPool.setFont(new Font("Dialog", Font.PLAIN, 10));
		
		scrollPaneTestPoolThread.setViewportView(textAreaTestPool);
		
		JLabel lblNbrThreadsConcurrents = new JLabel("Nbr threads concurrents");
		lblNbrThreadsConcurrents.setBounds(1063, 85, 188, 15);
		contentPane.add(lblNbrThreadsConcurrents);
		
		txtNbrThreadTestMutex = new JTextField();
		txtNbrThreadTestMutex.setText("2");
		txtNbrThreadTestMutex.setBounds(1066, 102, 40, 19);
		contentPane.add(txtNbrThreadTestMutex);
		txtNbrThreadTestMutex.setColumns(10);
		
		JLabel lblNbrCyclesDacces = new JLabel("Nbr cycles d'acces");
		lblNbrCyclesDacces.setBounds(749, 180, 188, 15);
		contentPane.add(lblNbrCyclesDacces);
		
		JLabel lblNbrCyclesDacces_1 = new JLabel("Nbr cycles d'acces");
		lblNbrCyclesDacces_1.setBounds(1063, 148, 188, 15);
		contentPane.add(lblNbrCyclesDacces_1);
		
		textFieldNbTestMutex.setText("1");
		textFieldNbTestMutex.setBounds(1066, 175, 86, 19);
		contentPane.add(textFieldNbTestMutex);
		textFieldNbTestMutex.setColumns(10);
		
		textFieldNbTestSem.setText("1");
		textFieldNbTestSem.setBounds(941, 178, 86, 19);
		contentPane.add(textFieldNbTestSem);
		textFieldNbTestSem.setColumns(10);
		
		textFieldAdresseIP.setBounds(271, 56, 117, 19);
		contentPane.add(textFieldAdresseIP);
		textFieldAdresseIP.setColumns(10);
		
		JLabel lblAdresseIpDe = new JLabel("@ IP de la console distante");
		lblAdresseIpDe.setBounds(26, 58, 240, 15);
		contentPane.add(lblAdresseIpDe);
		
		
	
		/**
		 * initialisation des variables de l'IHM
		 */
		initIHM();

	}
}
