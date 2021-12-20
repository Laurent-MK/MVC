package Modèle;

import java.util.concurrent.BlockingQueue;

/**
 * ------------------------------------------
 * Classe du producteur : implemente Thread
 * ------------------------------------------
 * 
 * @author balou
 *
 */
public class Producteur implements Runnable {
    private int numeroProduit = 0;			// numï¿½ro de produit
    private int numProducteur; 				// numï¿½ro de producteur

    private String producerName = "";
    private BlockingQueue<Produit> queue = null;
    private int delay = 0;
    private int nbBoucles;

    /*
     * Liste des constructeurs possibles
     */
    public Producteur(String producerName, int numProd, int delay, int nbProductionARealiser, BlockingQueue<Produit> q) {
        this.producerName = producerName;
        this.delay = delay;
        this.queue = q;
        this.nbBoucles = nbProductionARealiser;
        this.numProducteur = numProd;
    }
    
    public Producteur(String producerName, int numProd, int delay, int nbProductionARealiser, int priority, BlockingQueue<Produit> q) {
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
                Thread.sleep(this.delay * 1000); // endormissement durant "delay" secondes.
                for (i =0; i < 555000; i++) {
                	;
                }
                this.queue.put(this.produce());
            }
        } catch (InterruptedException ex) {
        }
    }
    
    /**
     * production d'un nouveau produit a envoyer aux consommateurs
     * @return
     */
    private Produit produce() {
    	//numeroProduit++;
    	
        System.out.println("#" + this.producerName + "_" + numProducteur + " >> Crï¿½ation d'un nouveau produit : " + "boulon " + numProducteur + "_" + ++numeroProduit + "\n");
        return new Produit("boulon ", numProducteur, numeroProduit);
    }
}