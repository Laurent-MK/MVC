package model;

import java.util.ArrayList;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

import View.ConsoleMK;
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
      * Liste des constructeurs
      */
    

	/**
	 * 
	 * @param producerName
	 * @param numProd
	 * @param delay
	 * @param nbProductionARealiser
	 * @param priority
	 * @param q
	 * @param console
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
     * methode de lancement du thread
     */
    @Override
    public void run() {
    	long i;

		try {
			console.sendMsgToConsole("Producteur numero : " + numProducteur + " cree");
		} catch (InterruptedException e1) {
			// TODO Bloc catch généré automatiquement
			e1.printStackTrace();
		}

        while(true) {
        	// toutes les secondes, un produit est envoye dans la queue
            try {
				//Thread.sleep(1000);

//            	this.queue.put(this.produire());
				boolean queuePleine = this.queue.offer(this.produire(), 200, TimeUnit.MILLISECONDS);
				if (queuePleine) {
					//console.sendMsgToConsole("Producteur numero : " + numProducteur + " => nouveau produit envoye");
				} else {
			    	console.sendMsgToConsole("Producteur numero : " + numProducteur + " QUEUE PLEINE");
					Thread.sleep(0);
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
    	
        return new ProduitText("boulon ", numProducteur, numeroProduit);
    }

    
    
    
    
	@Override
	public String getNom() {
		return this.nomProducteur;
	}

	@Override
	public int getNumero() {
		return this.numProducteur;
	}

	@Override
	public void setNom(String nom) {
		this.nomProducteur = nom;
		
	}
}