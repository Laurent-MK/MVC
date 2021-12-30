package model;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import utilitairesMK.ConsoleMK;
import utilitairesMK.MsgToConsole;


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
    private int delay = FREQ_PRODUCTION;
	private ConsoleMK console;
	private int nbProductionRealisee = 0;
	private int numConsole = NUM_CONSOLE_CONSOLE;
	private boolean AjouterNumMsg = AJOUTER_NUM_MESSAGE;

	/**
	 * ATTENTION, la variable de classe nbProdTotale doit etre protegee par un verrou car 
	 * tous les threads "instanciant" cette classe auront acces en concurrence a cette variable
	 * Si des acces concurrents se font sur une variable de classe non protegee, sa valeur devient
	 * imprevisible 
	 */
	public static long nbProdTotale;	// variable de classe : nbr total de production realise par cette classe
	public static Lock verrou = new ReentrantLock();	// verrou de protection de la variable de classe
	/*-*/
	

	/**
	 * Constructeur
	 * 
	 * @param producerName
	 * @param numProd
	 * @param delay
	 * @param priority
	 * @param msgQProduit
	 * @param console
	 * @param numConsole
	 */
    public ProducteurMQ(
    		String producerName,
    		int numProd,
    		int delay,
    		int priority,
    		BlockingQueue<ProduitText> msgQProduit,
    		ConsoleMK console,
    		int numConsole)
    {
        this.nomProducteur = producerName;
        this.delay = delay;
        this.queue = msgQProduit;
        this.numProducteur = numProd;
        this.console = console;
        this.numConsole = numConsole;
        
		int sommeil = (int) (Math.random() * ((AJOUT_DELAY_PROD_MAX - AJOUT_DELAY_PROD_MIN)+AJOUT_DELAY_PROD_MIN));
		this.delay = delay + sommeil;
      
        Thread.currentThread().setPriority(priority);
    }

    
    /**
     * methode de lancement du thread
     */
    @Override
    public void run() {
 
		console.sendMsgToConsole(new MsgToConsole(numConsole, AjouterNumMsg, "Producteur numero : " + numProducteur + " cree"));

        while(true) {
        	try {
				this.queue.put(this.produire());	// on produit un nouvel objet et on l'envoi dans la MQ
				
				Thread.sleep(delay);		// mise en sommeil eventuelle pour freiner la production (fixe par l'IHM)
			} catch (InterruptedException e) {
				// TODO Bloc catch genere automatiquement
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
    	
		nbProductionRealisee++;	// nbr de productions realisee par cet objet (donc le thread l'abritant)
    	numeroProduit++;

		/*
		 * morceau de code pour mettre en oeuvre un verrou sur la variable de classe nbProductionRealisee.*
		 * dans un systeme multithreade, celle-ci est modifiee en parallele par plusieurs threads producteurs
		 * Cela abouti à une valeur alleatoire de cette variable. L'utilisation du verrou permet d'optenir un MUTEX
		 * qui cree une section critique dans laquelle on peut modifier la varaibel sans risque d'instabilite.
		 * ATTENTION : cette section critique "serialise" les threads puisqu'ils vont tous devoir attendre que le verrou soit
		 * libre pour continuer leur execution.
		 * 
		 * 
		verrou.lock();
		ProducteurMQ.nbProdTotale++;
		verrou.unlock();
		*/
		
    	console.sendMsgToConsole(new MsgToConsole(numConsole, AjouterNumMsg, 
    											"#"
    											+ this.nomProducteur
    											+ "_"
    											+ this.numProducteur
    											+ " >> Produit : "
    											+ NOM_PRODUIT_TXT
    											+ " "
    											+ numProducteur
    											+ "_"
    											+ numeroProduit
    											+ " / "
    											+ nbProdTotale
    											+ "\n"));
  /*
   * debug pour analyser le fonctionnement de la protection de la variable de classe 
   * "ProducteurMQ.nbProdTotale".
   * 
   * 	
    	System.out.println("Thread : "
    				+ Thread.currentThread().getName()
    				+ " nbProdTotale = "
    				+ ProducteurMQ.nbProdTotale
    				+ " nbProductionRealisee par ce thread :"
    				+ nbProductionRealisee);
*/   	    	
        return (new ProduitText(NOM_PRODUIT_TXT, numProducteur, numeroProduit));
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