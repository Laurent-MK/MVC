package controler;

import java.util.ArrayList;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import model.ConsommateurMQ;
import model.Constantes;
import model.ProducteurMQ;
import model.ProduitText;
import model.TestMutex;
import model.TestPoolThread;
import model.TestSemaphore;
import utilitairesMK.ConsoleMK;
import utilitairesMK.Mutex;
import view.IHM;
import utilitairesMK.SemaphoreCpt;
import utilitairesMK.MsgToConsole;



/**
 * META KONSULTING
 * 
 * Classe controleur de l'application de test des messages queue bloquantes entre plusieurs threads
 * 
 * @author balou
 *
 */

public class Controler implements Constantes {
	private IHM ihmApplication;

    
    /**
     * propriétés utilisees pour gerer les threads : consommateur et producteur
     */
    private Thread listThreadP[];					// tableau des threads producteurs
    private ProducteurMQ listeProducteurMQ[];		// tableau des objets abritants les threads producteurs
    private Thread listThreadC[];					// tableau des threads consommateurs
    private ConsommateurMQ listeConsommateurMQ[];	// tableau des objets abritants les threads consommateurs
    private static BlockingQueue<ProduitText> msgQProduit = new ArrayBlockingQueue<ProduitText>(TAILLE_MESSAGE_Q_PC);	// queue de message utilisee par les threads producteurs et consommateurs
    
    private TestSemaphore listeTestSemaphore[];		// tableau des objets abritants les threads de test des semaphores
    private Thread listThreadTestSem[];					// tableau des threads recevant les objets de test des semaphores

    
    
    /**
     *  proprietes pour la gestion des affichages dans la console
     */
    private ConsoleMK console;	// l'objet pour manipuler la console
    private static ArrayBlockingQueue<MsgToConsole> msgQ_Console;	// queue de message utilisee pour les envois de messages dans la console
    
    private Mutex mutexSynchroIHM_Controleur;	// Mutex de synchronistion du Controleur et de l'IHM lors du démarrage de l'appli
    
    
    
    /**
     * D�marrage de l'application
     * 
     * @param args
     * @throws InterruptedException 
     */
    public static void main(String[] args) throws InterruptedException {

    	new Controler(); // lancement du controleur
     	
        System.out.println("main() termine !!!\n");
    }

 
    public ConsoleMK getConsole() {
    	return console;
    }
    
    
    /**
     * demande d'affichage de l'etat des threads dans la zone prevue par l'IHM
     */
    private void afficheEtatThreads() {
        String msg = "";	// pour la cr�ation du message
        // liste utilisee pour afficher l'etat des threads dans l'IHM
        ArrayList<String> listeEtatThreads = new ArrayList<>();
    	listeEtatThreads.clear();	// effacement de la liste contenant l'�tat des threads

    	/**
    	 * construction du message indiquant l'etat de vie des threads
    	 */
       	listeEtatThreads.add("=======================");
       	listeEtatThreads.add("Threads Consommateur " + "\n ---> nbConso totale = " + this.listeConsommateurMQ[0].getNbConsoTotale() + "\n");

    	/*
         *  on commence par l'etat des threads "Consommateur"
         */
       	for (int i=0; i <listThreadC.length ; i++) {
       		msg = this.listeConsommateurMQ[i].getNom()
       				+ "."
       				+ listThreadC[i].getName()
       				+ "["
       				+ listThreadC[i].isAlive()
       				+ "]"
       				+ ".prio["
       				+ listThreadC[i].getPriority()
       				+ "].nbConso : "
       				+ this.listeConsommateurMQ[i].getNbConsoRealisees();
       		
       		listeEtatThreads.add(msg);	// ajout du message dans la liste
       	}
       	
       	listeEtatThreads.add("\n");
       	listeEtatThreads.add("=======================");
       	listeEtatThreads.add("Threads Producteur " + "\n ---> nbProd totale = "  + "NbProd = " + this.listeProducteurMQ[0].getNbProdTotale() + "\n");       	

        /*
         *  on passe a l'etat des threads "Producteur"
         */
       	msg = "";		// raz du message
		
		for (int i=0; i <listThreadP.length ; i++) {
    		
    		msg = this.listeProducteurMQ[i].getNom()
    				+ "."
    				+  listThreadP[i].getName()
    				+ "["
    				+ listThreadP[i].isAlive()
    				+ "]"
    				+ ".nbProd : "
    				+ listeProducteurMQ[i].getNbProdReal();
          	
    		listeEtatThreads.add(msg);	// ajout du message dans la liste
    	}
    
    	// demande d'affichage dans l'IHM
    	this.ihmApplication.affichageEtatThreads(listeEtatThreads);
    }
    
    
    /**
     * methode appelee sur le clic du bouton "GO"
     * @throws InterruptedException 
     */
    public void dmdIHMGo() throws InterruptedException {

    	String msg = "lancement des threads P et C";
    	MsgToConsole msgToConsole = new MsgToConsole(NUM_CONSOLE_CONSOLE, msg);
  
    	console.sendMsgToConsole(msgToConsole);

    	// lancement des threads producteurs
    	for (int i =  0 ; i < listThreadP.length ; i++) {
    		listThreadP[i].start() ;
    	}

    	// lancement de threads consommateurs
    	for (int i =  0 ; i < listThreadC.length ; i++) {
    		listThreadC[i].start() ;
    	}
    	
    	afficheEtatThreads();	// affiche l'etat des thread producteur et consommateur
    }
    
    
    /**
     * procedure de test des semaphores
     * On lance autant de threads que demande dans l'IHM puis chacun d'eux va tenter de rentrer
     * dans la zone protegee. C'est le nbr de jetons qui donnera le nbr de thread a acceder
     * en meme temps a la ressource protegee
     */
    public void dmdIHMLanceTestSem(int nbrJetons, int nbrThread, int nbCycles) {
    	
    	SemaphoreCpt sem = new SemaphoreCpt(nbrJetons);	// création du semaphore avec le nbr de jetons passe en parametre
    	
    	listeTestSemaphore =  new TestSemaphore[nbrThread];	// tableau des objets de test
    	listThreadTestSem = new Thread[nbrThread];			// tableau des threads d'execution

    	for (int j=0; j<nbCycles; j++) {

    		for (int i=0; i < nbrThread; i++) {
    			listeTestSemaphore[i] = new TestSemaphore(NUM_CONSOLE_TEST_SEMAPHORE, this, sem); // creation de l'objet de test des semaphores
    			listThreadTestSem[i] = new Thread(listeTestSemaphore[i]);	// création du thread d'acceuil des objets de test des semaphores
    		}

    		/**
        	 * lancement des threads concurrents sur l'acces à la ressource protegee par le semaphore
        	 */
        	for (int i=0; i<nbrThread; i++) {
        		listThreadTestSem[i].start();
        	}    		
    	} // fin sur nbCycles   	
    }
   
