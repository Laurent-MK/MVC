package modelMVC;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

import utilitairesMK_MVC.MsgClientServeur;
import utilitairesMK_MVC.MsgToConsole;



public class ClientSocket implements Constantes, Runnable {
	private int serverPort = NUMERO_PORT_SERVEUR_TCP;
	private String serverName;
	private MsgToConsole msg;
	
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
	public ClientSocket(String adresseIPServeur, int numPort, MsgToConsole msg) throws UnknownHostException, IOException, ClassNotFoundException {

		this.serverName = adresseIPServeur;
		this.serverPort = numPort;
		this.msg = msg;
	}
	
	
	public void envoiMsg() {
		
	}


	/**
	 * "main() du thread de gestion de l'envoi de message vers la console distante
	 */
	@Override
	public void run() {
		
		ObjectOutputStream out;	// canal de sortie vers le client
		
		try {
			Socket socket = new Socket(serverName, serverPort);
			if (VERBOSE_ON)
				System.out.println("Socket client: " + socket);

			// ouverture du canal d'envoi
			out = new ObjectOutputStream(socket.getOutputStream());
//			out.flush();

			// ouverture du canal de reception des messages
			ObjectInputStream in = new ObjectInputStream(socket.getInputStream());	// canal d'arrivee des messages
			if (VERBOSE_ON)
				System.out.println("Client a cree les flux");

			out.writeObject(msg);	// envoi du message
			out.flush();		
			if (VERBOSE_ON)
				System.out.println("Client: donnees MsgToConsole emises");

			/**
			 * envoi d'un message applicatif de type TYPE_MSG_CONTROLE vers le serveur
			 */
			out.writeObject(new MsgClientServeur(TYPE_MSG_CONTROLE, 200, "Message de controle du systeme !!!",
											new MsgToConsole(0, false, "msg du client (TYPE_MSG_CONTROLE) : " + msg.getNumConsoleDest())));
			out.flush();

			if (VERBOSE_ON)
				System.out.println("Client: donnees MsgCS emises");
		
			/**
			 * envoi d'un message applicatif de type TYPE_MSG_FIN_CONNEXION entre le client et le serveur
			 */
			out.writeObject(new MsgClientServeur(TYPE_MSG_FIN_CONNEXION, 200, "Message de controle du systeme !!!",
					new MsgToConsole(0, false, "ceci est un objet MsgToConsole transporte dans le message client->serveur")));
			out.flush();
			if (VERBOSE_ON)
				System.out.println("Client: donnees MsgCS emises");

			/**
			 * lecture du message de retour venant du serveur.
			 * Sert a acquitter que les messages precedents sont bien passes
			 */
			MsgToConsole msgRecu = (MsgToConsole) in.readObject();
			if (VERBOSE_ON)
				System.out.println("Client recoit: " + msgRecu.getMsg());
		
			in.close();		// fermeture du canal d'arrivee des messages
			out.close();	// fermeture du canal d'envoi des messages
			socket.close();	// fermeture de la socket client
			
			} catch (IOException e) {
				// TODO Bloc catch généré automatiquement
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				// TODO Bloc catch généré automatiquement
				e.printStackTrace();
			}
		}	
}
