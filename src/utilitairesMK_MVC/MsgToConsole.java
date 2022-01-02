package utilitairesMK_MVC;

import java.io.Serializable;

/**
 * Classe permettant de construire des messages a destination de la console systeme
 * 
 * @author Balou
 *
 */
public class MsgToConsole extends MessageMK implements Serializable {
	
	/**
	 * numero de serie de la classe pour verif de la compatibilite lors de la serialisation
	 */
	private static final long serialVersionUID = -6392917557600619944L;

	private int numConsoleDest;
	private String msg;
	private boolean ajoutNumMsg = true;

	
	/**
	 * Constructeur
	 */
	public MsgToConsole(int numConsole, boolean isAjoutNumMsg, String msg) {
		
		this.numConsoleDest = numConsole;
//		this.msg = msg;
		this.msg = String.copyValueOf(msg.toCharArray());
		this.ajoutNumMsg = isAjoutNumMsg;
	}
	
	public MsgToConsole(int numConsole, String msg) {
		
		this.numConsoleDest = numConsole;
		this.msg = String.copyValueOf(msg.toCharArray());
		this.ajoutNumMsg = false;
	}

	
	
	/**
	 * methodes d'acces aux proprietes de cette classe
	 * 
	 * @return
	 */
	public String getMsg() {
		return msg;
	}
	
	public void setMsg(String msg) {
		this.msg = msg;
	}

	
	public int getNumConsoleDest() {
		return numConsoleDest;
	}
	
	public boolean isAjoutNumMsg() {
		return ajoutNumMsg;
	}

}
