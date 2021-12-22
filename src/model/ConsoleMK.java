package model;

import java.util.ArrayList;
import java.util.concurrent.BlockingQueue;

import View.IHM;



/**
 * 
 * Classe Console : un thread destiné à recevoir tous les messages a afficher dans la fenetre de l'application
 * 
 * 
 * @author balou
 *
 */
public class ConsoleMK implements Runnable, Consommateur {

	// propriétés
	public String nomConsole = "nom inconnu";
    private final BlockingQueue<String> queueMsg;
    private IHM ihmApplication;
    
    private static int numMsg = 0;
    
    private ArrayList<String> message = new ArrayList<String>();
	
    /**
     * Constructeur
     * 
     * @param consumerName
     * @param priority
     * @param q
     * @param msg
     */
    public ConsoleMK(String consumerName, int priority, BlockingQueue<String> q, IHM ihmApplication)
    {
        setNom(consumerName);
        this.queueMsg = q;
        this.ihmApplication = ihmApplication;
       
        
        Thread.currentThread().setPriority(priority);
    }
	
    
    
    public void afficherMsgConsole(String msg) {
    	System.out.println(msg);
    	
    	//message.add("On a cliqué sur le bouton Go !");
    	//ihmApplication.affichageConsole(message); // affichage dans la fenêtre de console
    }
    
    
    public void sendMsgToConsole(String msg) {
    	this.queueMsg.add(msg); // placement du message dans la queue
    }
    
    
	@Override
	public void consommer(Object messageConsole) {
		String msg;
		
		msg = "msg n ° : " + ++numMsg + " " + (String) messageConsole + "\n";
		
		// on a receptionné un message => on doit le passer à l'IHM pour qu'elle l'affiche
				
		System.out.println((String)messageConsole);
		message.add(msg);
		
		//System.out.println("taille de la console = " + message.size() + " -> msg = " + msg);
					
		//message.add("msg n ° : " + ++numMsg + " " + (String) messageConsole);
		ihmApplication.affichageConsole(message); // affichage dans la fenêtre de console
		
	}

	@Override
	public String getNom() {
		// TODO Stub de la méthode généré automatiquement
		return null;
	}

	@Override
	public void setNom(String nom) {
		this.nomConsole = nom;
		
	}

	@Override
	public void run() {
        try {
            while (true) {
                consommer(queueMsg.take()); // attente de l'arrivée d'un produit dans la queue de message
            }
        } catch (InterruptedException ex) {
        }
		
	}

}
