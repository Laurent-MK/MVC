package testMQ;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

/**
 * programme de test des message queue bloquantes entre plusieurs thread
 * 
 * @author balou
 *
 */

public class TestMQMT {
    private static int delaiDeProductionP1 = 0;
    private static int delaiDeProductionP2 = 0;
    private static int nbProdP1 = 4;
    private static int nbProdP2 = 4;
    private static int prioriteP1 = 10;
    private static int prioriteP2= 1;
    
    public static void main(String[] args) {
        
    	/**
    	 *  Création de la message queue "bloquante
    	 */
        BlockingQueue<Produit> q = new ArrayBlockingQueue<Produit>(5);
        
        Producteur producer1 = new Producteur("P1", 1, delaiDeProductionP1, nbProdP1, 3, q);
        Producteur producer2 = new Producteur("P2", 2, delaiDeProductionP2, nbProdP2, 8, q);
        Producteur producer3 = new Producteur("P3", 3, delaiDeProductionP2, nbProdP2, 6, q);
        
        Consommateur consumer1 = new Consommateur("Consommateur 01", 2, q); // lancement des consommateurs
        Consommateur consumer2 = new Consommateur("Consommateur 02", 1, q);
        Consommateur consumer3 = new Consommateur("Consommateur 03", 5, q);

        
        // Starting the threads
        new Thread(producer1).start();
       
       	try {
			Thread.sleep(0);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

        /**
         * lancement des producteurs
         */
        new Thread(producer2).start();
        new Thread(producer3).start();
        
        new Thread(consumer1).start();
        new Thread(consumer2).start();
        new Thread(consumer3).start();
        
        System.out.println("\t main() terminé !!!\n");
    }
}

