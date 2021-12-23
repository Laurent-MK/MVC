package model;

import java.io.Console;
import java.util.ArrayList;
import java.util.concurrent.BlockingQueue;

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
	
	// propriétés
	public String nomConsommateur = "nom inconnu";
    private final BlockingQueue<ProduitText> queue;
    private Controler controleur;
    private ConsoleMK consoleMK;

        
    
    /**
     * Constructeur : recoit le nom du consommateur, sa priorité et la queue de messages qui va recevoir les produits
     * 
     * @param consumerName
     * @param priority
     * @param q
     */
    public ConsommateurMQ(String consumerName,
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
        
        Thread.currentThread().setPriority(priority);
    }
    
    /**
     * m�thode de lancement du thread consommateur
     * @Override
     */
    public void run() {
        try {
            while (true) {
                consommer(queue.take()); // attente de l'arrivée d'un produit dans la queue de message
            }
        } catch (InterruptedException ex) {
        }
    }


	@Override
    /**
     * consommation d'un produit arriv� dans la queue de message
     * @param x
     */
	public void consommer(Object x) {
		String msgAAfficher = "--> "
				+ nomConsommateur
				+ " + "
				+ "Consomme : \n\t\t\t"
				+ ((ProduitText)x).getName()
				+ " "
				+ ((ProduitText)x).getNumProducteur()
				+ " "
				+ ((ProduitText)x).getNumero();

		consoleMK.sendMsgToConsole(msgAAfficher);

		
//		controleur.dmdModelAffichageConsole(msgAAfficher);
	}

	@Override
	public String getNom() {
		return this.nomConsommateur;
	}

	@Override
	public void setNom(String nom) {
		this.nomConsommateur = nom;
	}

	
}

