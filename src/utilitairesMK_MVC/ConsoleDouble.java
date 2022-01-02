package utilitairesMK_MVC;

import java.util.concurrent.ArrayBlockingQueue;

import viewMVC.IHM;

public class ConsoleDouble extends ConsoleMK {

    public ConsoleDouble(String consumerName,
    		int numero,
    		int priority,
    		ArrayBlockingQueue<MsgToConsole> msgQ,
    		IHM ihmApplication,
    		String adresseIPConsoleDistante) {
    	super(consumerName, numero, priority, msgQ, ihmApplication, adresseIPConsoleDistante);
    	
    	
    }

    /**
     * Pour envoyer un message vers la console situee dans l'IHM.
     * Le message a afficher est place dans la MQ du thread de console.
     * 
     * @param msg
     */
    public void sendMsgToConsole(MsgToConsole msg) {
    	if (super.queueMsg.remainingCapacity() > 1)
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

	
}
