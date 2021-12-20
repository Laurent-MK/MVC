package controler;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

/**
 * imports des packages spécifiques de l'application
 */
import model.Consommateur;
import model.Producteur;
import model.Produit;



/**
 * programme de test des messages queue bloquantes entre plusieurs threads
 * 
 * @author balou
 *
 */

public class Controler {
    private static int delaiDeProductionP1 = 0;
    private static int delaiDeProductionP2 = 0;
    private static int nbProdP1 = 4;
    private static int nbProdP2 = 4;
    private static int prioriteP1 = 10;
    private static int prioriteP2= 1;
    
    public static void main(String[] args) {
        
    	//  Création de la message queue "bloquante"
        BlockingQueue<Produit> q = new ArrayBlockingQueue<Produit>(5);
        
        // création des producteurs
        Producteur producer1 = new Producteur("P1", 1, delaiDeProductionP1, nbProdP1, 3, q);
        Producteur producer2 = new Producteur("P2", 2, delaiDeProductionP2, nbProdP2, 8, q);
        Producteur producer3 = new Producteur("P3", 3, delaiDeProductionP2, nbProdP2, 6, q);
        
        // création des consommateurs
        Consommateur consumer1 = new Consommateur("Consommateur 01", 2, q);
        Consommateur consumer2 = new Consommateur("Consommateur 02", 1, q);
        Consommateur consumer3 = new Consommateur("Consommateur 03", 5, q);
        
        
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
}

