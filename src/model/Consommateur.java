package model;

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
    
    /**
     * m�thode de lancement du thread
     *     @Override
     */
    public void run() {
        try {
            while (true) {
                consume(queue.take()); // attente de l'arrivée d'un produit dans la queue de message
            }
        } catch (InterruptedException ex) {
        }
    }

    /**
     * consommation d'un produit arriv� dans la queue de message
     * @param x
     */
    private void consume(Produit x) {
    	System.out.println("--> " + this.consumerName + " >> Consomme : \n\t\t\t" + x.getName() + "" + x.getNumProducteur() + "_" + x.getNumero());
        //System.out.println(" --> " + this.consumerName + " >> Consomme : \n\t\t\t" + x.getInfo() + "\n");
    }
}

