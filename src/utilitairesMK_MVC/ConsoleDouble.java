package utilitairesMK_MVC;

import java.util.concurrent.ArrayBlockingQueue;

import viewMVC.IHM;
import viewMVC.IHM_Test_Thread;


/**
 * META KONSULTING
 * 
 * Classe de gestion des envois de message vers la console locale et la console distante
 * 
 * @author balou
 *
 */
public class ConsoleDouble extends ConsoleMK {

	private ClientSocketTCP socketClient = null;
	
    public ConsoleDouble(String consumerName,
    		int numero,
    		int priority,
    		ArrayBlockingQueue<MsgToConsole> msgQ,
    		IHM ihmApplication,
    		String adresseIPConsoleDistante,
    		ClientSocketTCP socketClient) {
    	
    	super(consumerName, numero, priority, msgQ, ihmApplication, adresseIPConsoleDistante);
    	
    	this.socketClient = socketClient;
    	
    }

    
    /**
     * Pour envoyer un message vers la console situee dans l'IHM.
     * Le message a afficher est place dans la MQ du thread de console.
     * De plus, si la case a cocher est active dans l'IHM, on envoi le meme msg vers la console distante
     * 
     * @param msg
     */
    public void sendMsgToConsole(MsgToConsole msg) {
    	
    	// envoi vers la console locale
    	super.sendMsgToConsole(msg);
    	
    	/**
    	 *  envoi vers la console distante. Le msg va etre envoye dans la MQ du thread qui gere les transferts
    	 *  vers la console distante. C'est ce thread qui va effectivement envoyer le message vers le serveur
    	 */
    	if (((IHM_Test_Thread)(super.ihmApplication)).chckbxConnexion.isSelected())
    		socketClient.sendMsgToServer(msg);  		    	
    	}

	
}
