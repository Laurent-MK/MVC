package controler;

import java.util.ArrayList;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Semaphore;

import View.ConsoleMK;
/**
 * imports des packages specifiques de l'application
 */
import View.IHM;
import model.ConsommateurMQ;
import model.Constantes;
import model.ProducteurMQ;
import model.ProduitText;



/**
 * Classe controleur de l'application de test des messages queue bloquantes entre plusieurs threads
 * 
 * @author balou
 *
 */

public class Controler implements Constantes {
	private static IHM ihmApplication;
    private static int nbProd = 4000000;

    
    /**
     * propriétés utilisees pour gerer les threads : consommateur et producteur
     */
    private Thread listThreadP[];					// tableau des threads producteurs
    private ProducteurMQ listeProducteurMQ[];		// tableau des objets abritants les threads producteurs
    private Thread listThreadC[];					// tableau des threads consommateurs
    private ConsommateurMQ listeConsommateurMQ[];	// tableau des objets abritants les threads consommateurs
    private static BlockingQueue<ProduitText> msgQProduit = new ArrayBlockingQueue<ProduitText>(TAILLE_MESSAGE_Q_PC);	// queue de message utilisee par les threads producteurs et consommateurs
    
    // liste utilisee pour afficher l'etat des threads dans l'IHM
    private ArrayList<String> listeThreads = new ArrayList<>();

    /**
     *  proprietes pour la gestion des affichages dans la console
     */
    private ConsoleMK console;									// l'objet pour manipuler la console
    private static ArrayBlockingQueue<String> msgQ_Console;		// queue de message utilisee pour les envois de messages dans le console
    
    private Semaphore sem;
    
    
    
    /**
     * D�marrage de l'application
     * 
     * @param args
     * @throws InterruptedException 
     */
    public static void main(String[] args) throws InterruptedException {
    	
        // creation de la queue de messages pour afficher dans la console
        msgQ_Console = new ArrayBlockingQueue<String>(TAILLE_MSG_Q_CONSOLE);
        
    	new Controler(); // lancement du controleur
     	
        System.out.println("main() termine !!!\n");
    }

 
    /**
     * demande d'affichage de l'etat des threads dans la zone prevue par l'IHM
     */
    public void afficheEtatThreads() {
        String msg = "";	// pour la cr�ation du message
        
    	listeThreads.clear();	// effacement de la liste contenant l'�tat des threads
      	
    	/**
    	 * construction du message indiquant l'etat de vie des threads
    	 */
       	listeThreads.add("====================================");
       	listeThreads.add("Etat des threads Consommateur");
       	listeThreads.add("\n");

    	/*
         *  on commence par l'etat des threads "Consommateur"
         */
       	for (int i=0; i <listThreadC.length ; i++) {
   		msg = "[" + this.listeConsommateurMQ[i].getNom() + "]." + listThreadC[i].getName() + " " + listThreadC[i].isAlive() + " ";
       	listeThreads.add(msg);	// ajout du message dans la liste
       	}
       	
       	listeThreads.add("\n");
       	listeThreads.add("\n");
       	listeThreads.add("====================================");
       	listeThreads.add("Etat des threads Producteur");       	
       	listeThreads.add("\n");

        /*
         *  on passe � l'etat des threads "Producteur"
         */
       	msg = "";		// raz du message
    	for (int i=0; i <listThreadP.length ; i++) {
    		msg = "[" + this.listeProducteurMQ[i].getNom() + "]." +  listThreadP[i].getName() + " " + listThreadP[i].isAlive() + " ";
          	listeThreads.add(msg);	// ajout du message dans la liste
    	}
    
    	// demande d'affichage dans l'IHM
    	this.ihmApplication.affichageThreads(listeThreads);     	
    }
    
    
    
    
    /**
     * methode appelee sur le clic du bouton "GO"
     * @throws InterruptedException 
     */
    public void dmdIHMGo() throws InterruptedException {
    	String msg = "lancement des threads P et C";
  
    	console.sendMsgToConsole(msg);
    	this.ihmApplication.affichageThreads(msg);

    	// lancement des threads producteurs
    	for (int i =  0 ; i < listThreadP.length ; i++) {
    		listThreadP[i].start() ;
    	}

    	// lancement de threads consommateurs
    	for (int i =  0 ; i < listThreadC.length ; i++) {
    		listThreadC[i].start() ;
    	}
    	
    	afficheEtatThreads();
    }
    
    
    /**
     *  methode appelee lors du clic sur le bouton de creation des threads faite par l'IHM
     * @throws InterruptedException 
     */
    public void dmdIHMCreationThread() throws InterruptedException {

    	int nbProducteur = ihmApplication.getNbThreadP();
        int nbConsommateur = ihmApplication.getNbThreadC();
        
        
        console.sendMsgToConsole("creation des threads Producteur et consommateur");      
        console.sendMsgToConsole("lancement IHM");
 
    	/**
    	 *  construction de la liste des producteurs
    	 */
    	listThreadP =  new Thread[nbProducteur];
    	listeProducteurMQ = new ProducteurMQ[nbProducteur];

    	 // creation des threads Producteurs
    	 for (int i =  0 ; i < listThreadP.length ; i++) {
    		 listeProducteurMQ[i] = new ProducteurMQ("P"+(i+1), i+1, FREQ_PRODUCTION, nbProd, PRIORITE_PRODUCTEUR, msgQProduit, console);
    		 listThreadP[i] =  new Thread(listeProducteurMQ[i]);
    	} 	

    	 /**
      	  *  construction de la liste des consommateurs
       	  */
     	listThreadC =  new Thread[nbConsommateur];
    	listeConsommateurMQ = new ConsommateurMQ[nbConsommateur];

     	 // creation des threads Consommateurs
     	 for (int i =  0 ; i < listThreadC.length ; i++) {
    		 listeConsommateurMQ[i] = new ConsommateurMQ("C"+(i+1), i+1, PRIORITE_CONSOMMATEUR, msgQProduit, msgQ_Console, this, console);
     		 listThreadC[i] =  new Thread(listeConsommateurMQ[i]);
     	}
     	 
     	afficheEtatThreads();	// affiche dans l'IHM l'etat des threads
    }
    
    
    
    /*
     * Constructeur : demarre l'IHM et envoit le controleur en param�tre
     */
    public Controler() throws InterruptedException {

    	sem = new Semaphore(1);
         
        ihmApplication = new IHM(this, sem);		// l'IHM recoit le controleur en parametre
    	ihmApplication.setVisible(true);	// affichage de l'IHM


    	
    	// lancement du thread de gestion de la console
        console = new ConsoleMK("Console", NUMERO_CONSOLE, PRIORITE_CONSOLE, msgQ_Console, ihmApplication, sem);
        new Thread(console).start();
        
    	console.sendMsgToConsole("creation et lancement du thread de console");


        /***
         * boucle de surveillance des threads et affichage de leur etat dans l'IHM
         */
        while(true) {

            try {
				Thread.sleep(FREQ_POLLING_THREADS);	// on s'endort durant un certain temps
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
            if (listeConsommateurMQ != null)
            	afficheEtatThreads();     		// affiche dans l'IHM l'état des threads que si ils existent 	
        }
        
    }
}

