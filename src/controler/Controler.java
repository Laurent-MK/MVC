package controler;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

/**
 * imports des packages spécifiques de l'application
 */
import View.IHM;
import model.ConsommateurMQ;
import model.ProducteurMQ;
import model.ProduitText;



/**
 * Classe controleur de l'application de test des messages queue bloquantes entre plusieurs threads
 * 
 * @author balou
 *
 */

public class Controler {
	IHM ihmApplication;
    private static int delaiDeProductionP1 = 0;
    private static int delaiDeProductionP2 = 0;
    private static int nbProdP1 = 4;
    private static int nbProdP2 = 4;
    private static int prioriteP1 = 10;
    private static int prioriteP2= 1;
    
    
    public static void main(String[] args) {
    	
    	new Controler(); // lancement du controleur
        
    	//  Création de la message queue "bloquante"
        BlockingQueue<ProduitText> q = new ArrayBlockingQueue<ProduitText>(5);
        
        // création des producteurs
        ProducteurMQ producer1 = new ProducteurMQ("P1", 1, delaiDeProductionP1, nbProdP1, 3, q);
        ProducteurMQ producer2 = new ProducteurMQ("P2", 2, delaiDeProductionP2, nbProdP2, 8, q);
        ProducteurMQ producer3 = new ProducteurMQ("P3", 3, delaiDeProductionP2, nbProdP2, 6, q);
        
        // création des consommateurs
        ConsommateurMQ consumer1 = new ConsommateurMQ("Consommateur 01", 2, q);
        ConsommateurMQ consumer2 = new ConsommateurMQ("Consommateur 02", 1, q);
        ConsommateurMQ consumer3 = new ConsommateurMQ("Consommateur 03", 5, q);
        
        
        // démarrage des threads des producteurs avec retard possible sur le 1er
        new Thread(producer1).start();
       
       	try {
			Thread.sleep(0);	// retard éventuel avant lancement des autres
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
        new Thread(producer2).start();
        new Thread(producer3).start();
        
        new Thread(consumer1).start();
        new Thread(consumer2).start();
        new Thread(consumer3).start();
        
        System.out.println("\t main() terminÃ© !!!\n");
    }
    

    /*
     * Constructeur : démarre l'IHM et envoi le controleur en paramètre
     */
    public Controler() {
    	ihmApplication = new IHM(this); // l'IHM recoit le controleur en paramètre
    	ihmApplication.setVisible(true); // affichage de l'IHM
    }
}

