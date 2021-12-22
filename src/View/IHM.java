package View;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import controler.Controler;

import javax.swing.JButton;
import javax.swing.JTextPane;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.awt.event.ActionEvent;
import javax.swing.JLabel;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JScrollPane;
import javax.swing.JList;
import javax.swing.JTextField;
import javax.swing.ListModel;
import javax.swing.JTextArea;

public class IHM extends JFrame {

	/**
	 * d�claration des propri�t�s de la classe
	 */
	private static final long serialVersionUID = 1L;
	private final JPanel contentPane;
	private final JButton btnGo = new JButton("Go");
	private final JTextPane txtTest = new JTextPane();
	private final JLabel lblNewLabel = new JLabel("");
	private final String chemin = "image.jpg";
	private final JLabel lblZoneConsole = new JLabel("Affichage des messages venant de l'application");
	private final JButton btnDel = new JButton("Del");
	private JTextField txtNbProducteur;
	private JTextField txtNbConsommateur;
	private Controler controleur = null;
	
	private DefaultListModel contenuConsole = new DefaultListModel();
	private static int nbMsg = 0;


	private JList lstAffichageConsole;


	/**
	 * Launch the application.
	 */
/*	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					IHM frame = new IHM(); 		// cr�ation de l'IHM par appel du constructeur
					frame.setVisible(true);		// affichage de la fen�tre
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
*/
	
	/**
	 * fct execut�e lors du clic sur le bouton
	 * @param e
	 */
	private void btnGo_clic(ActionEvent e) {
		txtTest.setText("C'est parti !");
		controleur.dmdIHMGo(Long.parseLong(txtNbConsommateur.getText()), Long.parseLong(txtNbProducteur.getText()));
	}
	
	/**
	 * fct exécutée lors du clic sur le bouton "Del"
	 * @param e
	 */
	private void btnDel_clic(ActionEvent e) {
		// A FAIRE !!!
	}
	
	
	/**
	 * remplir la zone avec les messages dans la liste
	 * @param message
	 */
	public void affichageConsole(ArrayList<String> messageConsole) {
		//vider la liste
		contenuConsole.clear();
		
/*		for (int i=0; i < messageConsole.size(); i++) {
			contenuConsole.addElement(messageConsole);
*/		
		// boucle pour remplir la liste
		for(String message : messageConsole) {
			//String ligne;
			
			//ligne = message;
//			contenuConsole.addElement(ligne);
			contenuConsole.addElement(message);
			
			lstAffichageConsole.updateUI();
			lstAffichageConsole.repaint();

			
			//System.out.println("size = " + contenuConsole.getSize());
		}
		
		//lstAffichageConsole
	}
	
	/**
	 * pour initialiser à des valerus par défaut des champs de saisie de l'IHM
	 */
	private void initIHM() {
		txtNbProducteur.setText("1"); // on fixe le mini à 1
		txtNbConsommateur.setText("2"); // on fixe le mini à 2
	}
	
	
	/**======================================== IHM ===============================================
	 * Create the frame.
	 */
	public IHM(Controler controleur) {
		
		this.controleur = controleur;
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 1266, 583);
		
		contentPane = new JPanel();		// conteneur des objets graphiques
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);	// int�gration du contentPane dans le conteneur
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
				// action ex�cut�e lors du clic
				btnGo_clic(e);
			}
		});
		btnGo.setBounds(205, 103, 58, 23);
		contentPane.add(btnGo);
		
		/**
		 * ajout d'une image
		 */
		String ressource = getClass().getClassLoader().getResource(chemin).getPath();
		lblNewLabel.setIcon(new ImageIcon(ressource));
		lblNewLabel.setBounds(503, 30, 117, 61);
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
		txtNbProducteur.setBounds(195, 30, 86, 20);
		contentPane.add(txtNbProducteur);
		txtNbProducteur.setColumns(10);
		
		JLabel lblNewLabelProdcuteur = new JLabel("Nombre de producteurs");
		lblNewLabelProdcuteur.setBounds(27, 33, 188, 14);
		contentPane.add(lblNewLabelProdcuteur);
		
		
		
		txtNbConsommateur = new JTextField();
		txtNbConsommateur.setBounds(195, 61, 86, 20);
		contentPane.add(txtNbConsommateur);
		txtNbConsommateur.setColumns(10);


		JLabel lblNewLabelConsommateurs = new JLabel("Nombre de consommateurs");
		lblNewLabelConsommateurs.setBounds(27, 64, 188, 14);
		contentPane.add(lblNewLabelConsommateurs);

		/**
		 * initialisation des variables de l'IHM
		 */
		initIHM();
				
		// bouton "Del"
		contentPane.add(btnDel);
		
		// zone d'afichage (avec un scroll) des messages venant de l'application
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(23, 175, 643, 320);
		contentPane.add(scrollPane);
		
		lstAffichageConsole = new JList(contenuConsole);
//		JList lstAffichageConsole = new JList(contenuConsole);
//		scrollPane.add(lstAffichageConsole);
//		scrollPane.setVisible(true);;
		scrollPane.setViewportView(lstAffichageConsole);
		
		//		scrollPane.setColumnHeaderView(lstAffichageConsole);
	
		
	}
}
