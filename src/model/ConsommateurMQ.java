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
public class ConsommateurMQ implements Runnable {
    private String consumerName;
    private final BlockingQueue<ProduitText> queue;

    public ConsommateurMQ(String consumerName, int priority, BlockingQueue<ProduitText> q) {
        this.consumerName = consumerName;
        this.queue = q;
        Thread.currentThread().setPriority(priority);
    }
    
    /**
     * méthode de lancement du thread
     *     @Override
     */
    public void run() {
        try {
            while (true) {
                consume(queue.take()); // attente de l'arrivÃ©e d'un produit dans la queue de message
            }
        } catch (InterruptedException ex) {
        }
    }

    /**
     * consommation d'un produit arrivé dans la queue de message
     * @param x
     */
    private void consume(ProduitText x) {
    	System.out.println("--> " + this.consumerName + " >> Consomme : \n\t\t\t" + x.getName() + "" + x.getNumProducteur() + "_" + x.getNumero());
        //System.out.println(" --> " + this.consumerName + " >> Consomme : \n\t\t\t" + x.getInfo() + "\n");
    }
}

