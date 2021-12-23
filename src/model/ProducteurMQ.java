package model;

import java.util.ArrayList;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

import controler.Controler;

/**
 * ------------------------------------------
 * Classe du producteur : implemente Thread
 * ------------------------------------------
 * 
 * @author balou
 *
 */
public class ProducteurMQ implements Runnable, Producteur {
    private int numeroProduit;						// numero de produit
    private int numProducteur; 						// numero de producteur
    private String nomProducteur = "nom inconnu";	// nom du producteur
    private BlockingQueue<ProduitText> queue = null;
    private int delay = 100;
    private int nbBoucles;
	private ConsoleMK console;
	
     /**
      * Liste des constructeurs possibles
      */
    
      /**
      * 
      * @param producerName
      * @param numProd
      * @param delay
      * @param nbProductionARealiser
      * @param priority
      * @param q
      */
    public ProducteurMQ(
    		String producerName,
    		int numProd,
    		int delay,
    		int nbProductionARealiser,
    		int priority,
    		BlockingQueue<ProduitText> q,
    		ConsoleMK console)
    {
        this.nomProducteur = producerName;
        this.delay = delay;
        this.queue = q;
        this.nbBoucles = nbProductionARealiser;
        this.numProducteur = numProd;
        this.console = console;
      
        Thread.currentThread().setPriority(priority);
    }

    
    /**
     * m�thode de lancement du thread
     */
    @Override
    public void run() {
    	long i;

    	console.afficherMsgConsole("Producteur numero : " + numProducteur + " cree");
//        controleur.dmdModelAffichageConsole("Producteur n° : " + numProducteur + " crée");

        while(true) {
        	// toutes les secondes un produit est envoye dans la queue
            try {
				Thread.sleep(1000);
				boolean queuePleine = this.queue.offer(this.produire(), 200, TimeUnit.MILLISECONDS);
				
				if (queuePleine) {
					System.out.println("nouveu produit envoyé");
				} else {
					System.out.println("queue pleine");					
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
        }
    }
    
    
    
    /**
     * production d'un nouveau produit a envoyer aux consommateurs
     * @return
     */
    public ProduitText produire() {
    	
    	console.afficherMsgConsole("#" +
    			this.nomProducteur +
    			"_" +
    			this.numProducteur +
    			" >> Creation d'un nouveau produit : " +
    			"boulon " +
    			numProducteur +
    			"_" +
    			++numeroProduit +
    			"\n");
    	
    	/*controleur.dmdModelAffichageConsole("#" +
        			this.nomProducteur +
        			"_" +
        			this.numProducteur +
        			" >> Creation d'un nouveau produit : " +
        			"boulon " +
        			numProducteur +
        			"_" +
        			++numeroProduit +
        			"\n");
    	*/
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