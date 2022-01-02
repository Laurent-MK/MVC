package utilitairesMK_MVC;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.concurrent.ArrayBlockingQueue;

import modelMVC.Consommateur;
import modelMVC.Constantes;



public class ClientSocket implements Constantes, Runnable, Consommateur {
	private int serverPort = NUMERO_PORT_SERVEUR_TCP;
	private String adresseIPServer;
	private MsgToConsole msg;
	private Socket socketClient;
	private int typeThreadClient = TYPE_THREAD_ENVOI_1_MSG;
	private ObjectOutputStream canalEmission;
	private ObjectInputStream canalReception;
	
    private final ArrayBlockingQueue<MessageMK> queueMsgAEnvoyer;
	private String nomConsommateur = "nom inconnu";
    private int numero;

    
	/**
	 * Constructeur de la classe : celle-ci se charge de se connecter au serveur de console distante
	 * puis d'envoyer les messages vers ce processus
	 * 
	 * @param adresseIPServeur
	 * @param numPort
	 * @param msg
	 * @throws UnknownHostException
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	public ClientSocket(ParametrageClientTCP param, MsgToConsole MsgToConsole) {
		this.nomConsommateur = param.getNomConsommateur();
		this.adresseIPServer = param.getAdresseIPServeur();
		this.serverPort = param.getNumPortServer();
        this.adresseIPServer = String.copyValueOf(param.getAdresseIPServeur().toCharArray());
        this.queueMsgAEnvoyer = param.getQueue();
        this.typeThreadClient = param.getTypeThreadClient();

		this.msg = MsgToConsole;
	}

	public ClientSocket(ParametrageClientTCP param) {

		this.nomConsommateur = param.getNomConsommateur();
		this.adresseIPServer = param.getAdresseIPServeur();
		this.serverPort = param.getNumPortServer();
        this.adresseIPServer = String.copyValueOf(param.getAdresseIPServeur().toCharArray());
        this.queueMsgAEnvoyer = param.getQueue();
        this.typeThreadClient = param.getTypeThreadClient();

		this.msg = null;
	}
	
	public ClientSocket(ParametrageClientTCP param, int typeThreadClient) {

		this.nomConsommateur = param.getNomConsommateur();
		this.adresseIPServer = param.getAdresseIPServeur();
		this.serverPort = param.getNumPortServer();
        this.adresseIPServer = String.copyValueOf(param.getAdresseIPServeur().toCharArray());
        this.queueMsgAEnvoyer = param.getQueue();
		this.msg = null;
		this.typeThreadClient = typeThreadClient;
	}
	
	
	public void sendMsgUnique(Object obj) {
		MsgDeControle msgRecu;
		

		if (obj instanceof MsgToConsole) {
			System.out.println("sendMsgUnique(MsgToConsole)");
			sendMsg(obj);
		}
		else if (obj instanceof MsgDeControle)
		{
			int typeMsg = ((MsgDeControle) obj).getTypeMsg();

			switch (typeMsg) {
			
			/**
			 * cas particulier du message de test : le protocole prevoit que le serveur
			 * nous retourne un message de test apres reception du notre. Cela permet de
			 * tester les deux sens de la communication
			 */
			case TYPE_MSG_TEST_LINK :
				System.out.println("sendMsgUnique(MsgDeControle) : TYPE_MSG_TEST_LINK");

				// on envoi le message de test de la liaison
				sendMsg(obj);
				
				// on receptionne l'acquittement du message de test
				msgRecu = (MsgDeControle) receiveMsg();
				if (VERBOSE_ON)
					System.out.println("Message : TYPE_MSG_TEST_LINK recu - Client recoit : " + msgRecu.getLibelleMsg());

			break;


			/**
			 * message indiquant au serveur que nous quittons le canal de communication
			 */
			case TYPE_MSG_FIN_CONNEXION :
				// on envoi le message de fin de la liaison
				sendMsg(obj);
			break;
				

