package Modèle;

import java.util.concurrent.BlockingQueue;


/**
 * ---------------------------
 * Classe du consommateur
 * ---------------------------
 * 
 * @author balou
 *
 */
public class Consommateur implements Runnable {
    private String consumerName;
    private final BlockingQueue<Produit> queue;

    public Consommateur(String consumerName, int priority, BlockingQueue<Produit> q) {
        this.consumerName = consumerName;
        this.queue = q;
        Thread.currentThread().setPriority(priority);
    }
    
    @Override
    public void run() {
        try {
            while (true) {
                consume(queue.take()); // attente de l'arrivÃ©e d'un produit
            }
        } catch (InterruptedException ex) {
        }
    }
    
    private void consume(Produit x) {
    	System.out.println("--> " + this.consumerName + " >> Consomme : \n\t\t\t" + x.getName() + "" + x.getNumProducteur() + "_" + x.getNumero());
        //System.out.println(" --> " + this.consumerName + " >> Consomme : \n\t\t\t" + x.getInfo() + "\n");
    }
}

