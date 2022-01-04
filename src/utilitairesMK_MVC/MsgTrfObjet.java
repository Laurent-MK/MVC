package utilitairesMK_MVC;

import java.io.Serializable;

public class MsgTrfObjet  extends MessageMK implements Serializable  {
	
	/**
	 * numero de version de la classe
	 */
	private static final long serialVersionUID = -1976529261823142742L;
	
	private Object objATransporter;
	

	public MsgTrfObjet(long identMsg, Object objATransporter) {
		super(TypeMsgCS.MSG_TRF_OBJET, identMsg);	// le type de msg est obligatoirement un trf d'objet
		
		this.objATransporter = objATransporter;
	}


	/**
	 * @return le obj
	 */
	public Object getObj() {
		return objATransporter;
	}	
	

}
