package View;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import UtilitairesMK.Mutex;
import controler.Controler;
import model.Constantes;
import model.TestSemaphore;

import javax.swing.JButton;
import javax.swing.JTextPane;
import java.awt.event.ActionListener;
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

public class IHM extends JFrame implements Constantes {

	/**
	 * proprietes de la classe
	 */

	// proprietes utilisees pour la création de l'IHM (bouton, labels, zone d'affichage ...
	private static final long serialVersionUID = 1L;
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
	
	private final JTextField textFieldNbProducteur = new JTextField();
	private final JTextField textFieldNbConsommateur = new JTextField();
	private final JTextField textFieldFreqProd = new JTextField();
	private final JTextField textFieldMaxLigneConsole = new JTextField();
	private final JTextField textFieldTailleQueueConsole = new JTextField();

	private final JProgressBar progressBarConsole = new JProgressBar(0, MAX_MSG_CONSOLE);
	private final JProgressBar progressBarMQ = new JProgressBar(0, TAILLE_MSG_Q_CONSOLE);



	// proprietes utilisees pour la gestion de l'IHM
	private boolean isClicOnBtn_GO = false;
	private boolean isclicOnBtn_CreationThread = false;
	private boolean isClicOnBtnCreerSemaphore = false;
	private boolean isClicOnBtnCreerMutex = false;
	private Mutex mutexSynchroIHM_Controler;
	private JTextField txtNbJetons;
	private JTextField txtNbrThreads;
	private Controler controleur;
	private int freqProd;
	private int tailleConsole;
	private int tailleMqConsole;
	
	
	
	//+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
	/**
	 * ============================ methodes appelees sur clic sur les boutons de l'IHM =======================================
	 */
	
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
			
			tailleConsole = Integer.parseInt(textFieldMaxLigneConsole.getText());
			freqProd = Integer.parseInt(textFieldFreqProd.getText());
			this.tailleMqConsole = Integer.parseInt(textFieldTailleQueueConsole.getText());
			
			lblMaxLigneConsole.setText(Long.toString(tailleConsole));
			progressBarConsole.setMaximum(tailleConsole);
			
			lblTailleMQConsole.setText(Long.toString(tailleMqConsole));
			progressBarMQ.setMaximum(tailleMqConsole);
			
			mutexSynchroIHM_Controler.mutexRelease(); 	// liberation du mutex de synchro de l'IHM et du controleur
			
			Thread.yield();
			Thread.sleep(10);
		
