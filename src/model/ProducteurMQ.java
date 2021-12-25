package model;

import java.util.concurrent.BlockingQueue;

import utilitairesMK.ConsoleMK;


/**
 * META KONSULTING
 * 
 * Classe du producteur. Le thread produit des objets et les envoit dans la queue de message destinee aux consommateurs
 * A chque production, le thread informe l'IHM de cette nouvelle production
 * 
 * @author balou
 *
 */
public class ProducteurMQ implements Runnable, Producteur, Constantes {
    private int numeroProduit;						// numero de produit
    private int numProducteur; 						// numero de producteur
    private String nomProducteur = "nom inconnu";	// nom du producteur
    private BlockingQueue<ProduitText> queue = null;
    private int delay = 100;
	private ConsoleMK console;
	private int nbProductionRealisee = 0;
	private static long nbProdTotale = 0;
	
	

	/**
	 * Constructeur
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
    		int priority,
    		BlockingQueue<ProduitText> msgQProduit,
    		ConsoleMK console)
    {
        this.nomProducteur = producerName;
        this.delay = delay;
        this.queue = msgQProduit;
        this.numProducteur = numProd;
        this.console = console;
      
        Thread.currentThread().setPriority(priority);
    }

    
    /**
     * methode de lancement du thread
     */
    @Override
    public void run() {
 
		console.sendMsgToConsole("Producteur numero : " + numProducteur + " cree");

        while(true) {
        	try {
				this.queue.put(this.produire());	// on produit un nouvel objet et on l'envoi dans la MQ
				nbProductionRealisee++;
		    	ProducteurMQ.nbProdTotale++;

				Thread.sleep(this.delay);		// mise en sommeil
			} catch (InterruptedException e) {
				// TODO Bloc catch g�n�r� automatiquement
				e.printStackTrace();
			}      	
        }
    }
    
    
    
    /**
     * production d'un nouveau produit a envoyer aux consommateurs
     * 
     * @return
     * @throws InterruptedException 
     */
    public ProduitText produire() throws InterruptedException {
    	
    	console.sendMsgToConsole("#" +
    			this.nomProducteur +
    			"_" +
    			this.numProducteur +
    			" >> nouveau produit : Product " +
    			numProducteur +
    			"_" +
    			++numeroProduit +
    			"\n");
    	    	
        return (new ProduitText("Product ", numProducteur, numeroProduit));
    }

    
    public int getNbProdReal() {
    	return nbProductionRealisee;
    }

    public long getNbProdTotale() {
    	return nbProdTotale;
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