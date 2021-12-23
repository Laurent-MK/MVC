package model;

import java.util.concurrent.BlockingQueue;

import View.ConsoleMK;
import controler.Controler;


/**
 * ---------------------------
 * Classe du consommateur
 * ---------------------------
 * 
 * @author balou
 *
 */
public class ConsommateurMQ implements Runnable, Consommateur {
	
	// proprietes
	public String nomConsommateur = "nom inconnu";
    private final BlockingQueue<ProduitText> queue;
    private Controler controleur;
    private ConsoleMK consoleMK;
    private int numero;

        
    
    /**
     * Constructeur : recoit le nom du consommateur, sa priorité et la queue de messages qui va recevoir les produits
     * 
     * 
     * @param consumerName
     * @param num
     * @param priority
     * @param q
     * @param msgQ_Console
     * @param controleur
     * @param consoleMK
     */
    public ConsommateurMQ(String consumerName,
    		int num,
    		int priority,
    		BlockingQueue<ProduitText> q,
    		BlockingQueue<String> msgQ_Console,
    		Controler controleur,
    		ConsoleMK consoleMK)
    {
        setNom(consumerName);
        this.queue = q;
        this.controleur = controleur;
        this.consoleMK = consoleMK;
        this.numero = num;
        
        Thread.currentThread().setPriority(priority);
    }
    
    
    /**
     * methode de lancement du thread consommateur
     * @Override
     */
    public void run() {
        try {
            while (true) {
                consommer(queue.take()); // attente de l'arrivee d'un produit dans la queue de message
                Thread.sleep(0);
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
				+ " => Consomme : \n\t\t\t"
				+ ((ProduitText)x).getName()
				+ " "
				+ ((ProduitText)x).getNumProducteur()
				+ " "
				+ ((ProduitText)x).getNumero();

		consoleMK.sendMsgToConsole(msgAAfficher);
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