    /**
     * porcedure de test des Mutex
     * On lance autant de threads que demande dans l'IHM puis chacun d'eux va tenter de rentrer
     * dans la zone critique. A tout instant, un seul thread sera present dans la zone critique.
     * 
     */
    public void dmdIHMLanceTestMutex(int nbCycles, int nbrThread) {

    	Mutex mutexDeTest= new Mutex(MUTEX_CREE_LIBRE);
    	TestMutex listeTstMutex[] = new TestMutex[nbrThread];	// tableau des objets de test du mutex
    	Thread threadTestMutex[] = new Thread[nbrThread];		// tableau des threads d'execution des objets de test

    	for (int j=0; j<nbCycles; j++) {	// on executera le test autant de fois que demande

    		for (int i=0; i < nbrThread ; i++) {
    			listeTstMutex[i] = new TestMutex(NUM_CONSOLE_TEST_MUTEX, this, mutexDeTest);	// creation de l'objet de test d'acces a un mutex
    			threadTestMutex[i] = new Thread(listeTstMutex[i]);	// creation du thread d'execution de l'objet de test
    		}
   	   	
    		/**
    	 	* lancement des threads concurrents sur l'acces à la zone critique
    	 	*/
    		for (int i=0; i < nbrThread ; i++) {
    			threadTestMutex[i].start();
    		}
    	} // fin sur nbCycles
    }
 
    
    
    
    /**
     * methode de creation d'un thread de test des pools de threads
     * on cree un objet de type TestPoolThread que l'on associe a un thread.
     * Cela empeche de bloquer l'IHM pendant le test du pool de thread
     * Une fois le thread cree, on le lance.
     * 
     */
    public void dmdIHMLanceTestPool() {
    	Thread threadControlPoolThread;	// le thread de lancement du pool de thread 	
		TestPoolThread poolThread;		// L'objet de test des pools de threads
		
		try {
			poolThread = new TestPoolThread(this, NUM_CONSOLE_TEST_POOL);	// creation d'un objet de test des pools de threads
			threadControlPoolThread = new Thread(poolThread);	// creation d'un thread qui va "exécuter" le code run de l'objet de test
			threadControlPoolThread.start();	// lancement du thread de test (appel à la méthode run de l'objet
		} catch (InterruptedException e1) {
			// TODO Bloc catch généré automatiquement
			e1.printStackTrace();
		}
    }
    
