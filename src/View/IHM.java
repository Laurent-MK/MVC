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
import java.awt.event.ActionEvent;
import javax.swing.JLabel;
import javax.swing.ImageIcon;
import javax.swing.JScrollPane;
import javax.swing.JList;
import javax.swing.JTextField;

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
	private Controler Controleur = null;
	

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
	private void btnTest_clic(ActionEvent e) {
		txtTest.setText("Ca marche !!!");
	}
	
	
	/**
	 * Create the frame.
	 */
	public IHM(Controler Controleur) {
		
		this.Controleur = Controleur;
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 692, 583);
		
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
				btnTest_clic(e);
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

		// bouton "Del"
		contentPane.add(btnDel);
		
		// zone d'afichage (avec un scroll) des messages venant de l'application
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(23, 175, 643, 320);
		contentPane.add(scrollPane);
		
		JList lstAffichageConsole = new JList();
		scrollPane.setColumnHeaderView(lstAffichageConsole);
	

	}
}
