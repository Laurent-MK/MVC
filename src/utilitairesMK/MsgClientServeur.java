package utilitairesMK;

import java.io.Serializable;

import model.Constantes_SERVER_TCP;


public class MsgClientServeur extends MessageMK implements Constantes_SERVER_TCP, Serializable {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 8860951286914925272L;

	private Object obj = null;
	private String libelleMsg = "LIBELLE NON DEFINI";

	
	public MsgClientServeur(int typeMsg, long numMsg, String libelleMsg, Object obj) {

		setNumMsg(numMsg);
		setTypeMsg(typeMsg);
		this.libelleMsg = "ok";
		this.obj = obj;			// objet eventuel a transporter
		this.libelleMsg = libelleMsg;
	}


	/**
	 * @return le obj
	 */
	public Object getObj() {
		return obj;
	}


	/**
	 * @param obj le obj à définir
	 */
	public void setObj(Object obj) {
		this.obj = obj;
	}


	/**
	 * @return le libelleMsg
	 */
	public String getLibelleMsg() {
		return libelleMsg;
	}

}
