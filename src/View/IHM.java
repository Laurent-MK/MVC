package View;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

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

public class IHM extends JFrame implements Constantes {

	/**
	 * proprietes de la classe
	 */

	// propietes utilisees pour les widgets
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
	private JList lstAffichageConsole;


	// proprietes utilisees pour la gestion de l'IHM
	private static boolean isClicOnBtn_GO = false;
	private static boolean isclicOnBtn_CreationThread = false;
	
	private Controler controleur = null;
	
	private DefaultListModel contenuLstAffichageConsole = new DefaultListModel();
	private DefaultListModel contenulistThread = new DefaultListModel();


	private Semaphore sem;


	
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
	
	

	/**
	 * Remplir la zone d'affichage des threads presents
	 */
	public void affichageThreads(String msg) {

		contenulistThread.clear();
		contenulistThread.addElement(msg);
	}
	
	public void affichageThreads(ArrayList<String> listeThreads) {
	
		contenulistThread.clear();
		
		for (String msg : listeThreads) {
			String ligne;
			ligne = msg;
			
			contenulistThread.addElement(ligne);
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

	

	/**
	 * affichage d'un nouveau message dans la zone de console de l'IHM
	 * @param msg
	 */
	public void affichageConsole(String msg) {
		contenuLstAffichageConsole.addElement(msg);
	}
	
	
	/**
	 * remplir la zone avec les messages dans la liste
	 * @param message
	 */
	public void affichageConsole(ArrayList<String> messageConsole) throws InterruptedException {

		sem.acquire(); System.out.println("PRENDRE");
		//Thread.sleep(100);
		
//		lstAffichageConsole.setVisible(false);
		
		//vider la liste
		contenuLstAffichageConsole.clear();
//		contenuConsole.removeAllElements();

		
		// boucle pour remplir la liste
		for(String message : messageConsole) {
			String ligne;
			
			ligne = message;
			contenuLstAffichageConsole.addElement(ligne);

//			lstAffichageConsole.setVisible(true);
			 
//			contenuConsole.addElement(message);
//			lstAffichageConsole.updateUI();
			lstAffichageConsole.repaint();
			
			//System.out.println("size = " + contenuConsole.getSize());
		}
		sem.release();
		System.out.println("RENDRE");
	}
	
	/**
	 * pour initialiser à des valeurs par defaut des champs de saisie de l'IHM
	 */
	private void initIHM() {
		txtNbProducteur.setText(Integer.toString(DEFAULT_NB_THREAD_PROD)); 		// on fixe le mini
		txtNbConsommateur.setText(Integer.toString(DEFAULT_NB_THREAD_CONS)); 	// on fixe le mini
	}
	
	
	/**======================================== IHM ===============================================
	 * Create the frame.
	 * @param sem 
	 */
	public IHM(Controler controleur, Semaphore sem) {
		
		this.controleur = controleur;
		this.sem = sem;
		
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 1228, 583);
		
		contentPane = new JPanel();		// conteneur des objets graphiques
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);	// integration du contentPane dans le conteneur
		contentPane.setLayout(null);
		
		/**
		 * Ajout d'une zone de texte
		 */
		txtTest.setBounds(304, 103, 144, 23);
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
		btnGo.setBounds(205, 103, 58, 23);
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
		btnCreerThread.setBounds(465, 49, 155, 23);
		contentPane.add(btnCreerThread);

		
		
		
		/**
		 * ajout d'une image
		 */
		String ressource = getClass().getClassLoader().getResource(chemin).getPath();
		lblNewLabel.setIcon(new ImageIcon(ressource));
		lblNewLabel.setBounds(967, 11, 117, 61);
		contentPane.add(lblNewLabel);
		lblZoneConsole.setBounds(23, 150, 355, 14);
		
		contentPane.add(lblZoneConsole); 		// description de la zone de texte
		btnDel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				btnDel_clic(e);
			}
		});
		btnDel.setBounds(304, 510, 89, 23);

		
		txtNbProducteur = new JTextField();
		txtNbProducteur.setBounds(253, 31, 86, 20);
		contentPane.add(txtNbProducteur);
		txtNbProducteur.setColumns(10);
		
		JLabel lblNewLabelProdcuteur = new JLabel("Nombre de producteurs");
		lblNewLabelProdcuteur.setBounds(27, 33, 188, 14);
		contentPane.add(lblNewLabelProdcuteur);
		
		
		
		txtNbConsommateur = new JTextField();
		txtNbConsommateur.setBounds(253, 63, 86, 20);
		contentPane.add(txtNbConsommateur);
		txtNbConsommateur.setColumns(10);


		JLabel lblNewLabelConsommateurs = new JLabel("Nombre de consommateurs");
		lblNewLabelConsommateurs.setBounds(27, 64, 218, 14);
		contentPane.add(lblNewLabelConsommateurs);

		/**
		 * initialisation des variables de l'IHM
		 */
		initIHM();
				
		// bouton "Del"
		contentPane.add(btnDel);
		
		// zone d'afichage (avec un scroll) des messages venant de l'application
		JScrollPane scrollPaneConsole = new JScrollPane();
		scrollPaneConsole.setBounds(23, 175, 643, 320);
		contentPane.add(scrollPaneConsole);
		
		lstAffichageConsole = new JList(contenuLstAffichageConsole);
//		JList lstAffichageConsole = new JList(contenuConsole);
//		scrollPane.add(lstAffichageConsole);
//		scrollPane.setVisible(true);;
		scrollPaneConsole.setViewportView(lstAffichageConsole);
		
		JScrollPane scrollPaneThread = new JScrollPane();
		scrollPaneThread.setBounds(695, 175, 507, 320);
		contentPane.add(scrollPaneThread);
		
		JList listThread = new JList(contenulistThread);
		scrollPaneThread.setViewportView(listThread);
		
		JLabel lblNewLabel_1 = new JLabel("Etat des threads  : true = en vie / false = mort");
		lblNewLabel_1.setBounds(695, 150, 479, 14);
		contentPane.add(lblNewLabel_1);
		
		
		//		scrollPane.setColumnHeaderView(lstAffichageConsole);
	
		
	}
}
