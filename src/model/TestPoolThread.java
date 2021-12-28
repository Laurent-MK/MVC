package model;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import controler.Controler;
import utilitairesMK.ConsoleMK;
import utilitairesMK.MsgToConsole;



public class TestPoolThread implements Constantes, Runnable {

	private ConsoleMK console;
	private int numConsole = NUM_CONSOLE_CONSOLE;
	
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
        ExecutorService executorService = new ThreadPoolExecutor(5, 10, 60, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>());

        for (int i = 0; i < 50; i++) {
        	executorService.submit(new Runnable() {
        		@Override
        		public void run() {
        			String msg = "debut tache " + Thread.currentThread().getName();
					console.sendMsgToConsole(new MsgToConsole(numConsole, msg));
					if (VERBOSE_ON)
						System.out.print(msg);

        			try {
        				Thread.sleep(500);
        				} catch (InterruptedException e) {
        					e.printStackTrace();
        					}
        			msg = "fin de tache : " + Thread.currentThread().getName();
					console.sendMsgToConsole(new MsgToConsole(numConsole, msg));
					if (VERBOSE_ON)
						System.out.print(msg);
        			}
        		});
        	}

        console.sendMsgToConsole(new MsgToConsole(numConsole, "Autre traitement\n"));
		if (VERBOSE_ON)
			System.out.println("Autre traitement");
		
        executorService.shutdown();
        executorService.awaitTermination(300, TimeUnit.SECONDS);

        console.sendMsgToConsole(new MsgToConsole(numConsole, "Fin thread principal\n"));
		if (VERBOSE_ON)
			System.out.println("Fin thread principal");
        }

}
	
