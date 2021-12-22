package model;

import java.util.concurrent.BlockingQueue;

/**
 * ------------------------------------------
 * Classe du producteur : implemente Thread
 * ------------------------------------------
 * 
 * @author balou
 *
 */
public class ProducteurMQ implements Runnable, Producteur {
    private int numeroProduit;			// numero de produit
    private int numProducteur; 				// numero de producteur
    private String nomProducteur = "nom inconnu";	// nom du producteur
    private BlockingQueue<ProduitText> queue = null;
    private int delay = 0;
    private int nbBoucles;

     /**
      * Liste des constructeurs possibles
      */
    
    /**
     * 
      * @param producerName
      * @param numProd
      * @param delay
      * @param nbProductionARealiser
      * @param q
      */
     public ProducteurMQ(String producerName, int numProd, int delay, int nbProductionARealiser, BlockingQueue<ProduitText> q) {
        setNom(producerName);
        this.delay = delay;
        this.queue = q;
        this.nbBoucles = nbProductionARealiser;
        this.numProducteur = numProd;
    }

     /**
      * 
      * @param producerName
      * @param numProd
      * @param delay
      * @param nbProductionARealiser
      * @param priority
      * @param q
      */
    public ProducteurMQ(String producerName, int numProd, int delay, int nbProductionARealiser, int priority, BlockingQueue<ProduitText> q) {
        this.nomProducteur = producerName;
        this.delay = delay;
        this.queue = q;
        this.nbBoucles = nbProductionARealiser;
        this.numProducteur = numProd;
        
        Thread.currentThread().setPriority(priority);
    }

    
    /**
     * m�thode de lancement du thread
     */
    @Override
    public void run() {
    	long i;
    	
        try {
            while (--nbBoucles > 0) {
                Thread.sleep(this.delay * 1000); // endormissement durant "delay" secondes.
                for (i =0; i < 555000; i++) {
                	;
                }
                this.queue.put(this.produire());
            }
        } catch (InterruptedException ex) {
        }
    }
    
    /**
     * production d'un nouveau produit a envoyer aux consommateurs
     * @return
     */
    public ProduitText produire() {
    	//numeroProduit++;
    	
        System.out.println("#" + this.nomProducteur + "_" + this.numProducteur + " >> Cr�ation d'un nouveau produit : " + "boulon " + numProducteur + "_" + ++numeroProduit + "\n");
        return new ProduitText("boulon ", numProducteur, numeroProduit);
    }

    
    
    
    
	@Override
	public String getNom() {
		return this.nomProducteur;
	}

	@Override
	public int getNumProducteur() {
		return this.numProducteur;
	}

	@Override
	public void setNom(String nom) {
		this.nomProducteur = nom;
		
	}
}