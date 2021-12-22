package model;

import java.util.ArrayList;
import java.util.concurrent.BlockingQueue;


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
    private ArrayList<String> msg;
    
    
    
    /**
     * Constructeur : recoit le nom du consommateur, sa priorité et la queue de messages qui va recevoir les produits
     * 
     * @param consumerName
     * @param priority
     * @param q
     */
    public ConsommateurMQ(String consumerName, int priority, BlockingQueue<ProduitText> q, ArrayList<String> msg) {
        setNom(consumerName);
        this.queue = q;
        this.msg = msg;
        
        Thread.currentThread().setPriority(priority);
    }
    
    /**
     * m�thode de lancement du thread consommateur
     *     @Override
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
		msg.add("--> " + nomConsommateur + " >> Consomme (avec transtypage) : \n\t\t\t" + ((ProduitText)x).getName() + "" + ((ProduitText) x).getNumProducteur() + "_" + ((ProduitText)x).getNumero());
		
    	//System.out.println("--> " + nomConsommateur + " >> Consomme (avec transtypage) : \n\t\t\t" + ((ProduitText)x).getName() + "" + ((ProduitText) x).getNumProducteur() + "_" + ((ProduitText)x).getNumero());	
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

