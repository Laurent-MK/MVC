package model;

import java.util.ArrayList;
import java.util.concurrent.BlockingQueue;

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
    private int numeroProduit;			// numero de produit
    private int numProducteur; 				// numero de producteur
    private String nomProducteur = "nom inconnu";	// nom du producteur
    private BlockingQueue<ProduitText> queue = null;
    private int delay = 100;
    private int nbBoucles;
	private Controler controleur = null;
	
    private ArrayList<String> msg;

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
     public ProducteurMQ(String producerName, int numProd, int delay, int nbProductionARealiser, BlockingQueue<ProduitText> q, ArrayList<String> msg) {
        setNom(producerName);
        this.delay = delay;
        this.queue = q;
        this.nbBoucles = nbProductionARealiser;
        this.numProducteur = numProd;
        this.msg = msg;
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
    public ProducteurMQ(String producerName,
    		int numProd,
    		int delay,
    		int nbProductionARealiser,
    		int priority,
    		BlockingQueue<ProduitText> q,
    		ArrayList<String> msg,
    		Controler controleur)
    {
        this.nomProducteur = producerName;
        this.delay = delay;
        this.queue = q;
        this.nbBoucles = nbProductionARealiser;
        this.numProducteur = numProd;
        this.msg = msg;
        this.controleur = controleur;
      
        Thread.currentThread().setPriority(priority);
    }

    
    /**
     * m�thode de lancement du thread
     */
    @Override
    public void run() {
    	long i;

//        msg.add("Producteur n° : " + numProducteur + " crée");
        controleur.dmdModelAffichageConsole("Producteur n° : " + numProducteur + " crée");
        
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
        controleur.dmdModelAffichageConsole("#" + this.nomProducteur + "_" + this.numProducteur + " >> Cr�ation d'un nouveau produit : " + "boulon " + numProducteur + "_" + ++numeroProduit + "\n");
    	
//    	msg.add("#" + this.nomProducteur + "_" + this.numProducteur + " >> Cr�ation d'un nouveau produit : " + "boulon " + numProducteur + "_" + ++numeroProduit + "\n");
    	
        //System.out.println("#" + this.nomProducteur + "_" + this.numProducteur + " >> Cr�ation d'un nouveau produit : " + "boulon " + numProducteur + "_" + ++numeroProduit + "\n");
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