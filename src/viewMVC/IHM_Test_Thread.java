package viewMVC;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import controlerMVC.Controler;
import controlerMVC.ControlerTestThread;
import modelMVC.Constantes;
import utilitairesMK_MVC.MsgDeControle;
import utilitairesMK_MVC.MsgToConsole;
import utilitairesMK_MVC.MsgTrfObjet;
import utilitairesMK_MVC.Mutex;
import utilitairesMK_MVC.SocketClientTCP;
import utilitairesMK_MVC.TypeMsgCS;

import javax.swing.JButton;
import javax.swing.JTextPane;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.Serializable;
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
import java.awt.Dialog.ModalExclusionType;
import java.awt.Font;
import javax.swing.JProgressBar;
import javax.swing.JCheckBox;


/**
 * META KONSULTING
 * 
 * Classe de gestion de l'IHM
 * 
 * @author balou
 *
 */

public class IHM_Test_Thread extends JFrame implements Constantes, IHM, Serializable {

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
	
	public final JCheckBox chckbxConnexion = new JCheckBox("");

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
	private boolean isConnexionPermanente = false;
	
  	private boolean VERBOSE_LOCAL = VERBOSE_ON & false;

	

	public ControlerTestThread getControleur() {
		return controleur;
	}

	
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
	
	/**
	 *  clic sur bouton de lancement du test des pools de thread
	 * @param e
	 */
	private void btnClicTestPoolThread(ActionEvent e) {
		initAppli(e);		// initialisation de l'application
		controleur.dmdIHMLanceTestPool();
	}

	
	/**
	 *  clic sur bouton de lancement du test des semaphores
	 * @param e
	 */
	private void btnClicTestSemaphore(ActionEvent e) {
		initAppli(e);		// initialisation de l'application

		controleur.dmdIHMLanceTestSem(Integer.parseInt(txtNbJetons.getText()), Integer.parseInt(this.txtNbrThreadsTestSem.getText()), Integer.parseInt(textFieldNbTestSem.getText()));
		}
	
	/**
	 *  clic sur bouton de RAZ de la zone d'affichage du test des semaphores
	 * @param e
	 */
	private void btnClicRAZTestSemaphore(ActionEvent e) {
		this.textAreaTestSemaphore.setText("");
	}
	
	/**
	 *  clic sur bouton de lancement du test des MUTEX
	 * @param e
	 */
	private void btnClicTestMutex(ActionEvent e) {
		initAppli(e);		// initialisation de l'application
		
		controleur.dmdIHMLanceTestMutex(Integer.parseInt(textFieldNbTestMutex.getText()), Integer.parseInt(txtNbrThreadTestMutex.getText()));
	}
	
	/**
	 *  clic sur bouton de RAZ de la zone d'affichage du test des semaphores
	 * @param e
	 * @throws UnknownHostException
	 * @throws ClassNotFoundException
	 * @throws IOException
	 */
	private void btnClicRAZTestMutex(ActionEvent e) throws UnknownHostException, ClassNotFoundException, IOException  {
		this.textAreaTestMutex.setText("");
		
		initAppli(e);
		
		/**
		 * test de la fonction de deport de la console vers un PC distant
		 */
		MsgToConsole msgConsole = new MsgToConsole(0, false, "TYPE_THREAD_ENVOI_1_MSG - message venant du client");
		controleur.dmdIHMEnvoiVersServeurTCP(getAdresseIPConsoleDistante(), NUMERO_PORT_SERVEUR_TCP, TYPE_THREAD_ENVOI_1_MSG, msgConsole);

	}
	
