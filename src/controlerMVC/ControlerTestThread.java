package controlerMVC;

import java.util.ArrayList;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import modelMVC.ConsommateurMQ;
import modelMVC.Constantes;
import modelMVC.ProducteurMQ;
import modelMVC.ProduitText;
import modelMVC.TestMutex;
import modelMVC.TestPoolThread;
import modelMVC.TestSemaphore;
import utilitairesMK_MVC.ClientSocketTCP;
import utilitairesMK_MVC.ConsoleDouble;
import utilitairesMK_MVC.ConsoleMK;
import utilitairesMK_MVC.MessageMK;
import utilitairesMK_MVC.MsgDeControle;
import utilitairesMK_MVC.MsgToConsole;
import utilitairesMK_MVC.Mutex;
import utilitairesMK_MVC.ParametrageClientTCP;
import utilitairesMK_MVC.SemaphoreCpt;
import viewMVC.IHM_Test_Thread;



/**
 * META KONSULTING
 * 
 * Classe controleur de l'application de test des messages queue bloquantes entre plusieurs threads
 * 
 * @author balou
 *
 */

public class ControlerTestThread implements Constantes, Controler {
	private IHM_Test_Thread ihmApplication;
	private boolean AjouterNumMsg = AJOUTER_NUM_MESSAGE;

    /**
     * propriétés utilisees pour gerer les threads : consommateur et producteur
     */
    private Thread listThreadP[];					// tableau des threads producteurs
    private ProducteurMQ listeProducteurMQ[];		// tableau des objets abritants les threads producteurs
    private Thread listThreadC[];					// tableau des threads consommateurs
    private ConsommateurMQ listeConsommateurMQ[];	// tableau des objets abritants les threads consommateurs
    // queue de message utilisee par les threads producteurs et consommateurs
    private static BlockingQueue<ProduitText> msgQProduit = new ArrayBlockingQueue<ProduitText>(TAILLE_MESSAGE_Q_PC);	
    
    /**
     *  proprietes pour la gestion des affichages dans la console
     */
    private ConsoleDouble console;	// l'objet pour manipuler la console
    private static ArrayBlockingQueue<MsgToConsole> msgQ_Console;	// queue de message utilisee pour les envois de messages dans la console
    
    private Mutex mutexSynchroIHM_Controleur;	// Mutex de synchronistion du Controleur et de l'IHM lors du démarrage de l'appli
    
    /**
     * proprietes utilisees pour la gestion des envois de messages vers la console distante
     */
    private ArrayBlockingQueue<Object/*MessageMK*/> msgQToServer;
    private ClientSocketTCP socketClient;
    
    
    
    /**
     * les accesseurs
     * 
     * @return
     */
    public ClientSocketTCP getSocketClient() {
		return socketClient;
	}

    public ConsoleMK getConsole() {
    	return console;
    }

    
	/**
     * Demarrage de l'application
     * 
     * @param args
     * @throws InterruptedException 
     */
    public static void main(String[] args) throws InterruptedException {

    	new ControlerTestThread(); // lancement du controleur
        System.out.println("main() termine !!!\n");
    }

    
    
