package model;

import java.util.concurrent.BlockingQueue;

import UtilitairesMK.ConsoleMK;



/**
 * META KONSULTING
 * 
 * Classe du consommateur. Le thread se met en attente sur la queue de message remplie par les producteurs
 * Loirsqu'un objet arrive, il le recoit et en informe l'IHM
 * 
 * @author balou
 *
 */
public class ConsommateurMQ implements Runnable, Consommateur {
	
	// proprietes
	private String nomConsommateur = "nom inconnu";
    private final BlockingQueue<ProduitText> queue;
    private ConsoleMK consoleMK;
    private int numero;
    private int nbConsoRealisees = 0;

    public static long nbConsoTotale = 0;
        
    
    /**
     * Constructeur : recoit le nom du consommateur, sa priorité, la queue de messages qui va recevoir les produits
     * 
     * @param consumerName
     * @param num
     * @param priority
     * @param q
     * @param consoleMK
     */
    public ConsommateurMQ(String consumerName,
    		int num,
    		int priority,
    		BlockingQueue<ProduitText> q,
    		ConsoleMK consoleMK)
    {
        setNom(consumerName);
        this.queue = q;
        this.consoleMK = consoleMK;
        this.numero = num;
        
        Thread.currentThread().setPriority(priority);
    }
    
    
    /**
     * methode de lancement du thread consommateur
     */
    @Override
    public void run() {
        try {
            while (true) {
                consommer(queue.take()); // attente de l'arrivee d'un produit dans la queue de message
                nbConsoRealisees++;
                ConsommateurMQ.nbConsoTotale++;
            }
        } catch (InterruptedException ex) {
        }
    }



    /**
     * consommation d'un produit arrivee dans la queue de message
     * @param x
     * @throws InterruptedException 
     */
	@Override
	public void consommer(Object x) throws InterruptedException {
		String msgAAfficher = "--> "
				+ nomConsommateur
				+ " => Consomme : "
				+ ((ProduitText)x).getName()
				+ " "
				+ ((ProduitText)x).getNumProducteur()
				+ "_"
				+ ((ProduitText)x).getNumero();

		consoleMK.sendMsgToConsole(msgAAfficher);
	}
	
	
	public int getNbConsoRealisees() {
		return this.nbConsoRealisees;
	}

	public long getNbConsoTotale() {
		return nbConsoTotale;
	}
	
	@Override
	public String getNom() {
		return this.nomConsommateur;
	}

	
	
	@Override
	public void setNom(String nom) {
		this.nomConsommateur = nom;
	}


	@Override
	public int getNumero() {
		return this.numero;
	}

	
}

