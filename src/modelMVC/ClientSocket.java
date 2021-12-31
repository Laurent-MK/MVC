package modelMVC;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

import utilitairesMK_MVC.MsgClientServeur;
import utilitairesMK_MVC.MsgToConsole;



public class ClientSocket implements Constantes, Runnable {
	private int serverPort = 9999;
	private String serverName;
	private MsgToConsole msg;
	
	/**
	 * Constructeur de la classe
	 * @throws IOException 
	 * @throws UnknownHostException 
	 * @throws ClassNotFoundException 
	 */
	public ClientSocket(String adresseIPServeur, int numPort, MsgToConsole msg) throws UnknownHostException, IOException, ClassNotFoundException {

		this.serverName = adresseIPServeur;
		this.serverPort = numPort;
		this.msg = msg;
/*	
		Socket socket = new Socket(serverName, serverPort);
		System.out.println("Socket client: " + socket);

		ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
		out.flush();

		ObjectInputStream in = new ObjectInputStream(socket.getInputStream());

		System.out.println("Client a cree les flux");

		out.writeObject(msg);
//		out.writeObject(new MsgToConsole(NUM_CONSOLE_TEST_MUTEX, false, "venant du client : message passe par la scoket" ));
		out.flush();

		System.out.println("Client: donnees MsgToConsole emises");


		**
		 * envoi d'un message applicatif de type TYPE_MSG_CONTROLE entre le client et le serveur
		*
		out.writeObject(new MsgClientServeur(TYPE_MSG_CONTROLE, 200, "Message de controle du systeme !!!",
										new MsgToConsole(0, false, "msg du client (TYPE_MSG_CONTROLE) : " + msg.getNumConsoleDest())));
		out.flush();
		System.out.println("Client: donnees MsgCS emises");
		
		
		
		/**
		 * envoi d'un message applicatif de type TYPE_MSG_FIN_CONNEXION entre le client et le serveur
		*
		out.writeObject(new MsgClientServeur(TYPE_MSG_FIN_CONNEXION, 200, "Message de controle du systeme !!!",
										new MsgToConsole(0, false, "ceci est un objet MsgToConsole transporte dans le message client->serveur")));
		out.flush();
		System.out.println("Client: donnees MsgCS emises");

		/**
		 * lecture du message de retour venant du serveur.
		 * Sert a acquitter que les messages precedents sont bien passes
		 *
		MsgToConsole msgRecu = (MsgToConsole) in.readObject();
		System.out.println("Client recoit: " + msgRecu.getMsg());
		
		in.close();
		out.close();
		socket.close();
*/		
//		ihmApplication.affichageConsole(msgRecu);
	}
	
	
	public void envoiMsg() {
		
	}


	@Override
	public void run() {
		
		ObjectOutputStream out;
		
		
		try {
			Socket socket = new Socket(serverName, serverPort);
			if (VERBOSE_ON)
				System.out.println("Socket client: " + socket);


			out = new ObjectOutputStream(socket.getOutputStream());
			out.flush();

			ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
			if (VERBOSE_ON)
				System.out.println("Client a cree les flux");

//		out.writeObject(new MsgToConsole(NUM_CONSOLE_TEST_MUTEX, false, "venant du client : message passe par la scoket" ));
			out.writeObject(msg);
			out.flush();

			if (VERBOSE_ON)
				System.out.println("Client: donnees MsgToConsole emises");


			/**
			 * envoi d'un message applicatif de type TYPE_MSG_CONTROLE entre le client et le serveur
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
		
			in.close();
			out.close();
			socket.close();
			} catch (IOException e) {
				// TODO Bloc catch généré automatiquement
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				// TODO Bloc catch généré automatiquement
				e.printStackTrace();
			}
		}		
	
}
