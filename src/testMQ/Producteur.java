package testMQ;

import java.util.concurrent.BlockingQueue;

/**
 * ------------------------------------------
 * Classe du producteur : implemente Thread
 * ------------------------------------------
 * 
 * @author balou
 *
 */
class Producteur implements Runnable {
    private int numeroProduit = 0;			// num�ro de produit
    private int numProducteur; 				// num�ro de producteur

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
                Thread.sleep(this.delay * 1000); // 'delay' second.
                for (i =0; i < 555000; i++) {
                	;
                }
                this.queue.put(this.produce());
            }
        } catch (InterruptedException ex) {
        }
    }
    
    private Produit produce() {
    	//numeroProduit++;
    	
        System.out.println("#" + this.producerName + "_" + numProducteur + " >> Cr�ation d'un nouveau produit : " + "boulon " + numProducteur + "_" + ++numeroProduit + "\n");
        return new Produit("boulon ", numProducteur, numeroProduit);
    }
}