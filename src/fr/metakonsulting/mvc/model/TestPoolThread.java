package fr.metakonsulting.mvc.model;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import fr.metakonsulting.mvc.controler.Controler;
import fr.metakonsulting.mvc.utilitaires.ConsoleMK;
import fr.metakonsulting.mvc.utilitaires.MsgToConsole;



public class TestPoolThread implements Constantes, Runnable {

	private ConsoleMK console;
	private int numConsole = NUM_CONSOLE_CONSOLE;
	private boolean AjouterNumMsg = AJOUTER_NUM_MESSAGE;

	
	/**
	 * Constructeur
	 * 
	 * @param controleur
	 * @throws InterruptedException
	 */
    public TestPoolThread(Controler controleur, int numConsole) throws InterruptedException {
    	this.console = controleur.getConsole();
    	this.numConsole = numConsole;
    }
    
    /**
     * methode d'execution du thread porteur de l'objet de cette classe
     */
    public void run() {
    	try {
			goTestPoolThread();	// lancement du "main()" du thread
		} catch (InterruptedException e) {
			// TODO Bloc catch généré automatiquement
			e.printStackTrace();
		}
    }
    
    /**
     * methode de lancement du pool de threads
     * 
     * @throws InterruptedException
     */
    private void goTestPoolThread() throws InterruptedException {
		if (VERBOSE_ON)
			System.out.println(Thread.currentThread() + " : d�but du test d'un pool de threads");

		ExecutorService executorService = new ThreadPoolExecutor(5, 10, 60, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>());

        for (int i = 0; i < 50; i++) {
			if (VERBOSE_ON)
				System.out.println(Thread.currentThread().getName() + " : i = " + (i+1));
        	
        	executorService.submit(new Runnable() {
        		@Override
        		public void run() {        			
        			String msg =  Thread.currentThread().getName() + " : debut tache";
					console.sendMsgToConsole(new MsgToConsole(numConsole, AjouterNumMsg, msg));
					if (VERBOSE_ON)
						System.out.println(msg);

        			try {
        				Thread.sleep(500);
        				} catch (InterruptedException e) {
        					e.printStackTrace();
        					}
        			msg = Thread.currentThread().getName() + " : fin de tache";
					console.sendMsgToConsole(new MsgToConsole(numConsole, AjouterNumMsg, msg));
					if (VERBOSE_ON)
						System.out.println(msg);
        			}
        		});
        	}

        console.sendMsgToConsole(new MsgToConsole(numConsole, AjouterNumMsg, Thread.currentThread() + " : Autre traitement\n"));
		if (VERBOSE_ON)
			System.out.println(Thread.currentThread() + " : Autre traitement");
		
        executorService.shutdown();
        executorService.awaitTermination(300, TimeUnit.SECONDS);

        console.sendMsgToConsole(new MsgToConsole(numConsole, AjouterNumMsg, "Fin thread principal\n"));
		if (VERBOSE_ON)
			System.out.println(Thread.currentThread() + " : fin du test d'un pool de threads");
        }

}
	
