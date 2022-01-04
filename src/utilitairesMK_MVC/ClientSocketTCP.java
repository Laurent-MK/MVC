package utilitairesMK_MVC;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.concurrent.ArrayBlockingQueue;

import modelMVC.Consommateur;
import modelMVC.Constantes;



public class ClientSocketTCP implements Constantes, Runnable, Consommateur {
	private int serverPort = NUMERO_PORT_SERVEUR_TCP;
	private String adresseIPServer;
//	private MsgToConsole msg;
	private Object msg;
	private Socket socketClient;
	private int typeThreadClient = TYPE_THREAD_ENVOI_1_MSG;
	private ObjectOutputStream canalEmission;
	private ObjectInputStream canalReception;
	
    private final ArrayBlockingQueue<Object/*MessageMK*/> queueMsgAEnvoyer;
	private String nomConsommateur = "nom inconnu";
    private int numero;

  	private boolean VERBOSE_LOCAL = VERBOSE_ON & false;

    
	/**
	 * Constructeur de la classe : celle-ci se charge de se connecter au serveur de console distante
	 * puis d'envoyer les messages vers ce processus
	 * 
     * @param param
     * @param MsgToConsole
     */
	public ClientSocketTCP(ParametrageClientTCP param, MsgToConsole MsgToConsole) {
		this.nomConsommateur = param.getNomConsommateur();
		this.adresseIPServer = param.getAdresseIPServeur();
		this.serverPort = param.getNumPortServer();
        this.adresseIPServer = String.copyValueOf(param.getAdresseIPServeur().toCharArray());
        this.queueMsgAEnvoyer = param.getQueue();
        this.typeThreadClient = param.getTypeThreadClient();

		this.msg = MsgToConsole;
	}

	public ClientSocketTCP(ParametrageClientTCP param, Object obj) {
		this.nomConsommateur = param.getNomConsommateur();
		this.adresseIPServer = param.getAdresseIPServeur();
		this.serverPort = param.getNumPortServer();
        this.adresseIPServer = String.copyValueOf(param.getAdresseIPServeur().toCharArray());
        this.queueMsgAEnvoyer = param.getQueue();
        this.typeThreadClient = param.getTypeThreadClient();

		this.msg = obj;
	}

	public ClientSocketTCP(ParametrageClientTCP param) {

		this.nomConsommateur = param.getNomConsommateur();
		this.adresseIPServer = param.getAdresseIPServeur();
		this.serverPort = param.getNumPortServer();
        this.adresseIPServer = String.copyValueOf(param.getAdresseIPServeur().toCharArray());
        this.queueMsgAEnvoyer = param.getQueue();
        this.typeThreadClient = param.getTypeThreadClient();

		this.msg = null;
	}
	
	public ClientSocketTCP(ParametrageClientTCP param, int typeThreadClient) {

		this.nomConsommateur = param.getNomConsommateur();
		this.adresseIPServer = param.getAdresseIPServeur();
		this.serverPort = param.getNumPortServer();
        this.adresseIPServer = String.copyValueOf(param.getAdresseIPServeur().toCharArray());
        this.queueMsgAEnvoyer = param.getQueue();
		this.msg = null;
		this.typeThreadClient = typeThreadClient;
	}
	
	
	/**
	 * methode pour envoyer un message vers le serveur. on distingue plusieurs format et type de messages.
	 * Le protocole de communication avec le serveur est implemente dans cette methode
	 * 
	 * les messages sous forme d'objets MsgToConsole sont des msg de debug destines a la console distante
	 * Ils sont envoyes sans traitement particulier
	 * 
	 * Les msg desous forme d'objets MsgDeControle sont des objets de gestion de la console distante. Ils implementent
	 * le protocole de discussion entre un client et un serveur de console distante
	 * 
	 * @param obj
	 */
	public void sendMsgUnique(Object obj) {
		MsgDeControle msgRecu;
		
		if (obj instanceof MsgToConsole) {
			if (VERBOSE_LOCAL)
				System.out.println("sendMsgUnique(MsgToConsole)");
			sendMsg(obj);
		}
		else if (obj instanceof MsgDeControle)
		{
			TypeMsgCS typeMsg = ((MessageMK) obj).getTypeMsg();

			switch (typeMsg) {
/*			
			case TypeMessageCS.TYPE_MSG_SERVEUR :
				break;
	*/		
			/**
			 * cas particulier du message de test : le protocole prevoit que le serveur
			 * nous retourne un message de test apres reception du notre. Cela permet de
			 * tester les deux sens de la communication
			 */
			case MSG_TEST_LINK :
				if (VERBOSE_LOCAL)
					System.out.println("sendMsgUnique(MsgDeControle) : TYPE_MSG_TEST_LINK");

				// on envoi le message de test de la liaison
				sendMsg(obj);
				
				// on receptionne l'acquittement du message de test
				msgRecu = (MsgDeControle) receiveMsg();
				if (VERBOSE_LOCAL)
					System.out.println("Message : TYPE_MSG_TEST_LINK recu - Client recoit : " + msgRecu.getLibelleMsg());
			break;


			/**
			 * message indiquant au serveur que nous quittons le canal de communication
			 */
			case MSG_FIN_CONNEXION :
				// on envoi le message de fin de la liaison antre le client et le serveur
				sendMsg(obj);
			break;

			
			case MSG_TRF_OBJET :
				sendMsg(obj);
				if (VERBOSE_LOCAL)
					System.out.println("sendMsgUnique(obj) : type de message = MSG_TRF_OBJET");
			break;


			/**
			 * le autres cas de figure : on se contente d'envoyer le message vers le serveur
			 */
			default :
				sendMsg(obj);
				if (VERBOSE_LOCAL)
					System.out.println("sendMsgUnique(MsgDeControle) : type de message = INCONNU");
				break;
			}
		}
		else {
			/**
			 *  puisque ce n'est ni un MsgToConsole, ni un MsgDeControle, il s'agit d'un autre objet
			 * on l'envoi sans traitement particulier
			 */
			sendMsg(obj);
			if (VERBOSE_LOCAL)
				System.out.println("sendMsgUnique(Object)");
		}
	}
	

