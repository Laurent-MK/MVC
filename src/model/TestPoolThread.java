package model;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import controler.Controler;
import utilitairesMK.ConsoleMK;



public class TestPoolThread implements Constantes {

	private ConsoleMK console;
	
	/**
	 * Constructeur
	 * 
	 * @param controleur
	 * @throws InterruptedException
	 */
    public TestPoolThread(Controler controleur) throws InterruptedException {
    	this.console = controleur.getConsole();
    }
    
    
    /**
     * methode de lancement du pool de threads
     * 
     * @throws InterruptedException
     */
    public void Go() throws InterruptedException {
        ExecutorService executorService = new ThreadPoolExecutor(10, 40, 60, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>());

        for (int i = 0; i < 50; i++) {
        	executorService.submit(new Runnable() {
        		@Override
        		public void run() {
        			String msg = "debut tache " + Thread.currentThread().getName() + "\n";
					console.sendMsgToConsole(msg);
					if (VERBOSE_ON)
						System.out.print(msg);

        			try {
        				Thread.sleep(1000);
        				} catch (InterruptedException e) {
        					e.printStackTrace();
        					}
        			msg = "fin de tache : " + Thread.currentThread().getName() + "\n";
					console.sendMsgToConsole(msg);
					if (VERBOSE_ON)
						System.out.print(msg);
        			}
        		});
        	}

        console.sendMsgToConsole("Autre traitement\n");
		if (VERBOSE_ON)
			System.out.println("Autre traitement");
		
        executorService.shutdown();
        executorService.awaitTermination(300, TimeUnit.SECONDS);

        console.sendMsgToConsole("Fin thread principal\n");
		if (VERBOSE_ON)
			System.out.println("Fin thread principal");
        }

}
	