			mutexSynchroIHM_Controler.mutexGet();
			
			
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
		if (isClicOnBtnCreerSemaphore) {
			JOptionPane.showMessageDialog(null, "Semaphores deja crees !");		
			textAreaTestSemaphore.append("creation d'un semaphore\n");
		}
		else {
			isClicOnBtnCreerSemaphore = true;
			textAreaTestSemaphore.append("creation d'un semaphore\n");
		}
	}
	
	private void btnClicCreerMutex(ActionEvent e) {
		if (isClicOnBtnCreerMutex) {
			JOptionPane.showMessageDialog(null, "Semaphores deja crees !");		
			textAreaTestMutex.append("creation d'un MUTEX\n");
		}
		else {
			isClicOnBtnCreerMutex = true;
			textAreaTestMutex.append("creation d'un MUTEX\n");
		}
	}

	private void btnClicTestSemaphore(ActionEvent e) {
		try {
			if (! isclicOnBtn_CreationThread)
				btnCreerThread_clic(e);
			
			TestSemaphore tst = new TestSemaphore(this.controleur);
			tst.Go();
		} catch (InterruptedException e1) {
			// TODO Bloc catch généré automatiquement
			e1.printStackTrace();
		}
		
	}
	
	private void btnClicTestMutex(ActionEvent e) {
		
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
	public void affichageConsole(String msg) {
		textAreaConsole.append(msg);
		
		lblEtatBuffer.setText(Long.toString(textAreaConsole.getLineCount()));
		progressBarConsole.setValue(textAreaConsole.getLineCount());
		if (progressBarConsole.getPercentComplete() > SEUIL_CHGT_COULEUR_PROGRESS_BAR_CONSOLE)
			progressBarConsole.setForeground(Color.RED);

		
		if (textAreaConsole.getLineCount() > this.tailleConsole) {
			textAreaConsole.setText("");
			progressBarConsole.setForeground(Color.GREEN);
		}
	}
	
	
	/**
	 * remplir la zone d'affichage de la console avec une liste de messages recue en paramètre
	 * 
	 * @param messageConsole
	 */
	public void affichageConsole(ArrayList<String> messageConsole) {

		for(String message : messageConsole) {
			affichageConsole(message);
		}
	}
	
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
	private void initIHM() {
		textFieldNbProducteur.setText(Integer.toString(DEFAULT_NB_THREAD_PROD)); 		// nbr de producteurs a creer
		textFieldNbConsommateur.setText(Integer.toString(DEFAULT_NB_THREAD_CONS)); 		// nbr de consommateurs a creer
		textFieldFreqProd.setText(Integer.toString(FREQ_PRODUCTION));					// frequence de production des producteurs
		textFieldMaxLigneConsole.setText(Integer.toString(MAX_MSG_CONSOLE));			// nombre max sauvegardes dans la console
		textFieldTailleQueueConsole.setText(Integer.toString(TAILLE_MSG_Q_CONSOLE));	// taille de la MQ du thread de gestion de la console
		progressBarConsole.setForeground(Color.GREEN);									// la progressBar est en gris au début
		progressBarMQ.setBackground(Color.WHITE);
		progressBarMQ.setForeground(Color.GREEN);
		
		textAreaTestMutex.append("init. zone : textAreaTestMutex OK !\n");
		textAreaTestSemaphore.append("init. zone : textAreaSemaphore OK !\n");
		textAreaConsole.append("init. zone : textAreaConsole OK !\n");
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
	//-------------------------------------------------------------------------------------------------------------------------
	


	
	
	
	
	/**======================================== IHM ===============================================
	 * Create the frame.
	 * @param sem 
	 */
	public IHM(Controler controleur, Mutex sem) {
		
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
		txtTest.setBounds(178, 246, 144, 23);
		contentPane.add(txtTest);
		
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
		btnGo.setBounds(108, 246, 58, 23);
		contentPane.add(btnGo);
		

		// ajout d'un bouton "Creer Thread" et d'une fonction sur le clic du bouton
		JButton btnCreerThread = new JButton("Creer Threads");
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
		btnCreerThread.setBounds(468, 345, 155, 23);
		contentPane.add(btnCreerThread);

		// ajout du bouton "Del" et d'une fonction sur le clic du bouton
		JButton btnDel = new JButton("Del");
		btnDel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				btnDel_clic(e);
			}
		});
		btnDel.setBounds(414, 118, 89, 23);

		// ajout du bouton "Creer Semaphore" et d'une fonction sur le clic du bouton
		JButton btnCreerSemaphore = new JButton("Creer Semaphore");
		btnCreerSemaphore.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				btnClicCreerSemaphore(e);
			}
		});
		btnCreerSemaphore.setBounds(749, 178, 160, 23);
		contentPane.add(btnCreerSemaphore);
		
		// ajout du bouton "Creer MUTEX" et d'une fonction sur le clic du bouton
		JButton btnCreerMutex = new JButton("Creer MUTEX");
		btnCreerMutex.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				btnClicCreerMutex(e);
			}
		});
		btnCreerMutex.setBounds(1141, 178, 127, 23);
		contentPane.add(btnCreerMutex);

		// ajout d'un bouton "Test Semaphore" et d'une fonction sur le clic du bouton
		JButton btnTstSemaphore = new JButton("Test Semaphore");
		btnTstSemaphore.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				btnClicTestSemaphore(e);
			}
		});

		// ajout du bouton "Test Mutex" et d'une fonction sur le clic du bouton
		btnTstSemaphore.setBounds(749, 222, 160, 23);
		contentPane.add(btnTstSemaphore);
		JButton btnTstMutex = new JButton("Test Mutex");	
		btnTstMutex.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				btnClicTestMutex(e);
			}
		});
		
		
		//ajout du logo
		String ressource = getClass().getClassLoader().getResource(logo).getPath();
		lblNewLabel.setIcon(new ImageIcon(ressource));
		lblNewLabel.setBounds(1221, 80, 117, 61);
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
		scrollPaneConsole.setBounds(27, 445, 374, 463);
		contentPane.add(scrollPaneConsole);
		
		scrollPaneConsole.setViewportView(textAreaConsole);
		
		JScrollPane scrollPaneEtatThread = new JScrollPane();
		scrollPaneEtatThread.setBounds(414, 445, 297, 463);
		contentPane.add(scrollPaneEtatThread);
		
		scrollPaneEtatThread.setViewportView(textAreaAffichageEtatThread);
		
		JLabel lblNewLabel_1 = new JLabel("Etat des threads  : true = en vie / false = mort");
		lblNewLabel_1.setFont(new Font("Dialog", Font.BOLD, 10));
		lblNewLabel_1.setBounds(426, 391, 272, 14);
		contentPane.add(lblNewLabel_1);
		
		JSeparator separator = new JSeparator();
		separator.setForeground(Color.BLUE);
		separator.setOrientation(SwingConstants.VERTICAL);
		separator.setBounds(723, 0, 14, 962);
		contentPane.add(separator);
		

		/*****************************/

		
		btnTstMutex.setBounds(1141, 222, 127, 23);
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
		lblNewLabel_3.setBounds(794, 28, 457, 18);
		contentPane.add(lblNewLabel_3);
		
		JLabel lblNewLabel_3_1 = new JLabel("Test du multithreading Producteur / Consommateur");
		lblNewLabel_3_1.setFont(new Font("Tahoma", Font.BOLD, 20));
		lblNewLabel_3_1.setBounds(44, 28, 640, 31);
		contentPane.add(lblNewLabel_3_1);
		
	
		JLabel lblNewLabel_4 = new JLabel("Nbr threads concurents");
		lblNewLabel_4.setBounds(749, 122, 194, 14);
		contentPane.add(lblNewLabel_4);
		
		txtNbrThreads = new JTextField();
		txtNbrThreads.setText("2");
		txtNbrThreads.setBounds(749, 137, 40, 20);
		contentPane.add(txtNbrThreads);
		txtNbrThreads.setColumns(10);
		
		JScrollPane scrollPaneConsoleSemaphore = new JScrollPane();
		scrollPaneConsoleSemaphore.setBounds(749, 257, 293, 619);
		contentPane.add(scrollPaneConsoleSemaphore);
		
		scrollPaneConsoleSemaphore.setViewportView(textAreaTestSemaphore);

		JScrollPane scrollPaneConsoleMutex = new JScrollPane();
		scrollPaneConsoleMutex.setBounds(1066, 257, 272, 605);
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
		separator_1.setBounds(1051, 82, 17, 813);
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
	
		/**
		 * initialisation des variables de l'IHM
		 */
		initIHM();

	}
}
