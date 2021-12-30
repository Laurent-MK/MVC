package utilitairesMK;

import java.io.Serializable;

import model.Constantes_SERVER_TCP;


public class MessageMK implements Constantes_SERVER_TCP, Serializable {

	// Numero de version de la classe serialisable
	private static final long serialVersionUID = -2239749627148667746L;
	
	private int typeMsg = TYPE_MESSAGE_SERVEUR;
	private long numMsg = NUM_MSG_NOT_USED;
	
	public MessageMK() {
		
	}
	
	public int getTypeMsg() {
		return typeMsg;
	}

	public void setTypeMsg(int typeMessage) {
		this.typeMsg = typeMessage;
		
	}

	public long getNumMsg() {
		return numMsg;
	}

	public void setNumMsg(long numMessage) {
		this.numMsg = numMessage;
	}
		
}