    /**
     *  methode appelee lors du clic sur le bouton de creation des threads faite par l'IHM
     * @throws InterruptedException 
     */
    public void dmdIHMCreationThread() throws InterruptedException {

    	int nbProducteur = ihmApplication.getNbThreadP();
        int nbConsommateur = ihmApplication.getNbThreadC();

        console.sendMsgToConsole(new MsgToConsole(NUM_CONSOLE_CONSOLE, "creation des threads Producteur et consommateur"));      
        console.sendMsgToConsole(new MsgToConsole(NUM_CONSOLE_CONSOLE, "lancement de l'IHM"));

    	/**
    	 *  construction de la liste des producteurs
    	 */
    	listThreadP =  new Thread[nbProducteur];
    	listeProducteurMQ = new ProducteurMQ[nbProducteur];

    	 // creation des threads Producteurs
    	 for (int i =  0 ; i < listThreadP.length ; i++) {
    		 listeProducteurMQ[i] = new ProducteurMQ("P"+(i+1), i+1, ihmApplication.getFreqProd(), PRIORITE_PRODUCTEUR, msgQProduit, console, NUM_CONSOLE_CONSOLE);
    		 listThreadP[i] =  new Thread(listeProducteurMQ[i]);
    	} 	

    	 /**
      	  *  construction de la liste des consommateurs
       	  */
     	listThreadC =  new Thread[nbConsommateur];
    	listeConsommateurMQ = new ConsommateurMQ[nbConsommateur];

     	 // creation des threads Consommateurs avec une priorite tiree au sort entre MIN et MAX
     	 for (int i =  0 ; i < listThreadC.length ; i++) {
     		 int priority = Thread.MIN_PRIORITY + (int) (Math.random() * ((PRIORITE_MAX_CONSOMMATEUR-PRIORITE_MIN_CONSOMMATEUR) + Thread.MIN_PRIORITY));   		 
     		 
    		 listeConsommateurMQ[i] = new ConsommateurMQ("C"+(i+1), i+1, priority, msgQProduit, console, NUM_CONSOLE_CONSOLE);
     		 listThreadC[i] =  new Thread(listeConsommateurMQ[i]);
     	}
     	 
     	afficheEtatThreads();	// affiche dans l'IHM l'etat des threads
    }
    
    
    
    /*
     * Constructeur : demarre l'IHM et envoit le controleur en parametre
     */
    public Controler() throws InterruptedException {

    	/*
    	 *  creation du mutex de synchro entre l'IHM et le controleur
    	 *  Ce MUTEX est cree bloque pour permettre de bloquer le thread lors du 1er appel a semGet()
    	 */
    	mutexSynchroIHM_Controleur = new Mutex(!MUTEX_CREE_LIBRE);
         
        ihmApplication = new IHM(this, mutexSynchroIHM_Controleur);	// l'IHM recoit le controleur et le Mutex de synchro en parametre
    	ihmApplication.setVisible(true);							// affichage de l'IHM

    	/*
    	 * on attend que l'IHM soit prête et que la saisie des valeurs de parametrage soit OK
    	 * lorsque cela sera Ok, l'IHM va liberer le MUTEX de manière a permettre au controler de continuer son lancement
    	 */
    	mutexSynchroIHM_Controleur.mutexGet();
   	
    	
    	/* lancement du thread de gestion de la console :
         * On commence par creer la MessageQueue qui va recevoir les messages a afficher dans la console
         */
    	msgQ_Console = new ArrayBlockingQueue<MsgToConsole>(ihmApplication.getTailleBufferConsole());
        console = new ConsoleMK("Console", NUMERO_CONSOLE, PRIORITE_CONSOLE, msgQ_Console, ihmApplication);
        
        new Thread(console).start();    
    	console.sendMsgToConsole(new MsgToConsole(NUM_CONSOLE_CONSOLE, "creation et lancement du thread de console"));

    	// on debloque l'IHM une fois que le thread de gestion de la console est lance
    	mutexSynchroIHM_Controleur.mutexRelease();


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
            	afficheEtatThreads();     		// n'affiche dans l'IHM l'état des threads que si ils existent 	
        }
        
    }
}

