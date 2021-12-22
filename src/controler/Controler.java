package controler;

import java.util.ArrayList;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

/**
 * imports des packages sp�cifiques de l'application
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
    private static BlockingQueue<ProduitText> q = new ArrayBlockingQueue<ProduitText>(5);
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
    
    
    
    
    private static BlockingQueue<String> msgQ_Console;
    
    private ArrayList message = new ArrayList<String>();
    
    
    
    public static void main(String[] args) {
    	
        // création de la queue de messages pour afficher dans la console
        msgQ_Console = new ArrayBlockingQueue<String>(100);
        
    	new Controler(); // lancement du controleur
     	
    	
       	try {
			Thread.sleep(0);	// retard �ventuel avant lancement des autres
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
       	
 //      	while(true);
 
       System.out.println("\t main() terminé !!!\n");
    }

 
    
    
    
    
    public void dmdIHMGo(Object nbProducteur, Object nbCosommateur) {
    	        
    	//  Cr�ation de la message queue "bloquante"
//        BlockingQueue<ProduitText> q = new ArrayBlockingQueue<ProduitText>(5);
        
 //       console.sendMsgToConsole("lancement des threads Producteur et consommateur");      
 /*       
        // cr�ation des producteurs
        ProducteurMQ producer1 = new ProducteurMQ("P1", 1, delaiDeProductionP1, nbProdP1, 3, q, message, this);
        ProducteurMQ producer2 = new ProducteurMQ("P2", 2, delaiDeProductionP2, nbProdP2, 8, q, message, this);
        ProducteurMQ producer3 = new ProducteurMQ("P3", 3, delaiDeProductionP2, nbProdP2, 6, q, message, this);
        
        // cr�ation des consommateurs
        ConsommateurMQ consumer1 = new ConsommateurMQ("C01", 2, q, msgQ_Console, this);
        ConsommateurMQ consumer2 = new ConsommateurMQ("C02", 1, q, msgQ_Console, this);
        ConsommateurMQ consumer3 = new ConsommateurMQ("C03", 5, q, msgQ_Console, this);
   */     
    	System.out.println("clic !!!");
    	
    	console.sendMsgToConsole("lancement des threads P et C");

    	/*
        new Thread(producer1).start();
        
       	try {
			Thread.sleep(0);	// retard �ventuel avant lancement des autres
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
 */
       	/*
       	new Thread(producer2).start();
        new Thread(producer3).start();
        
        new Thread(consumer1).start();
        new Thread(consumer2).start();
        new Thread(consumer3).start();
*/

/*
    	C1.run();
    	if (C2.isAlive())
    		C2.run();
    	if (C3.isAlive())
    		C3.run();
  */  	
    	System.out.println("c1 : " + c1.isAlive() + " - c2 : " + c2.isAlive() + " - c3 : " + c3.isAlive() +
    			" - p1 : " + p1.isAlive() + " - p2 : " + p2.isAlive() + " - p3 : " + p3.isAlive());
       	p1.start();
 //      	p2.start();
 //      	p3.start();
       	
       	c1.start();
 //      	c2.start();
 //      	c3.start();
       	
    	System.out.println("=====================APRES les start()\n" + "c1 : " + c1.isAlive() + " - c2 : " + c2.isAlive() + " - c3 : " + c3.isAlive() +
    			" - p1 : " + p1.isAlive() + " - p2 : " + p2.isAlive() + " - p3 : " + p3.isAlive());
       	
       	System.out.println("Après les start() clic !!!");
    }
    
    
    
    
    
    public void dmdModelAffichageConsole(String msg){
    	console.sendMsgToConsole(msg);
    }

    
    
    
    
    
    /*
     * Constructeur : d�marre l'IHM et envoi le controleur en param�tre
     */
    public Controler() {
        
        ihmApplication = new IHM(this); // l'IHM recoit le controleur en param�tre
    	ihmApplication.setVisible(true); // affichage de l'IHM
         
        // lancement du thread de gestion de la console
        console = new ConsoleMK("Console", 5, msgQ_Console, ihmApplication);
        new Thread(console).start();
        
        console.sendMsgToConsole("création des threads Producteur et consommateur");      
        console.sendMsgToConsole("lancement IHM");
    	console.sendMsgToConsole("création du thread de console");
    	
        // cr�ation des producteurs
        producer1 = new ProducteurMQ("P1", 1, delaiDeProductionP1, nbProdP1, 3, q, message, this);
        producer2 = new ProducteurMQ("P2", 2, delaiDeProductionP2, nbProdP2, 8, q, message, this);
        producer3 = new ProducteurMQ("P3", 3, delaiDeProductionP2, nbProdP2, 6, q, message, this);
        
        // cr�ation des consommateurs
        consumer1 = new ConsommateurMQ("C01", 2, q, msgQ_Console, this);
        consumer2 = new ConsommateurMQ("C02", 1, q, msgQ_Console, this);
        consumer3 = new ConsommateurMQ("C03", 5, q, msgQ_Console, this);
        
        p1 = new Thread(producer1);
        p2 = new Thread(producer2);
        p3 = new Thread(producer3);

        c1 = new Thread(consumer1);
        c2 = new Thread(consumer2);
        c3 = new Thread(consumer3);
        
        
    }
}

