package model;

import controler.ControlerTestThread;
import utilitairesMK.SemaphoreMK;
import utilitairesMK.MsgToConsole;


/**
 * META KONULSTING
 * 
 * @author balou
 *
 */
public class TestSemaphore implements Runnable, Constantes {

	private int numConsole;
	private ControlerTestThread ctrl;
	private SemaphoreMK sem;

	
	
	/**
	 * Constructeur
	 * 
	 */
	public TestSemaphore(int numConsole, ControlerTestThread ctrl, SemaphoreMK sem) {
		
		this.sem = sem;
		this.numConsole = numConsole;
		this.ctrl = ctrl;
	}


	/**
	 * methode "main()" du thread porteur d'un objet de cette classe
	 */
	@Override
	public void run()  {

		ctrl.getConsole().sendMsgToConsole(new MsgToConsole(numConsole, Thread.currentThread().getName() + " -> Lancement Thread.\n-> Attente d'entrer dans la ZC"));

		sem.semGet(1);	// on prend le semaphore si il est disponible, sinon le thread est mis en sommeil
		/*
		 * ------------- debut de l'acces a la zone protegee par le semaphore
		 */
		ctrl.getConsole().sendMsgToConsole(new MsgToConsole(numConsole, "=>----Entree dans la ZONE PROTEGEE DE :\n\t------" + Thread.currentThread().getName() + "-----------"));
		
		for (int i=0; i<NB_BOUCLE_EN_ZONE_CRITIQUE; i++) {
			ctrl.getConsole().sendMsgToConsole(new MsgToConsole(numConsole, Thread.currentThread().getName() + " est dans la zone protegee."));

			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				// TODO Bloc catch généré automatiquement
				e.printStackTrace();
			}
			
		}
		/*
		 * ------------- fin de l'acces à la zone protegee par le semaphore
		 */		
		ctrl.getConsole().sendMsgToConsole(new MsgToConsole(numConsole, "<==----SORTIE DE ZONE ZONE PROTEGEE DE : " + Thread.currentThread().getName() + "-------"));
		sem.semRelease(1);	// on relache le semaphore
	}
	
	
}
