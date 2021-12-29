package model;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

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

		System.out.println("Client: donnees emises");
		 
		MsgToConsole msgRecu = (MsgToConsole) in.readObject();
		System.out.println("Client recoit: " + msgRecu.getMsg());
		
		in.close();
		out.close();
		socket.close();
		
//		ihmApplication.affichageConsole(msgRecu);
	}
}
