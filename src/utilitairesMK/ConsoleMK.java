package utilitairesMK;

import java.util.concurrent.ArrayBlockingQueue;

import model.Consommateur;
import model.Constantes;
import view.IHM;



/**
 * META KONSULTING
 * 
 * Classe ConsoleMK : un thread destiné à recevoir tous les messages a afficher dans la fenetre de l'application
 * 
 * 
 * @author balou
 *
 */
public class ConsoleMK implements Runnable, Consommateur, Constantes {

	// proprietes
	private String nomConsole = "nom inconnu";
    private final ArrayBlockingQueue<MsgToConsole> queueMsg;
    private IHM ihmApplication;
    private int numeroProducteur;
    private static int numMsg = 0;
	
    /**
     * Constructeur
     * 
     * @param consumerName
     * @param numero
     * @param priority
     * @param msgQ
     * @param ihmApplication
     */
    public ConsoleMK(String consumerName, int numero, int priority, ArrayBlockingQueue<MsgToConsole> msgQ, IHM ihmApplication)
    {
        this.nomConsole = consumerName;
        this.queueMsg = msgQ;
        this.ihmApplication = ihmApplication;
        this.numeroProducteur = numero;
       
        Thread.currentThread().setPriority(priority);
    }
	
    
    /**
     * Pour envoyer un message vers la console situee dans l'IHM.
     * Le message a afficher est place dans la MQ du thread de console.
     * 
     * @param msg
     */
    public void sendMsgToConsole(MsgToConsole msg) {
    	if (queueMsg.remainingCapacity() > 1)
			try {
				this.queueMsg.put(msg);
			} catch (InterruptedException e) {
				// TODO Bloc catch généré automatiquement
				e.printStackTrace();
			}
		else {
    		System.out.println("queue de la console pleine => message perdu !!! : " + Thread.currentThread().getName());
    	}
    }
    
    
    /**
     * Retirer un message present dans la queue du thread de console
     */
	@Override
	public void consommer(Object msgConsole) throws InterruptedException {
		String msg;

		msg = "msg[" + ++numMsg + "] :  " + ((MsgToConsole)msgConsole).getMsg() + "\n";
		((MsgToConsole)msgConsole).setMsg(msg);
		
		
		// on a receptionné un message => on doit le passer à l'IHM pour qu'elle l'affiche dans la console systeme

		/**
		 * si le mode verbeux est active, on affiche égalezment les messages dans la console système
		 * Dans le cas contraire, les messages de debug sont tous envoyés vers l'IHM
		 */
		if (VERBOSE_ON)
			System.out.println(((MsgToConsole)msgConsole).getMsg());

		// affichage dans la fenetre de l'IHM dediee aux messages de console
		ihmApplication.affichageConsole((MsgToConsole)msgConsole);
		// affichage dans l'IHM du remplissage de la MQ de la console
		ihmApplication.affichageRemplissageMQ_Console(queueMsg.size());
	}

	
	
	/**
	 * le "main" du thread de console
	 * Des l'arrivee d'un message, celui-ci est depile puis afficher dans la console
	 */
	@Override
	public void run() {
        try {
            while (true) {
                consommer(queueMsg.take()); // attente de l'arrivee d'un produit dans la queue de message
//                Thread.sleep(0); // Pour debug : freiner les consommations des messages
            }
        } catch (InterruptedException ex) {
        }
		
	}



	/**
	 * Obtenir le nom de ce thread
	 */
	@Override
	public String getNom() {
		return nomConsole;
	}

	/**
	 * fixer le nom du thread
	 */
	@Override
	public void setNom(String nom) {
		this.nomConsole = nom;
		
	}

	/**
	 * obtenir le numero du thread de console
	 */
	@Override
	public int getNumero() {
		return numeroProducteur;
	}

}