    /**
     * demande d'affichage de l'etat des threads dans la zone prevue par l'IHM
     */
    private void afficheEtatThreads() {
        String msg = "";	// pour la creation du message
        // liste utilisee pour afficher l'etat des threads dans l'IHM
        ArrayList<String> listeEtatThreads = new ArrayList<>();

    	/**
    	 * construction du message indiquant l'etat de vie des threads
    	 */
       	listeEtatThreads.add("========== CONSOMMATEURS =============\n");

    	/*
         *  on commence par l'etat des threads "Consommateur"
         */
       	long nbConsoTotale = 0;
       	
       	for (int i=0; i <listThreadC.length ; i++) {
       		nbConsoTotale += listeConsommateurMQ[i].getNbConsoRealisees();
       		
       		msg = this.listeConsommateurMQ[i].getNom()
       				+ "."
       				+ listThreadC[i].getName()
       				+ "["
       				+ listThreadC[i].isAlive()
       				+ "]"
       				+ ".prio["
       				+ listThreadC[i].getPriority()
       				+ "].nbConso : "
       				+ listeConsommateurMQ[i].getNbConsoRealisees();
       		
       		listeEtatThreads.add(msg);	// ajout du message dans la liste
       	}
       
       	listeEtatThreads.add("\n    Threads Consommateur ---> nbConso totale = " + nbConsoTotale /*ConsommateurMQ.nbConsoTotale*/ + "\n");

       	
       	listeEtatThreads.add("\n");
       	listeEtatThreads.add("========== PRODUCTEURS =============\n");

        /*
         *  on passe a l'etat des threads "Producteur"
         */
       	msg = "";		// raz du message
       	long nbProdTotale = 0;
		
		for (int i=0; i <listThreadP.length ; i++) {
    		
    		nbProdTotale += listeProducteurMQ[i].getNbProdReal();
    		msg = this.listeProducteurMQ[i].getNom()
    				+ "."
    				+  listThreadP[i].getName()
       				+ "]"
       				+ ".prio["
       				+ listThreadP[i].getPriority()
       				+ "].nbProd : "
    				+ listeProducteurMQ[i].getNbProdReal();
   		
    		listeEtatThreads.add(msg);	// ajout du message dans la liste
    	}
    
		listeEtatThreads.add("\n    Threads Producteur ---> nbProd totale = "  + nbProdTotale + "\n");       	

    	// demande d'affichage dans l'IHM
    	this.ihmApplication.affichageEtatThreads(listeEtatThreads);
    }
    
    
	/**
	 * test de la fonction de deport de la console vers un PC distant
	 */   
    private void connexionToServer(String adresseIPServer, int numPortServer, int typeThreadGestion, Object msg) {
    	
    	ParametrageClientTCP paramClient;
    	ClientSocketTCP c;
    	
    	switch (typeThreadGestion) {
    	
    		/**
    		 * l'envoi du message se fera grace a unthread qui ne servira que pour un seul envoi 
    		 */
	    	case TYPE_THREAD_ENVOI_1_MSG :
	    		
	    		paramClient = new ParametrageClientTCP("client TCP", 0, 5, null, adresseIPServer, numPortServer, typeThreadGestion);
	    		c = new ClientSocketTCP(paramClient, (MsgToConsole)msg);
	          	new Thread(c).start();
	          	
	    		break;
	    	
	    	/**
	    	 * Un thread est cree pour assurer la communication permanente avec le serveur distant. Ce thread
	    	 * viendra se mettre en attente, sur un MQ, des messages envoyes par toute l'application.
	    	 * 
	    	 */
	    	case TYPE_THREAD_ENVOI_N_MSG :
				paramClient = new ParametrageClientTCP("client TCP", 0, 5, this.msgQToServer, adresseIPServer, numPortServer, typeThreadGestion);

				// creation de la socket client
				this.socketClient = new ClientSocketTCP(paramClient);

				// creation et lancement du thread de gestion des messages a destination du serveur distant
				new Thread(socketClient).start();
				break;
	    		
			/**
			 * 	l'envoi du message se fait en sequentiel directement dans cette methode
			 */
	    	case TYPE_THREAD_ENVOI_NO_THREAD :
				paramClient = new ParametrageClientTCP("client TCP", 0, 5, null, adresseIPServer, numPortServer, typeThreadGestion);
				
				// creation de la socket client
				c = new ClientSocketTCP(paramClient);
				c.ouvrirSocketClient();
				
				c.sendMsgUnique(msg);
				
				MsgDeControle msgC = new MsgDeControle(TYPE_MSG_FIN_CONNEXION, NUM_MSG_NOT_USED, "TYPE_THREAD_ENVOI_N_MSG - Message de fin de connexion", null);
				c.sendMsgUnique(msgC);
				c.fermerSocketClient();
				break;
				
	    		
	    	default :
	    		// mauvais type de gestion par thread
				if (VERBOSE_ON)
					System.out.println("ERREUR : connexionToServer() => mauvais type -typeThreadGestion- recu en parametre : " + typeThreadGestion);	    		
	    		break;
    	}
    }


    /**
     * 
     * methode appelee par l'IHM pour tester la connexion avec le serveur distant. Le test sera fait en utilisant
     * l'une des methodes suivantes : 
     *  - TYPE_THREAD_ENVOI_1_MSG : un thread est cree juste pour l'envoi de l'objet recu en parametre
     *  - TYPE_THREAD_ENVOI_N_MSG : un thread est cree et reste connecte au serveur tant qu'une fin de connexion    <<==== A REVOIR car le thread est deja cree
     *  							n'est pas demandee. Le msg passe en parametre est communique a ce thread en lui
     *  							envoyant dans une MQ reservee à cela.
     *  - TYPE_THREAD_ENVOI_NO_THREAD : l'envoi se fait en sequentiel dans le thread courant.
     * 
     * @param adresseIPServer
     * @param numPortServer
     * @param typeThreadGestion
     * @param obj
     * @return
     */
    public int dmdIHMTestConnexionToServer(String adresseIPServer, int numPortServer, int typeThreadGestion, Object obj) {
    	if (obj == null)
    		return TEST_CONNEXION_SERVEUR_BAD_PARAM;
    	else {
    		connexionToServer(adresseIPServer, numPortServer, typeThreadGestion, obj);
    		return TEST_CONNEXION_SERVEUR_OK;    		
    	}
    }

    
    