	/**
	 * pour envoyer un seul message vers le serveur distant. Cette fct s'occupe de creer la connexion,
	 * envoyer le message puis refermer la connexion avec le serveur. Pour chaque envoi, cette fct refait tout 
	 * le travail d'ouverture, envoi puis fermeture de la connexion avec le serveur
	 * 
	 * @param msg
	 */
/*	public void sendMsgUnique(MessageMK msg) {
		if (VERBOSE_ON)
			System.out.println("ouverture de la socket");
		// ouverture de la socket vers le serveur
		ouvrirSocketClient();

		// on envoie le message
		sendMsg(msg);
		if (VERBOSE_ON)
			System.out.println("message envoye");

		// fermeture des canaux de communication et de la socket client
		fermerSocketClient();
		if (VERBOSE_ON)
			System.out.println("Reception du message -TYPE_MSG_FIN_CONNEXION- => fermeture de la liaison avec le serveur distant");
		
	}
*/	
	/**
	 * Envoi effectif d'un message et non un objet n type vers le serveur
	 * 
	 * @param msg
	 */
	private void sendMsg(MessageMK msg) {
		
		try {
			canalEmission.writeObject(msg);
			canalEmission.flush();
		} catch (IOException e) {
			// TODO Bloc catch généré automatiquement
			e.printStackTrace();
		}
	}

	/**
	 * envoi effectif d'un objet vers le serveur
	 * 
	 * @param obj
	 */
	private void sendMsg(Object obj) {
			
			try {
				canalEmission.writeObject(obj);
				canalEmission.flush();
			} catch (IOException e) {
				// TODO Bloc catch généré automatiquement
				e.printStackTrace();
			}
	}
	
	/**
	 * recevoir un message venant du serveur
	 * 
	 * @return
	 */
	private MessageMK receiveMsg() {
		
		MessageMK msgRecu;
		
		try {
			msgRecu = (MsgDeControle) canalReception.readObject();
		} catch (ClassNotFoundException | IOException e) {
			msgRecu = null;
			e.printStackTrace();
		}
		
		return msgRecu;
	}
	
	
	/**
	 * ouverture du canal de reception
	 * 
	 * @return ok ou ko
	 */
	private boolean ouvrirCanalReception() {
		
		try {
			canalReception = new ObjectInputStream(socketClient.getInputStream());
		} catch (IOException e) {
			e.printStackTrace();
			return !OUVERTURE_CANAL_OK;
		}
		return OUVERTURE_CANAL_OK;
	}

	/**
	 * ouverture du canal d'emission vers le serveur
	 * 
	 * @return
	 */
	private boolean ouvrirCanalEmission() {

		try {
			canalEmission = new ObjectOutputStream(socketClient.getOutputStream());
		} catch (IOException e) {
			e.printStackTrace();
			return !OUVERTURE_CANAL_OK;
		}
		return OUVERTURE_CANAL_OK;
	}
	
	/**
	 * fermeture du canal de reception vers le serveur
	 */
	private void fermerCanalReception() {
		try {
			this.canalReception.close();
		} catch (IOException e) {
			// TODO Bloc catch généré automatiquement
			e.printStackTrace();
		}
	}

	/**
	 * fermeture du canal d'emission vers le serveur
	 */
	private void fermerCanalEmission() {
		try {
			this.canalEmission.close();
		} catch (IOException e) {
			// TODO Bloc catch généré automatiquement
			e.printStackTrace();
		}
	}

	/**
	 * fermeture de la coscket client
	 */
	public void fermerSocketClient() {
		try {
			socketClient.close();
			
			fermerCanalReception();
			fermerCanalEmission();
			
		} catch (IOException e) {
			// TODO Bloc catch généré automatiquement
			e.printStackTrace();
		}
	}
	
	
	
