package controler;

import java.util.ArrayList;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

/**
 * imports des packages spï¿½cifiques de l'application
 */
import View.IHM;
import model.ConsoleMK;
import model.ConsommateurMQ;
import model.ProducteurMQ;
import model.ProduitText;



/**
 * Classe controleur de l'application de test des messages queue bloquantes entre plusieurs threads
 * 
 * @author balou
 *
 */

public class Controler {
	private static IHM ihmApplication;
    private static int delaiDeProductionP1 = 0;
    private static int delaiDeProductionP2 = 0;
    private static int nbProdP1 = 4000000;
    private static int nbProdP2 = 4;
    private static int prioriteP1 = 10;
    private static int prioriteP2= 1;
    
    private ConsoleMK console;
  
    /**
     * 
     */
    private static BlockingQueue<ProduitText> msgQProduit = new ArrayBlockingQueue<ProduitText>(5);
    private ProducteurMQ producer1;
    private ProducteurMQ producer2;
    private ProducteurMQ producer3;
    private ConsommateurMQ consumer1;
    private ConsommateurMQ consumer2;
    private ConsommateurMQ consumer3;
    
    private Thread p1;
    private Thread p2;
    private Thread p3;
    private Thread c1;
    private Thread c2;
    private Thread c3;
    
    private Thread listThreadP[];	// tableau des threads producteurs
    private Thread listThreadC[];	// tableau des threads consommateurs
    
    private ArrayList<String> listeThreads = new ArrayList<>();
    
    
    
    private static BlockingQueue<String> msgQ_Console;
    
    
    
    public static void main(String[] args) {
    	
        // creation de la queue de messages pour afficher dans la console
        msgQ_Console = new ArrayBlockingQueue<String>(100);
        
    	new Controler(); // lancement du controleur
     	
        System.out.println("main() termine !!!\n");
    }

 
    
    
    
    
    public void dmdIHMGo(Object nbProducteur, Object nbCosommateur) {
    	String msg;
    	 	
    	console.sendMsgToConsole("lancement des threads P et C");

      	msg = "c1 : " + listThreadC[0].isAlive() + " - c2 : " + listThreadC[1].isAlive() + " - c3 : " + listThreadC[2].isAlive() +
    			" - p1 : " + listThreadP[0].isAlive() + " - p2 : " + listThreadP[1].isAlive() + " - p3 : " + listThreadP[2].isAlive();
      	
    	System.out.println(msg);
    	
/*    	msg = "c1 : " + c1.isAlive() + " - c2 : " + c2.isAlive() + " - c3 : " + c3.isAlive() +
    			" - p1 : " + p1.isAlive() + " - p2 : " + p2.isAlive() + " - p3 : " + p3.isAlive();
*/    	listeThreads.add(msg);
    	this.ihmApplication.affichageThreads(listeThreads);


    	// lancement de threads
    	for (int i =  0 ; i < listThreadP.length ; i++) {
    		listThreadP[i].start() ;
    	}

    	// lancement de threads
    	for (int i =  0 ; i < listThreadC.length ; i++) {
    		listThreadC[i].start() ;
    	}
    	
    	/*   	
       	p1.start();
      	p2.start();
      	p3.start();
       	
       	c1.start();
      	c2.start();
      	c3.start();
*/
      	msg = "c1 : " + listThreadC[0].isAlive() + " - c2 : " + listThreadC[1].isAlive() + " - c3 : " + listThreadC[2].isAlive() +
    			" - p1 : " + listThreadP[0].isAlive() + " - p2 : " + listThreadP[1].isAlive() + " - p3 : " + listThreadP[2].isAlive();

      	/*msg = "c1 : " + listThreadP[0].isAlive() + " - c2 : " + listThreadP[1].isAlive() + " - c3 : " + listThreadP[2].isAlive() +
    			" - p1 : " + p1.isAlive() + " - p2 : " + p2.isAlive() + " - p3 : " + p3.isAlive();
      	*/
      	listeThreads.clear();
      	listeThreads.add(msg);
    	this.ihmApplication.affichageThreads(listeThreads);
      	
    	System.out.println(msg);
    }
    
    
     
    
    /*
     * Constructeur : dï¿½marre l'IHM et envoi le controleur en paramï¿½tre
     */
    public Controler() {
        
        ihmApplication = new IHM(this);		// l'IHM recoit le controleur en parametre
    	ihmApplication.setVisible(true);	// affichage de l'IHM

    	int nbProducteur = ihmApplication.getNbThreadP();
        int nbConsommateur = ihmApplication.getNbThreadC();
    	
        // lancement du thread de gestion de la console
        console = new ConsoleMK("Console", 5, msgQ_Console, ihmApplication);
        new Thread(console).start();
        
        console.sendMsgToConsole("crÃ©ation des threads Producteur et consommateur");      
        console.sendMsgToConsole("lancement IHM");
    	console.sendMsgToConsole("crÃ©ation du thread de console");

    	/**
    	 *  construction de la liste des producteurs
    	 */
    	listThreadP =  new Thread[nbProducteur];

    	 // création des threads Producteurs
    	 for (int i =  0 ; i < listThreadP.length ; i++) {
    		 listThreadP[i] =  new Thread(new ProducteurMQ("P0"+i+1, 1, delaiDeProductionP1, nbProdP1, 3, msgQProduit, console)) ;
    	} 	

    	 /**
      	  *  construction de la liste des consommateurs
       	  */
     	listThreadC =  new Thread[nbConsommateur];

     	 // création des threads Producteurs
     	 for (int i =  0 ; i < listThreadC.length ; i++) {
     		 listThreadC[i] =  new Thread(new ConsommateurMQ("C0"+i+1, 2, msgQProduit, msgQ_Console, this, console));
     	} 	
    	
/*    	
        // crï¿½ation des producteurs
        producer1 = new ProducteurMQ("P1", 1, delaiDeProductionP1, nbProdP1, 3, msgQProduit, console);
        producer2 = new ProducteurMQ("P2", 2, delaiDeProductionP2, nbProdP2, 8, msgQProduit, console);
        producer3 = new ProducteurMQ("P3", 3, delaiDeProductionP2, nbProdP2, 6, msgQProduit, console);
        
        // crï¿½ation des consommateurs
        consumer1 = new ConsommateurMQ("C01", 2, msgQProduit, msgQ_Console, this, console);
        consumer2 = new ConsommateurMQ("C02", 1, msgQProduit, msgQ_Console, this, console);
        consumer3 = new ConsommateurMQ("C03", 5, msgQProduit, msgQ_Console, this, console);
        
        p1 = new Thread(producer1);
        p2 = new Thread(producer2);
        p3 = new Thread(producer3);

        c1 = new Thread(consumer1);
        c2 = new Thread(consumer2);
        c3 = new Thread(consumer3);
        
  */      
        
        
        /***
         * boucle de surveillance des threads et affichage de leur etat dans l'IHM
         */
        while(true) {
        	try {
				Thread.sleep(1000);				// on dort durant 1 seconde
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
        	
          	String msg = "c1 : " + listThreadC[0].isAlive() + " - c2 : " + listThreadC[1].isAlive() + " - c3 : " + listThreadC[2].isAlive() +
        			" - p1 : " + listThreadP[0].isAlive() + " - p2 : " + listThreadP[1].isAlive() + " - p3 : " + listThreadP[2].isAlive();
          	
          	listeThreads.clear();
          	listeThreads.add(msg);
        	this.ihmApplication.affichageThreads(listeThreads);
        }
        
    }
}

