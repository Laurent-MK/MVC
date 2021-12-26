package model;

import controler.Controler;
import utilitairesMK.SemaphoreMK;


/**
 * META KONULSTING
 * 
 * @author balou
 *
 */
public class TestSemaphore implements Runnable, Constantes {

	private int numConsole;
	private Controler ctrl;
	private SemaphoreMK sem;

	
	
	/**
	 * Constructeur
	 * 
	 */
	public TestSemaphore(int numConsole, Controler ctrl, SemaphoreMK sem) {
		
		this.sem = sem;
		this.numConsole = numConsole;
		this.ctrl = ctrl;
	}


	/**
	 * methode "main()" du thread porteur d'un objet de cette classe
	 */
	@Override
	public void run()  {

		ctrl.getConsole().sendMsgToConsole(new MsgToConsole(numConsole, Thread.currentThread().getName() + " -> Lancement Thread."));
		ctrl.getConsole().sendMsgToConsole(new MsgToConsole(numConsole, Thread.currentThread().getName() + "-> Attente d'entrer dans la ZC"));

		sem.semGet(1);	// on prend le semaphore si il est disponible, sinon le thread est mis en sommeil
		/*
		 * ------------- debut de la ZONE CRITIQUE protegee par le semaphore
		 */
		ctrl.getConsole().sendMsgToConsole(new MsgToConsole(numConsole, "--------ZONE CRITIQUE DE : " + Thread.currentThread().getName() + "-----------"));
		ctrl.getConsole().sendMsgToConsole(new MsgToConsole(numConsole, "=> Entree dans la ZC - " + Thread.currentThread().getName()));
		
		for (int i=0; i<10; i++) {
			ctrl.getConsole().sendMsgToConsole(new MsgToConsole(numConsole, Thread.currentThread().getName() + " est dans la ZC."));

			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				// TODO Bloc catch généré automatiquement
				e.printStackTrace();
			}
			
		}
		/*
		 * ------------- fin de la ZONE CRITIQUE protegee par le semaphore
		 */		
		sem.semRelease(1);	// on relache le semaphore
		ctrl.getConsole().sendMsgToConsole(new MsgToConsole(numConsole, Thread.currentThread().getName() + "=> Sortie de la ZC - " + "\n"));
	}
	
	
}
