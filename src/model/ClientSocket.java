package model;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

import utilitairesMK.MsgClientServeur;
import utilitairesMK.MsgToConsole;
import view.IHM_Test_Thread;



public class ClientSocket implements Constantes {
	private int serverPort = 9999;
	private String serverName;
	
	/**
	 * Constructeur de la classe
	 * @throws IOException 
	 * @throws UnknownHostException 
	 * @throws ClassNotFoundException 
	 */
	public ClientSocket(String adresseIPServeur, int numPort, IHM_Test_Thread ihmApplication, MsgToConsole msg) throws UnknownHostException, IOException, ClassNotFoundException {

		this.serverName = adresseIPServeur;
		this.serverPort = numPort;
		
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

		
		/**
		 * envoi d'un message applicatif entre le client et le serveur
		*/	
		out.writeObject(new MsgClientServeur(666, 200, "Message de controle du systme !!!",
				new MsgToConsole(0, false, "ceci est un objet MsgToConsole transporte dans le message client->serveur")));
		out.flush();
		System.out.println("Client: donnees MsgCS emises");
			 
		MsgToConsole msgRecu = (MsgToConsole) in.readObject();
		System.out.println("Client recoit: " + msgRecu.getMsg());
		
		in.close();
		out.close();
		socket.close();
		
//		ihmApplication.affichageConsole(msgRecu);
	}
	
	
	public void envoiMsg() {
		
	}
	
}