			/**
			 * le autres cas de figure
			 */
			default :
				sendMsg(obj);
				System.out.println("sendMsgUnique(MsgDeControle) : type de message != TYPE_MSG_TEST_LINK");
				break;
			}
		}
		else {
			// puisque ce n'est ni un MsgToConsole, ni un MsgDeControle, il s'agit d'un autre objet
			// on l'envoi sans traitement particulier
			sendMsg(obj);
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
	public void sendMsgUnique(MessageMK msg) {
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
	
	/**
	 * envoyer un message vers le serveur via la socket existante. Cette methode doit etre utilisee
	 * en prenant soin de creer la socket client, ouvrir les canaux de communication puis de les refermer lorsque la connexion
	 * avec le serveur n'est plus necessaire. Et enfin fermer la socket client
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
	 * 
	 * @param socketClient
	 * @return
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
	 * 
	 * @param socketClient
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
	
	
	private void fermerCanalReception() {
		try {
			this.canalReception.close();
		} catch (IOException e) {
			// TODO Bloc catch généré automatiquement
			e.printStackTrace();
		}
	}

	private void fermerCanalEmission() {
		try {
			this.canalEmission.close();
		} catch (IOException e) {
			// TODO Bloc catch généré automatiquement
			e.printStackTrace();
		}
	}

	
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
	 * 
	 * @return
	 */
	public Socket ouvrirSocketClient() {
		try {
			socketClient = new Socket(adresseIPServer, serverPort);
			
			ouvrirCanalEmission();
			ouvrirCanalReception();
			
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
		if (VERBOSE_ON)
			System.out.println("Socket client: " + socketClient);		
		return socketClient;
	}

	
	
	public void sendMsgToServer(MessageMK msg) {
		System.out.println("verif de la MQ du thread de connexion avec le serveur");

		if (this.queueMsgAEnvoyer.remainingCapacity() > 1 ) {
			try {
				System.out.println("envoi du message dans la MQ du thread de connexion avec le serveur");
				this.queueMsgAEnvoyer.put(msg);
			} catch (InterruptedException e) {
				// TODO Bloc catch genere automatiquement
				e.printStackTrace();
			}      				
		}
		else
    		System.out.println("MQ console distante pleine => message perdu !!! : " + Thread.currentThread().getName());
	}

	
/*	
	public void sendMsgToServer(MsgToConsole msg) {

		if (this.queueMsgAEnvoyer.remainingCapacity() > 1 ) {
			try {
				this.queueMsgAEnvoyer.put(msg);
			} catch (InterruptedException e) {
				// TODO Bloc catch genere automatiquement
				e.printStackTrace();
			}      				
		}
		else
    		System.out.println("MQ console distante pleine => message perdu !!! : " + Thread.currentThread().getName());
	}

	public void sendMsgToServer(MsgDeControle msg) {

		if (this.queueMsgAEnvoyer.remainingCapacity() > 1 ) {
			try {
				this.queueMsgAEnvoyer.put(msg);
			} catch (InterruptedException e) {
				// TODO Bloc catch genere automatiquement
				e.printStackTrace();
			}      				
		}
		else
    		System.out.println("MQ console distante pleine => message perdu !!! : " + Thread.currentThread().getName());
	}
*/

	/**
	 * "main()" du thread de gestion de l'envoi de message vers la console distante
	 */
	@Override
	public void run() {
		ouvrirSocketClient();
		if (VERBOSE_ON)
			System.out.println("Client.run() => le thread : " + Thread.currentThread() + " a cree les flux");
		
		switch (typeThreadClient) {

			/**
			 *  ce thread ne sera utilise que pour envoyer un seul message applicatif
			 */
			case TYPE_THREAD_ENVOI_1_MSG :
				// ouverture de la socket client
				this.ouvrirSocketClient();
				
				// envoi du message vers le serveur
				sendMsgUnique(msg);
				if (VERBOSE_ON)
					System.out.println("Client.run() : => " + Thread.currentThread() + " donnees Msg emises");

				// envoi d'un message de fin de communication avec le serveur
				MsgDeControle msgC = new MsgDeControle(TYPE_MSG_FIN_CONNEXION, NUM_MSG_NOT_USED, "TYPE_THREAD_ENVOI_N_MSG - Message de fin de connexion", null);
				sendMsgUnique(msgC);

				// fermeture de la socket client et fin du thread
				this.fermerSocketClient();
				if (VERBOSE_ON)
					System.out.println("Client.run() : => " + Thread.currentThread() + " => fermeture de la connexion");
				break;

			/**
			 * ce thread sera utilise pour envoyer bcp de messages vers le serveur distant
			 * Tant qu'il ne recoit pas l'ordre de mettre fin a la com, il reste en contact avec le serveur
			 */
			case TYPE_THREAD_ENVOI_N_MSG :
				System.out.println("Thread : " + Thread.currentThread() + " a boucle infinie : on entre dans la boucle");

				// ouverture de la socket client
//				this.ouvrirSocketClient();

				while (true) {
						try {
							System.out.println("Thread : " + Thread.currentThread() + " en attente sur sa MQ");
							MessageMK msg = this.queueMsgAEnvoyer.take();
						
							int typeMsg = msg.getTypeMsg();

							consommer(msg);
							
							if (typeMsg == TYPE_MSG_FIN_CONNEXION) {
								break;
							}
						} 	catch (InterruptedException e) {
							// TODO Bloc catch généré automatiquement
							e.printStackTrace();
							}
				}
				// fermeture de la socket client et fin du thread
				this.fermerSocketClient();
				if (VERBOSE_ON)
					System.out.println("Client.run() : => " + Thread.currentThread() + " => fermeture de la connexion");					
				break;
			
				
				
			default :
				break;
		}		
	}

	
	
	@Override
	public void consommer(Object msg) {
		/**
		 * envoi, vers le server, du message recu
		 */		
		sendMsgUnique(msg);
		if (VERBOSE_ON)
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
