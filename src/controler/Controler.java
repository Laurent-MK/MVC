package controler;

import java.util.ArrayList;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

/**
 * imports des packages sp�cifiques de l'application
 */
import View.IHM;
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
    private static int nbProdP1 = 4;
    private static int nbProdP2 = 4;
    private static int prioriteP1 = 10;
    private static int prioriteP2= 1;
    
     
    private ArrayList message = new ArrayList<String>();
    
    
    
    public static void main(String[] args) {
    	
    	new Controler(); // lancement du controleur
     	
    	
       	try {
			Thread.sleep(0);	// retard �ventuel avant lancement des autres
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
       	
 /*       
    	//  Cr�ation de la message queue "bloquante"
        BlockingQueue<ProduitText> q = new ArrayBlockingQueue<ProduitText>(5);
        
        // cr�ation des producteurs
        ProducteurMQ producer1 = new ProducteurMQ("P1", 1, delaiDeProductionP1, nbProdP1, 3, q);
        ProducteurMQ producer2 = new ProducteurMQ("P2", 2, delaiDeProductionP2, nbProdP2, 8, q);
        ProducteurMQ producer3 = new ProducteurMQ("P3", 3, delaiDeProductionP2, nbProdP2, 6, q);
        
        // cr�ation des consommateurs
        ConsommateurMQ consumer1 = new ConsommateurMQ("Consommateur 01", 2, q);
        ConsommateurMQ consumer2 = new ConsommateurMQ("Consommateur 02", 1, q);
        ConsommateurMQ consumer3 = new ConsommateurMQ("Consommateur 03", 5, q);
   */     
        
        // d�marrage des threads des producteurs avec retard possible sur le 1er
/*
        new Thread(producer1).start();
       
       	try {
			Thread.sleep(0);	// retard �ventuel avant lancement des autres
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
        new Thread(producer2).start();
        new Thread(producer3).start();
        
        new Thread(consumer1).start();
        new Thread(consumer2).start();
        new Thread(consumer3).start();
 */       
        System.out.println("\t main() terminé !!!\n");
    }

    public void dmdIHMGo(Object nbProducteur, Object nbCosommateur) {
    	        
    	//  Cr�ation de la message queue "bloquante"
        BlockingQueue<ProduitText> q = new ArrayBlockingQueue<ProduitText>(5);
        
        // cr�ation des producteurs
        ProducteurMQ producer1 = new ProducteurMQ("P1", 1, delaiDeProductionP1, nbProdP1, 3, q, message, this);
        ProducteurMQ producer2 = new ProducteurMQ("P2", 2, delaiDeProductionP2, nbProdP2, 8, q, message, this);
        ProducteurMQ producer3 = new ProducteurMQ("P3", 3, delaiDeProductionP2, nbProdP2, 6, q, message, this);
        
        // cr�ation des consommateurs
        ConsommateurMQ consumer1 = new ConsommateurMQ("C01", 2, q, message);
        ConsommateurMQ consumer2 = new ConsommateurMQ("C02", 1, q, message);
        ConsommateurMQ consumer3 = new ConsommateurMQ("C03", 5, q, message);
        
    	message.add("On a cliqué sur le bouton Go !");
    	ihmApplication.affichageConsole(message); // affichage dans la fenêtre de console
    	
    	//message.clear();

        new Thread(producer1).start();
        
       	try {
			Thread.sleep(0);	// retard �ventuel avant lancement des autres
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
        new Thread(producer2).start();
        new Thread(producer3).start();
        
        new Thread(consumer1).start();
        new Thread(consumer2).start();
        new Thread(consumer3).start();

        message.add("ca marche !!!");
    	ihmApplication.affichageConsole(message); // affichage dans la fenêtre de console
    }
    
    public void dmdModelAffichageConsole(String msg){
    	message.add(msg);
    	
    	ihmApplication.affichageConsole(message); // affichage dans la fenêtre de console
    }

    
    
    /*
     * Constructeur : d�marre l'IHM et envoi le controleur en param�tre
     */
    public Controler() {
    	ihmApplication = new IHM(this); // l'IHM recoit le controleur en param�tre
    	ihmApplication.setVisible(true); // affichage de l'IHM
    	
    	message.add("lancement IHM");
    }
}

