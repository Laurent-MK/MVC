package model;

import java.util.concurrent.BlockingQueue;

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
    private int delay = 100;
	private ConsoleMK console;
	private int nbProductionRealisee = 0;
	public static long nbProdTotale = 0;
	private int numConsole = NUM_CONSOLE_CONSOLE;
	private boolean AjouterNumMsg = AJOUTER_NUM_MESSAGE;

	
	

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
				nbProductionRealisee++;
		    	ProducteurMQ.nbProdTotale++;
				this.queue.put(this.produire());	// on produit un nouvel objet et on l'envoi dans la MQ

				Thread.sleep(this.delay);		// mise en sommeil eventuelle pour freiner la production (fixe par l'IHM)
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
    	
    	console.sendMsgToConsole(new MsgToConsole(numConsole, AjouterNumMsg, 
    											"#" +
    											this.nomProducteur +
    											"_" +
    											this.numProducteur +
    											" >> Produit : " +
    											NOM_PRODUIT_TXT +
    											" " +
    											numProducteur +
    											"_" +
    											++numeroProduit +
    											" / " +
    											nbProdTotale +
    											"\n"));
    	    	
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