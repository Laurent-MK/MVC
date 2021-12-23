package View;

import java.util.ArrayList;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Semaphore;

import model.Consommateur;



/**
 * 
 * Classe Console : un thread destiné à recevoir tous les messages a afficher dans la fenetre de l'application
 * 
 * 
 * @author balou
 *
 */
public class ConsoleMK implements Runnable, Consommateur {

	// proprietes
	public String nomConsole = "nom inconnu";
//    private final BlockingQueue<String> queueMsg;

    private final ArrayBlockingQueue<String> queueMsg;
    
    private IHM ihmApplication;
    private int numeroProducteur;
    
    private static int numMsg = 0;
    
    private ArrayList<String> message = new ArrayList<String>();
    
    private Semaphore sem;
    
    
	
    /**
     * Constructeur
     * 
     * @param consumerName
     * @param priority
     * @param sem 
     * @param q
     * @param msg
     */
    public ConsoleMK(String consumerName, int numero, int priority, ArrayBlockingQueue<String> msgQ, IHM ihmApplication, Semaphore sem)
    {
        this.nomConsole = consumerName;
        this.queueMsg = msgQ;
        this.ihmApplication = ihmApplication;
        this.numeroProducteur = numero;
        this.sem = sem;
       
        Thread.currentThread().setPriority(priority);
    }
	
    
    
    public void afficherMsgConsole(String msg) {
    	System.out.println(msg);
    	
    	//message.add("On a cliqué sur le bouton Go !");
    	//ihmApplication.affichageConsole(message); // affichage dans la fenêtre de console
    }
    
    
    public void sendMsgToConsole(String msg) throws InterruptedException {
    	sem.acquire();
    	this.queueMsg.add(msg); // placement du message dans la queue
    	sem.release();
    }
    
    
	@Override
	public void consommer(Object messageConsole) throws InterruptedException {
		String msg;

		sem.acquire();
		//Thread.sleep(1000);
		
		msg = "msg[" + ++numMsg + "] :  " + (String) messageConsole + "\n";
		
		
		// on a receptionné un message => on doit le passer à l'IHM pour qu'elle l'affiche et l'afficher dans la console syst�me
		message.add(msg);			// stockage de tous les messages dans une liste
		System.out.println((String)messageConsole);

		sem.release();
		

		// affichage dans la fenetre de l'IHM dediee aux messages de console
//		ihmApplication.affichageConsole(msg);
		try {
			ihmApplication.affichageConsole(message);
		} catch (InterruptedException e) {
			System.out.println("ERREUR");
			e.printStackTrace();
		}
	}

	
	
	
	@Override
	public String getNom() {
		return nomConsole;
	}

	@Override
	public void setNom(String nom) {
		this.nomConsole = nom;
		
	}

	@Override
	public void run() {
        try {
            while (true) {
                consommer(queueMsg.take()); // attente de l'arrivee d'un produit dans la queue de message
            }
        } catch (InterruptedException ex) {
        }
		
	}



	@Override
	public int getNumero() {
		return numeroProducteur;
	}

}
