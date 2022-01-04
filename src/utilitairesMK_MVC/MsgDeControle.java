package utilitairesMK_MVC;

import java.io.Serializable;


public class MsgDeControle extends MessageMK implements Serializable {
	
	/**
	 * numero de version de classe : pour assurer la compatibilite
	 * dans les phases de serialisation/deserialisaton
	 */ 
	private static final long serialVersionUID = 8860951286914925272L;

	private Object obj = null;
	private String libelleMsg = "LIBELLE NON DEFINI";

	/**
	 * 
	 * Constructeur de classe
	 * 
	 * @param typeMsg
	 * @param numMsg
	 * @param libelleMsg
	 * @param obj
	 */
	public MsgDeControle(TypeMsgCS typeMsg, long numMsg, String libelleMsg, Object obj) {

		super(typeMsg, numMsg);		// appel du constructeur de la classe mere
		this.libelleMsg = libelleMsg;
		this.obj = obj;			// objet eventuel a transporter
	}


	/**
	 * @return le obj
	 */
	public Object getObj() {
		return obj;
	}


	/**
	 * @param obj le obj a definir
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
