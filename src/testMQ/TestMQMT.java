package testMQ;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

/**
 * programme de test des message queue bloquantes entre plusieurs thread
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
        BlockingQueue<Product> q = new ArrayBlockingQueue<Product>(5);
        
        Producer producer1 = new Producer("P1", 1, delaiDeProductionP1, nbProdP1, 3, q);
        Producer producer2 = new Producer("P2", 2, delaiDeProductionP2, nbProdP2, 8, q);
        Producer producer3 = new Producer("P3", 3, delaiDeProductionP2, nbProdP2, 6, q);
        
        Consumer consumer1 = new Consumer("Consommateur 01", 2, q); // lancement des consommateurs
        Consumer consumer2 = new Consumer("Consommateur 02", 1, q);
        Consumer consumer3 = new Consumer("Consommateur 03", 5, q);

        
        // Starting the threads
        new Thread(producer1).start();
       
       	try {
			Thread.sleep(0);
		} catch (InterruptedException e) {
			//pr
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




/**
 * Classe Produit
 * 
 * @author balou
 *
 */
class Product {
    private String name;
    private int numero;
    private int numProducteur;
    
    // les constructeurs
    public Product(String name, int numero) {
        this.name = name;
        this.numero = numero;
    }
    
    public Product(String name, int numProducteur, int numero) {
        this.name = name;
        this.numero = numero;
        this.numProducteur = numProducteur;
    }
    
    // les méthodes de la classe
    public String getInfo() {
        return ("Nom du produit : " + this.name + " ayant le num�ro => " + this.numProducteur + "_" + this.numero);
    }
    
    public String getName() {
    	return "" + this.name;
    }
    public String getNumero() {
    	return "" + this.numero;
    }
    public String getNumProducteur () {
    	return "" + numProducteur;
    }
}




/**
 * ---------------------------
 * Classe du consommateur
 * ---------------------------
 * 
 * @author balou
 *
 */
class Consumer implements Runnable {
    private String consumerName;
    private final BlockingQueue<Product> queue;

    public Consumer(String consumerName, int priority, BlockingQueue<Product> q) {
        this.consumerName = consumerName;
        this.queue = q;
        Thread.currentThread().setPriority(priority);
    }
    
    @Override
    public void run() {
        try {
            while (true) {
                consume(queue.take()); // attente de l'arrivée d'un objet
            }
        } catch (InterruptedException ex) {
        }
    }
    
    private void consume(Product x) {
    	System.out.println("--> " + this.consumerName + " >> Consomme : \n\t\t\t" + x.getName() + "" + x.getNumProducteur() + "_" + x.getNumero());
        //System.out.println(" --> " + this.consumerName + " >> Consomme : \n\t\t\t" + x.getInfo() + "\n");
    }
}




/**
 * ----------------------------------------
 * Classe du producteur : implemente Thread
 * ----------------------------------------
 * 
 * @author balou
 *
 */
class Producer implements Runnable {
    private int numeroProduit = 0;			// num�ro de produit
    private int numProducteur; 				// num�ro de producteur

    private final String producerName;
    private final BlockingQueue<Product> queue;
    private final int delay; // Seconds
    private int nbBoucles;

    /*
     * Liste des constructeurs possibles
     */
    public Producer(String producerName, int numProd, int delay, int nbProductionARealiser, BlockingQueue<Product> q) {
        this.producerName = producerName;
        this.delay = delay;
        this.queue = q;
        this.nbBoucles = nbProductionARealiser;
        this.numProducteur = numProd;
    }
    
    public Producer(String producerName, int numProd, int delay, int nbProductionARealiser, int priority, BlockingQueue<Product> q) {
        this.producerName = producerName;
        this.delay = delay;
        this.queue = q;
        this.nbBoucles = nbProductionARealiser;
        this.numProducteur = numProd;
        
        Thread.currentThread().setPriority(priority);
    }

    
    
    @Override
    public void run() {
    	long i;
    	
        try {
            while (--nbBoucles > 0) {
                Thread.sleep(this.delay * 1000); // 'delay' second.
                for (i =0; i < 555000; i++) {
                	;
                }
                this.queue.put(this.produce());
            }
        } catch (InterruptedException ex) {
        }
    }
    
    private Product produce() {
    	//numeroProduit++;
    	
        System.out.println("#" + this.producerName + "_" + numProducteur + " >> Cr�ation d'un nouveau produit : " + "boulon " + numProducteur + "_" + ++numeroProduit + "\n");
        return new Product("boulon ", numProducteur, numeroProduit);
    }
}