	/**
	 *  clic sur le bouton de connexion avec le serveur de socket TCP
	 * @param e
	 */
	private void btnClicConnexion(ActionEvent e) {
		if (chckbxConnexion.isSelected()) {
			// la connexion doit etre permanente entre le client et le serveur
			isConnexionPermanente = true;
			
			MsgToConsole msgConsole = new MsgToConsole(NUM_CONSOLE_CONSOLE_DIST, false, "TYPE_THREAD_ENVOI_1_MSG - message venant du client");
			controleur.dmdIHMEnvoiVersServeurTCP(getAdresseIPConsoleDistante(), NUMERO_PORT_SERVEUR_TCP, TYPE_THREAD_ENVOI_1_MSG, msgConsole);
			
			MsgDeControle msgControle = new MsgDeControle(TypeMsgCS.MSG_TEST_LINK, NUM_MSG_NOT_USED, "TYPE_THREAD_ENVOI_NO_THREAD - Message de test", null);
			controleur.dmdIHMEnvoiVersServeurTCP(getAdresseIPConsoleDistante(), NUMERO_PORT_SERVEUR_TCP, TYPE_THREAD_ENVOI_NO_THREAD, msgControle);
		}
		else {
			/*
			 *  la connexion ne doit exister que pour l'envoi du message entre le client et le serveur.
			 dans ce cas, le clic sur le bouton de connexion lance un test en envoyant un message de
			* controle vers le serveur distant
			*/
			isConnexionPermanente = false;
			
			MsgDeControle msgControle = new MsgDeControle(TypeMsgCS.MSG_TEST_LINK, NUM_MSG_NOT_USED, "TYPE_THREAD_ENVOI_NO_THREAD - Message de test",
												new MsgToConsole(NUM_CONSOLE_CONSOLE_DIST, false, "msg de test niveau 2"));
			controleur.dmdIHMEnvoiVersServeurTCP(getAdresseIPConsoleDistante(), NUMERO_PORT_SERVEUR_TCP, TYPE_THREAD_ENVOI_NO_THREAD, msgControle);

			MsgToConsole msgConsole = new MsgToConsole(NUM_CONSOLE_CONSOLE_DIST, false, "TYPE_THREAD_ENVOI_NO_THREAD - message venant du client");
			controleur.dmdIHMEnvoiVersServeurTCP(getAdresseIPConsoleDistante(), NUMERO_PORT_SERVEUR_TCP, TYPE_THREAD_ENVOI_NO_THREAD, msgConsole);

			msgConsole = new MsgToConsole(NUM_CONSOLE_CONSOLE_DIST, false, "TYPE_THREAD_ENVOI_1_MSG - deuxieme message venant du client et avant fin de comm");
			controleur.dmdIHMEnvoiVersServeurTCP(getAdresseIPConsoleDistante(), NUMERO_PORT_SERVEUR_TCP, TYPE_THREAD_ENVOI_1_MSG, msgConsole);
			
			MsgTrfObjet objDivers = new MsgTrfObjet(NUM_MSG_NOT_USED, new String("ok"));
			controleur.dmdIHMEnvoiVersServeurTCP(getAdresseIPConsoleDistante(), NUMERO_PORT_SERVEUR_TCP, TYPE_THREAD_ENVOI_NO_THREAD, objDivers);
		
			
			IHM_SERIALISABLE ihm = new IHM_SERIALISABLE();
			ihm.setLocation(500, 500);
			ihm.setVisible(true);
			ihm.setModalExclusionType(ModalExclusionType.APPLICATION_EXCLUDE);
			this.setModalExclusionType(ModalExclusionType.APPLICATION_EXCLUDE);
			JOptionPane.showMessageDialog(null, "appuyer sur le bouton pour lancer le transfert\nde la fenetre et de son contenu");
			
			ihm.setVisible(false);

			controleur.dmdIHMEnvoiVersServeurTCP(getAdresseIPConsoleDistante(), NUMERO_PORT_SERVEUR_TCP, TYPE_THREAD_ENVOI_N_MSG, ihm);

//			controleur.dmdIHMEnvoiVersServeurTCP(getAdresseIPConsoleDistante(), NUMERO_PORT_SERVEUR_TCP, TYPE_THREAD_ENVOI_1_MSG, ihm);

/*
		    BlockingQueue<MessageMK> msgQ = new ArrayBlockingQueue<MessageMK>(TAILLE_MESSAGE_Q_PC);
			ParametrageClientTCP paramClient = new ParametrageClientTCP("client TCP", 0, 5, msgQ, getAdresseIPConsoleDistante(), NUMERO_PORT_SERVEUR_TCP, TYPE_THREAD_ENVOI_1_MSG);
			
			// creation du message a envoyer : message de test de la liaison
			MsgDeControle msgControle = new MsgDeControle(TYPE_MSG_TEST_LINK, NUM_MSG_NOT_USED, "Message de test", null);
			// creation de la socket client
			ClientSocket client = new ClientSocket(paramClient);
			
			client.sendMsgUnique(msgControle);

			if (VERBOSE_LOCAL)
				System.out.println("Envoi d'un message -TYPE_MSG_TEST_LINK- realise par un seul appel de fonction");
*/		}
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

		switch (msg.getNumConsoleDest()) {
		
			case NUM_CONSOLE_CONSOLE :
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
				break;

			case NUM_CONSOLE_TEST_SEMAPHORE :
					// on ajoute a la liste d'affichage (un widget "textArea") le message recu en parametre
					textAreaTestSemaphore.append(msg.getMsg());
	
					// si on arrive au nbr max de messages stockes dans la textArea, on l'efface (pas de conso memoire inutile)
					if (textAreaTestSemaphore.getLineCount() > this.tailleConsole)
						textAreaTestSemaphore.setText("");
				break;

			case NUM_CONSOLE_TEST_MUTEX :
					// on ajoute a la liste d'affichage (un widget "textArea") le message recu en parametre
					textAreaTestMutex.append(msg.getMsg());
					if (VERBOSE_LOCAL)
						System.out.println(msg.getMsg() + " destine a la console numero : " + msg.getNumConsoleDest());
	
					// si on arrive au nbr max de messages stockes dans la textArea, on l'efface (pas de conso memoire inutile)
					if (textAreaTestMutex.getLineCount() > this.tailleConsole)
						textAreaTestMutex.setText("");
				break;

			case NUM_CONSOLE_TEST_POOL :
					// on ajoute a la liste d'affichage (un widget "textArea") le message recu en parametre
					textAreaTestPool.append(msg.getMsg());
	
					// si on arrive au nbr max de messages stockes dans la textArea, on l'efface (pas de conso memoire inutile)
					if (textAreaTestPool.getLineCount() > this.tailleConsole)
						textAreaTestPool.setText("");
				break;
		
			default :
				// a developper
				break;
		}
	}
	
	
	/**
	 * remplir la zone d'affichage de la console avec une liste de messages recue en parametre
	 * 
	 * @param messageConsole
	 */
	public void affichageConsole(ArrayList<String> messageConsole, int numProducteur, int numConsole, boolean ajouterNumMessage) {

		for(String message : messageConsole) {
			// on affiche chacun des message contenu dans la ArrayList
			affichageConsole(new MsgToConsole(numConsole, ajouterNumMessage, message));
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
	 * pour initialiser a des valeurs par defaut des champs de saisie de l'IHM
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
	 * ============================ methodes d'acces aux proprietes de cette classe =======================================
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
	 * Creation de la fenetre
	 * 
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
		txtTest.setBounds(124, 281, 144, 23);
		contentPane.add(txtTest);
		
		
		/**
		 * ------------------------------------------------------------------------------
		 * Gestion des clics sur les differents boutons de l'IHM
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
		btnGo.setBounds(54, 281, 58, 23);
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
		btnInitAppli.setBounds(516, 380, 108, 23);
		contentPane.add(btnInitAppli);

		// ajout du bouton "Del" et d'une fonction sur le clic du bouton
		JButton btnDel = new JButton("STOP");
		btnDel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				btnClicStop(e);
			}
		});
		btnDel.setBounds(291, 281, 89, 23);

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
		btnTestPoolThread.setBounds(461, 104, 188, 25);
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

		// ajout du bouton indiquant si la connexion avec le serveur de console distante
		// doit etre permanente ou non
		JButton btnConnexion = new JButton("Connexion");
		btnConnexion.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				btnClicConnexion(e);
			}
		});

		
		//ajout du logo
		String ressource = getClass().getClassLoader().getResource(logo).getPath();
		lblNewLabel.setIcon(new ImageIcon(ressource));
		lblNewLabel.setBounds(666, -7, 117, 61);
		contentPane.add(lblNewLabel);
		lblZoneConsole.setBounds(25, 316, 355, 14);
		
		contentPane.add(lblZoneConsole); 		// description de la zone de texte
		
		textFieldNbProducteur.setBounds(295, 223, 86, 20);
		contentPane.add(textFieldNbProducteur);
		textFieldNbProducteur.setColumns(10);
		
		JLabel lblNewLabelProdcuteur = new JLabel("Nombre de producteurs");
		lblNewLabelProdcuteur.setBounds(26, 225, 188, 14);
		contentPane.add(lblNewLabelProdcuteur);
		
		
		textFieldNbConsommateur.setBounds(295, 249, 86, 20);
		contentPane.add(textFieldNbConsommateur);
		textFieldNbConsommateur.setColumns(10);


		JLabel lblNewLabelConsommateurs = new JLabel("Nombre de consommateurs");
		lblNewLabelConsommateurs.setBounds(26, 251, 218, 14);
		contentPane.add(lblNewLabelConsommateurs);

		contentPane.add(btnDel);
		
		// zone d'afichage (avec un scroll) des messages venant de l'application
		JScrollPane scrollPaneConsole = new JScrollPane();
		scrollPaneConsole.setBounds(23, 444, 374, 506);
		contentPane.add(scrollPaneConsole);
		
		scrollPaneConsole.setViewportView(textAreaConsole);
		
		JScrollPane scrollPaneEtatThread = new JScrollPane();
		scrollPaneEtatThread.setBounds(414, 444, 297, 506);
		contentPane.add(scrollPaneEtatThread);
		
		scrollPaneEtatThread.setViewportView(textAreaAffichageEtatThread);
		
		JLabel lblNewLabel_1 = new JLabel("Etat des threads  : true = en vie / false = mort");
		lblNewLabel_1.setFont(new Font("Dialog", Font.BOLD, 10));
		lblNewLabel_1.setBounds(423, 415, 272, 14);
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
		progressBarConsole.setBounds(25, 368, 288, 14);
		contentPane.add(progressBarConsole);
		
		JLabel lblNewLabel_5 = new JLabel("Buffer console");
		lblNewLabel_5.setBounds(24, 342, 160, 14);
		contentPane.add(lblNewLabel_5);
		

		lblEtatBuffer.setBounds(209, 341, 34, 17);
		contentPane.add(lblEtatBuffer);
		
		JLabel lblNewLabel_6 = new JLabel("/");
		lblNewLabel_6.setBounds(241, 342, 27, 14);
		contentPane.add(lblNewLabel_6);
		
		lblMaxLigneConsole.setBounds(267, 342, 46, 14);
		contentPane.add(lblMaxLigneConsole);
		
		JLabel lblNewLabel_7 = new JLabel("Frequence de production (msec)");
		lblNewLabel_7.setBounds(26, 135, 251, 14);
		contentPane.add(lblNewLabel_7);
		
		textFieldFreqProd.setText("500");
		textFieldFreqProd.setBounds(295, 132, 86, 20);
		contentPane.add(textFieldFreqProd);
		textFieldFreqProd.setColumns(10);
		
		JLabel lblNewLabel_8 = new JLabel("Taille de la console (nbr de lignes)");
		lblNewLabel_8.setBounds(25, 167, 252, 14);
		contentPane.add(lblNewLabel_8);
		
		textFieldMaxLigneConsole.setText("100");
		textFieldMaxLigneConsole.setBounds(295, 168, 86, 20);
		contentPane.add(textFieldMaxLigneConsole);
		textFieldMaxLigneConsole.setColumns(10);
		
		JLabel lblTailleDeMessagequeueconsole = new JLabel("Taille de messageQueueConsole");
		lblTailleDeMessagequeueconsole.setBounds(26, 192, 249, 15);
		contentPane.add(lblTailleDeMessagequeueconsole);
		
		textFieldTailleQueueConsole.setBounds(295, 192, 86, 19);
		contentPane.add(textFieldTailleQueueConsole);
		textFieldTailleQueueConsole.setColumns(10);
		
		JSeparator separator_1 = new JSeparator();
		separator_1.setOrientation(SwingConstants.VERTICAL);
		separator_1.setBounds(1051, 82, 17, 852);
		contentPane.add(separator_1);
		
		progressBarMQ.setBounds(25, 418, 285, 14);
		contentPane.add(progressBarMQ);
		
		lblEtatMQ.setBounds(206, 394, 27, 15);
		contentPane.add(lblEtatMQ);
		
		JLabel lbl_80 = new JLabel("Remplissage de la MQ ");
		lbl_80.setBounds(25, 395, 177, 14);
		contentPane.add(lbl_80);
		
		JLabel lbl = new JLabel("/");
		lbl.setBounds(241, 394, 14, 15);
		contentPane.add(lbl);
		
		lblTailleMQConsole.setBounds(267, 394, 57, 15);
		contentPane.add(lblTailleMQConsole);
		
		JScrollPane scrollPaneTestPoolThread = new JScrollPane();
		scrollPaneTestPoolThread.setBounds(417, 141, 278, 227);
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
		
		textFieldAdresseIP.setBounds(241, 64, 97, 19);
		contentPane.add(textFieldAdresseIP);
		textFieldAdresseIP.setColumns(10);
		
		JLabel lblAdresseIpDe = new JLabel("@ IP de la console distante");
		lblAdresseIpDe.setBounds(25, 66, 240, 15);
		contentPane.add(lblAdresseIpDe);
		
		chckbxConnexion.setBounds(414, 66, 70, 14);
		contentPane.add(chckbxConnexion);
		
		btnConnexion.setBounds(506, 55, 130, 25);
		contentPane.add(btnConnexion);
		
		JLabel lblConnexionPermanente = new JLabel("Connexion permanente");
		lblConnexionPermanente.setFont(new Font("Dialog", Font.BOLD, 10));
		lblConnexionPermanente.setBounds(363, 43, 152, 15);
		contentPane.add(lblConnexionPermanente);
		
	
		/**
		 * initialisation des variables de l'IHM
		 */
		initIHM();
	}

	public boolean getIsConnexionPermanente() {
		return isConnexionPermanente;
	}


	@Override
	public Controler getControler() {
		// TODO Stub de la méthode généré automatiquement
		return null;
	}
}
