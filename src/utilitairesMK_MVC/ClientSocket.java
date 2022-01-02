package utilitairesMK_MVC;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.concurrent.BlockingQueue;

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
	
    private final BlockingQueue<MessageMK> queueMsgAEnvoyer;
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
		this.adresseIPServer = param.getAdresseIPServeur();
		this.serverPort = param.getNumPortServer();
        this.adresseIPServer = String.copyValueOf(param.getAdresseIPServeur().toCharArray());
        this.queueMsgAEnvoyer = param.getQueue();
        this.typeThreadClient = param.getTypeThreadClient();

		this.msg = MsgToConsole;
	}

	public ClientSocket(ParametrageClientTCP param) {

		this.adresseIPServer = param.getAdresseIPServeur();
		this.serverPort = param.getNumPortServer();
        this.adresseIPServer = String.copyValueOf(param.getAdresseIPServeur().toCharArray());
        this.queueMsgAEnvoyer = param.getQueue();

		this.msg = null;
	}
	
	public ClientSocket(ParametrageClientTCP param, int typeThreadClient) {

		this.adresseIPServer = param.getAdresseIPServeur();
		this.serverPort = param.getNumPortServer();
        this.adresseIPServer = String.copyValueOf(param.getAdresseIPServeur().toCharArray());
        this.queueMsgAEnvoyer = param.getQueue();
		this.msg = null;
		this.typeThreadClient = typeThreadClient;
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

		// reception du message d'acquittement du message de test
		MsgDeControle msgRecu = (MsgDeControle) receiveMsg();
		if (VERBOSE_ON)
			System.out.println("Message : TYPE_MSG_TEST_LINK recu - Client recoit : " + msgRecu.getLibelleMsg());

		// envoi du message de fin de connexion avec le serveur
		sendMsg(new MsgDeControle(TYPE_MSG_FIN_CONNEXION, 200, "Message de controle du systeme !!!", null));
		// reception de l'acquittement de la fin de connexion
		receiveMsg();
		
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
	public void sendMsg(MessageMK msg) {
		
		try {
			canalEmission.writeObject(msg);
			canalEmission.flush();
		} catch (IOException e) {
			// TODO Bloc catch généré automatiquement
			e.printStackTrace();
		}

		//		canalEmission.writeObject(new MsgClientServeur(TYPE_MSG_CONTROLE, 200, "Message de controle du systeme venant du client !!!",
//				new MsgToConsole(0, false, "msg du client (TYPE_MSG_CONTROLE) : " + msg.getNumConsoleDest())));
	}
	
	/**
	 * recevoir un message venant du serveur
	 * 
	 * @return
	 */
	public MessageMK receiveMsg() {
		
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

//		ObjectInputStream canalIn;
		
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

//		ObjectOutputStream canalOut;
		
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
		
//		Socket socketClient;
		
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

	

	/**
	 * "main() du thread de gestion de l'envoi de message vers la console distante
	 */
	@Override
	public void run() {
		try {
			ouvrirSocketClient();
			if (VERBOSE_ON)
				System.out.println("Client a cree les flux");

			if (typeThreadClient == TYPE_THREAD_ENVOI_1_MSG) {

				canalEmission.writeObject(msg);	// envoi du message
				canalEmission.flush();		
				if (VERBOSE_ON)
					System.out.println("Client: donnees MsgToConsole emises");

				/**
				 * envoi d'un message applicatif de type TYPE_MSG_CONTROLE vers le serveur
				 */
				canalEmission.writeObject(new MsgDeControle(TYPE_MSG_CONTROLE, 200, "Message de controle du systeme !!!",
												new MsgToConsole(0, false, "msg du client (TYPE_MSG_CONTROLE) : " + msg.getNumConsoleDest())));
				canalEmission.flush();

				if (VERBOSE_ON)
					System.out.println("Client: donnees MsgCS -TYPE_MSG_CONTROLE- emises");
			
				/**
				 * envoi d'un message applicatif de type TYPE_MSG_FIN_CONNEXION entre le client et le serveur
				 */
				canalEmission.writeObject(new MsgDeControle(TYPE_MSG_FIN_CONNEXION, 200, "Message de controle du systeme !!!",
						new MsgToConsole(0, false, "ceci est un objet MsgToConsole transporte dans le message client->serveur")));
				canalEmission.flush();
				if (VERBOSE_ON)
					System.out.println("Client: donnees MsgCS -TYPE_MSG_FIN_CONNEXION- emises");

				/**
				 * lecture du message de retour venant du serveur.
				 * Sert a acquitter le fait que les messages precedents sont bien passes
				 */
				MsgDeControle msgRecu = (MsgDeControle) canalReception.readObject();
				if (VERBOSE_ON) {
					System.out.println("Message : TYPE_MSG_FIN_CONNEXION recu par le client - Client recoit : " + msgRecu.getLibelleMsg());
				}
		
				fermerSocketClient();				
			}
			else {
				
				System.out.println("Thread : " + Thread.currentThread() + "a boucle infinie : on entre dans la boucle");
				while (true) {
					try {
						consommer(this.queueMsgAEnvoyer.take());
						
						
						} catch (InterruptedException e) {
						// TODO Bloc catch généré automatiquement
						e.printStackTrace();
						}
					
					fermerSocketClient();
				}
			
			}
		} catch (IOException e) {
			// TODO Bloc catch g�n�r� automatiquement
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Bloc catch g�n�r� automatiquement
			e.printStackTrace();
		}
	}

	
	
	@Override
	public void consommer(Object msg) {
		
		try {
			canalEmission.writeObject(msg);
			canalEmission.flush();
		} catch (IOException e) {
			// TODO Bloc catch généré automatiquement
			e.printStackTrace();
		}		
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