    /**
     * methode appelee sur le clic du bouton "GO"
     * @throws InterruptedException 
     */
    public void dmdIHMGo() throws InterruptedException {

    	String msg = "lancement des threads P et C";
    	MsgToConsole msgToConsole = new MsgToConsole(NUM_CONSOLE_CONSOLE, AjouterNumMsg, msg);
  
    	console.sendMsgToConsole(msgToConsole);

    	// lancement de threads consommateurs
    	for (int i =  0 ; i < listThreadC.length ; i++) {
    		listThreadC[i].start() ;
    	}

    	// lancement des threads producteurs
    	for (int i =  0 ; i < listThreadP.length ; i++) {
    		listThreadP[i].start() ;
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

        TestSemaphore listeTestSemaphore[];		// tableau des objets abritants les threads de test des semaphores
        Thread listThreadTestSem[];				// tableau des threads recevant les objets de test des semaphores

    	SemaphoreCpt sem = new SemaphoreCpt(nbrJetons);	// creation du semaphore avec le nbr de jetons passe en parametre
    	
    	listeTestSemaphore =  new TestSemaphore[nbrThread];	// tableau des objets de test
    	listThreadTestSem = new Thread[nbrThread];			// tableau des threads d'execution

    	for (int j=0; j<nbCycles; j++) {

    		for (int i=0; i < nbrThread; i++) {
    			listeTestSemaphore[i] = new TestSemaphore(NUM_CONSOLE_TEST_SEMAPHORE, this, sem); // creation de l'objet de test des semaphores
    			listThreadTestSem[i] = new Thread(listeTestSemaphore[i]);	// creation du thread d'acceuil des objets de test des semaphores
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

        console.sendMsgToConsole(new MsgToConsole(NUM_CONSOLE_CONSOLE, AjouterNumMsg, "creation des threads Producteur et consommateur"));      
        console.sendMsgToConsole(new MsgToConsole(NUM_CONSOLE_CONSOLE, AjouterNumMsg, "lancement de l'IHM"));

    	/**
    	 *  construction de la liste des producteurs
    	 */
    	listThreadP =  new Thread[nbProducteur];
    	listeProducteurMQ = new ProducteurMQ[nbProducteur];

    	 // creation des threads Producteurs
    	 for (int i =  0 ; i < listThreadP.length ; i++) {
     		 int priority = Thread.MIN_PRIORITY + (int) (Math.random() * ((PRIORITE_MAX_PRODUCTEUR-PRIORITE_MIN_PRODUCTEUR) + Thread.MIN_PRIORITY));   		 

     		 listeProducteurMQ[i] = new ProducteurMQ("P"+(i+1), i+1, ihmApplication.getFreqProd(), priority, msgQProduit, console, NUM_CONSOLE_CONSOLE);
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
    public ControlerTestThread() throws InterruptedException {

    	/*
    	 *  creation du mutex de synchro entre l'IHM et le controleur
    	 *  Ce MUTEX est cree bloque pour permettre de bloquer le thread lors du 1er appel a semGet()
    	 */
    	mutexSynchroIHM_Controleur = new Mutex(!MUTEX_CREE_LIBRE);
         
        ihmApplication = new IHM_Test_Thread(this, mutexSynchroIHM_Controleur);	// l'IHM recoit le controleur et le Mutex de synchro en parametre
    	ihmApplication.setVisible(true);							// affichage de l'IHM

    	/*
    	 * on attend que l'IHM soit prête et que la saisie des valeurs de parametrage soit OK
    	 * lorsque cela sera Ok, l'IHM va liberer le MUTEX de manière a permettre au controler de continuer son lancement
    	 */
    	mutexSynchroIHM_Controleur.mutexGet();
   	
    	/**
    	 * creation et lancement du thread de gestion des envois de message vers la console distante
    	 */
    	msgQToServer = new ArrayBlockingQueue<Object/*MessageMK*/>(100);
		connexionToServer(this.ihmApplication.getAdresseIPConsoleDistante(), NUMERO_PORT_SERVEUR_TCP, TYPE_THREAD_ENVOI_N_MSG, null);

		
    	/* lancement du thread de gestion de la console :
         * On commence par creer la MessageQueue qui va recevoir les messages a afficher dans la console
         */
    	msgQ_Console = new ArrayBlockingQueue<MsgToConsole>(ihmApplication.getTailleBufferConsole());
    	console = new ConsoleDouble("Console", NUMERO_CONSOLE, PRIORITE_CONSOLE, msgQ_Console, ihmApplication, ihmApplication.getAdresseIPConsoleDistante(), this.getSocketClient());
        
        new Thread(console).start();    
    	console.sendMsgToConsole(new MsgToConsole(NUM_CONSOLE_CONSOLE, AjouterNumMsg, "creation et lancement du thread de console"));
    	
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
            	afficheEtatThreads();     		// l'etat des threads n'est affiche dans l'IHM que si ils existent 
        }
        
    }
}

