package model;

public class MsgToConsole {
	
	private int numConsoleDest;
	private String msg;
	
	/**
	 * Constructeur
	 */
	public MsgToConsole(int numConsole, String msg) {
		
		this.numConsoleDest = numConsole;
		this.msg = msg;
	}
	
	
	/**
	 * methodes d'acces aux proprietes de cette classe
	 * 
	 * @return
	 */
	public String getMsg() {
		return msg;
	}
	
	
	public int getNumConsoleDest() {
		return numConsoleDest;
	}
	

}
