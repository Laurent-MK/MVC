package utilitairesMK_MVC;

/**
 * enum pour les types de messages echanges entre le client et
 * le serveur sur socket TCP
 * 
 * @author Balou
 *
 */
public enum TypeMsgCS {
	
	MSG_TYPE_NOT_USED,
	MSG_INCONNU,
	MSG_SERVEUR,
	MSG_CONSOLE,
	MSG_FIN_CONNEXION,
	MSG_CONTROLE,
	MSG_TEST_LINK,
	MSG_TRF_OBJET,
	MSG_TRF_IHM,
	TBD
}
