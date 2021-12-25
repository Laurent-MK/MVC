package model;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class TestSemaphore {

    public TestSemaphore() throws InterruptedException {
    	

              
              
      /*        
        ExecutorService executorService = new ThreadPoolExecutor(1, 1, 1000, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>());
    	Future future = executorService.submit(new Runnable() {

    	@Override
    	public void run() {
    		System.out.println("debut tache " + Thread.currentThread().getName());
    		try {
    			Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
    		System.out.println("fin tache");
			}
    	});

         
     	System.out.println("Autre traitement");

    	try {
    		System.out.println("resultat=" + future.get());
    	} catch (InterruptedException e) {
    		e.printStackTrace();
    	} catch (ExecutionException e) {
    		e.printStackTrace();
    	}
    
    	executorService.shutdown();
    	System.out.println("Fin thread principal");
    */
    	
    	
    }
    
    public void Go() throws InterruptedException {
        ExecutorService executorService = new ThreadPoolExecutor(10, 20, 60, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>());

        for (int i = 0; i < 50; i++) {
//        	System.out.println("i = " + i);
          executorService.submit(new Runnable() {
            @Override
            public void run() {
              System.out.println("debut tache " + Thread.currentThread().getName());
              try {
                Thread.sleep(1000);
              } catch (InterruptedException e) {
                e.printStackTrace();
              }
              System.out.println("fin tache");
            }
          });
        }

          System.out.println("Autre traitement");
//          Thread.sleep(2000);

          executorService.shutdown();
          executorService.awaitTermination(300, TimeUnit.SECONDS);

          System.out.println("Fin thread principal");
    	
    }

}
	