	/**
	 * ouverture de la socket client
	 * 
	 * @return la socket client
	 */
	public Socket ouvrirSocketClient() {
		try {
			socketClient = new Socket(adresseIPServer, serverPort);	// creation de la socket
			
			ouvrirCanalEmission();		// ouverture du canal d'emission
			ouvrirCanalReception();		// ouverture du canal de recpetion
			
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
		if (VERBOSE_LOCAL)
			System.out.println("Socket client: " + socketClient);		
		return socketClient;
	}

	
	/**
	 * methode utilisee pour envoyer un message au thread de gestion de la console distante
	 * le message a envoyer est place dans la MQ du thread qui est connecte avec la console distante
	 * 
	 * @param msg
	 */
	public void sendMsgToServer(MessageMK msg) {
//		System.out.println("verif de la MQ du thread de connexion avec le serveur");

		if (this.queueMsgAEnvoyer.remainingCapacity() > 1 ) {
			try {
//				System.out.println("envoi du message dans la MQ du thread de connexion avec le serveur");
				this.queueMsgAEnvoyer.put(msg);
			} catch (InterruptedException e) {
				// TODO Bloc catch genere automatiquement
				e.printStackTrace();
			}      				
		}
		else
    		System.out.println("MQ console distante pleine => message perdu !!! : " + Thread.currentThread().getName());
	}


	
	/**
	 * "main()" du thread de gestion de l'envoi de message vers la console distante
	 */
	@Override
	public void run() {
		
		ouvrirSocketClient();
		if (VERBOSE_LOCAL)
			System.out.println("Client.run() => le thread : " + Thread.currentThread() + " a cree les flux");
		
		switch (typeThreadClient) {

			/**
			 *  ce thread ne sera utilise que pour envoyer un seul message applicatif
			 */
			case TYPE_THREAD_ENVOI_1_MSG :
				// ouverture de la socket client
				this.ouvrirSocketClient();
				
				// envoi du message (quelque soit le type d'objet) vers le serveur
				sendMsgUnique(msg);
				if (VERBOSE_LOCAL)
					System.out.println("Client.run() : => " + Thread.currentThread() + " donnees Msg emises");

				// envoi d'un message de fin de communication avec le serveur
				MsgDeControle msgC = new MsgDeControle(TypeMsgCS.MSG_FIN_CONNEXION, NUM_MSG_NOT_USED, "TYPE_THREAD_ENVOI_1_MSG - Message de fin de connexion", null);
				sendMsgUnique(msgC);

				// fermeture de la socket client et fin du thread
				this.fermerSocketClient();
				if (VERBOSE_LOCAL)
					System.out.println("Client.run() : => " + Thread.currentThread() + " => fermeture de la connexion");
				break;

			/**
			 * ce thread sera utilise pour envoyer bcp de messages vers le serveur distant
			 * Tant qu'il ne recoit pas l'ordre de mettre fin a la com, il reste en contact avec le serveur
			 */
			case TYPE_THREAD_ENVOI_N_MSG :
				if (VERBOSE_LOCAL)
					System.out.println("Thread : " + Thread.currentThread() + " a boucle infinie : on entre dans la boucle");

				while (true) {
						try {
							TypeMsgCS typeMsg = TypeMsgCS.MSG_INCONNU;

							if (VERBOSE_LOCAL)
								System.out.println("Thread : " + Thread.currentThread() + " en attente sur sa MQ");

							/**
							 * opn attend l'arrivee d'un msg dans la MQ du thread.
							 * Des son arrivee, on le recupere et on le traite
							 * Le thread est mis en sommeil tant qu'il n'y a pas de msg a traiter
							 */
							Object msg = this.queueMsgAEnvoyer.take();
							
							if (msg instanceof MessageMK)
								typeMsg = ((MessageMK)(msg)).getTypeMsg();

							consommer(msg);
							
							if (typeMsg == TypeMsgCS.MSG_FIN_CONNEXION) {
								/**
								 *  si on recoit une demande de fin de connexion, on doit prevenir le serveur. Cela a ete
								 *  fait sur la ligne du dessus dans "consommer(msg)" puis s'arreter la. Le thread courant
								 *  de gestion de la connexion permanente va donc s'arreter
								 */
								break;	// break de la boucle infinie
							}
						} 	catch (InterruptedException e) {
							// TODO Bloc catch généré automatiquement
							e.printStackTrace();
							}
				}

				/**
				 * fin du thread 
				 * fermeture de la socket client et fin du thread
				 */
				this.fermerSocketClient();
				if (VERBOSE_LOCAL)
					System.out.println("Client.run() : => " + Thread.currentThread() + " => fermeture de la connexion");					
				break;
				
			default :	// a developper
				break;
		}		
	}

	
	
	@Override
	public void consommer(Object msg) {
		/**
		 * envoi, vers le serveur, du message recu
		 */		
		sendMsgUnique(msg);
		if (VERBOSE_LOCAL)
			System.out.println("Client.run() : => " + Thread.currentThread() + " donnees Msg emises");		
	}

	
	
	@Override
	public String getNom() {
		return nomConsommateur;
	}

	@Override
	public void setNom(String nom) {
		this.nomConsommateur = nom;
	}

	@Override
	public int getIdentifiant() {
		return  numero;
	}

}
