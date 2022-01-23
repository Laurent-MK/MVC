package fr.metakonsulting.mvc.utilitaires;

import java.io.Serializable;

import fr.metakonsulting.mvc.model.Constantes;


public class MessageMK implements Constantes, Serializable {

	// Numero de version de la classe serialisable
	private static final long serialVersionUID = -2239749627148667746L;
	
	private long numMsg = NUM_MSG_NOT_USED;
	private TypeMsgCS typeMsg = TypeMsgCS.MSG_TYPE_NOT_USED;
	
	
	/**
	 * constructeur avec parametre
	 * 
	 * @param type
	 * @param num
	 */
	public MessageMK(TypeMsgCS type, long num) {
		this.typeMsg = type;
		this.numMsg = num;
	}

	/**
	 * constructeur sans parametre. Dans ce cas, on ne fait rien
	 */
	public MessageMK() {
	}

	
	public TypeMsgCS getTypeMsg() {
		return typeMsg;
	}

	public void setTypeMsg(TypeMsgCS typeMessage) {
		this.typeMsg = typeMessage;
		
	}

	public long getNumMsg() {
		return numMsg;
	}

	public void setNumMsg(long numMessage) {
		this.numMsg = numMessage;
	}
		
}
