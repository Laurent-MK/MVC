package modelMVC;

import controlerMVC.ControlerTestThread;
import utilitairesMK_MVC.MsgToConsole;
import utilitairesMK_MVC.Mutex;

public class TestMutex implements Runnable, Constantes {
	Mutex m;
	ControlerTestThread ctrl;
	int numConsole;
	private boolean AjouterNumMsg = AJOUTER_NUM_MESSAGE;

	
	/**
	 * Constructeur de la classe
	 * 
	 * @param numConsole
	 * @param ctrl
	 * @param mutex
	 */
	public TestMutex(int numConsole, ControlerTestThread ctrl, Mutex mutex) {
		this.m = mutex;
		this.ctrl = ctrl;
		this.numConsole = numConsole;
	}

	/**
	 * 
	 */
	@Override
	public void run() {
		
		ctrl.getConsole().sendMsgToConsole(new MsgToConsole(numConsole, AjouterNumMsg, Thread.currentThread().getName() + " -> Lancement Thread.\n-> Attente d'entrer dans la ZC"));

		m.mutexGet();	// on prend le mutex si il est disponible, sinon le thread est mis en sommeil
		/*
		 * ------------- debut de l'acces a la zone critique
		 */
		ctrl.getConsole().sendMsgToConsole(new MsgToConsole(numConsole, AjouterNumMsg, "=>----Entree dans la ZONE CRITIQUE DE :\n\t------" + Thread.currentThread().getName() + "-----------"));
		
		for (int i=0; i<NB_BOUCLE_EN_ZONE_CRITIQUE; i++) {
			ctrl.getConsole().sendMsgToConsole(new MsgToConsole(numConsole, AjouterNumMsg, Thread.currentThread().getName() + " est dans la ZC."));

			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				// TODO Bloc catch généré automatiquement
				e.printStackTrace();
			}
			
		}
		/*
		 * ------------- fin de l'acces à la zone critique. Les autres threads vont pouvoir entrer dans celle-ci
		 */
		ctrl.getConsole().sendMsgToConsole(new MsgToConsole(numConsole, AjouterNumMsg, "<==----SORTIE DE ZONE ZONE CRITIQUE DE : " + Thread.currentThread().getName() + "-------"));
		m.mutexRelease();	// on relache le mutex
	}
}
