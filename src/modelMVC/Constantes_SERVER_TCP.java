package modelMVC;

public interface Constantes_SERVER_TCP {
	/**
	 * Definition de toutes les constantes de l'application
	 */
	public static final int		VALEUR_NUM_NON_DEFINIE = 0;
	
	public static final boolean VERBOSE_ON = true;		// pour declencher les affichage dans la console systeme
	public static final boolean	VERBOSE_ON_CONSOLE = false;
	
	public static final int 	NB_CONSOLE = 5;				// taille de la message queue entre producteurs et consommateurs
	public static final int 	TAILLE_MSG_Q_CONSOLE = 250;	// taille de la message queue du thread de console IHM
	public static final int 	MAX_MSG_CONSOLE = 500;		// maximum de messages stockes dans la console
	public static final int		TAILLE_BUFFER_CONSOLE = 500;
	public static final boolean AJOUTER_NUM_MESSAGE = true;

	
	public static final int 	NUM_CONSOLE_SYSTEM = 0;		// numero de la console d'affichage
	public static final int 	NUM_CONSOLE_TCP = 1;		// numero de la console d'affichage
	
	public static final int		NUM_PORT_SERVER = 9999;
	
	public static final int 	NUMERO_THREAD_CONSOLE = 1;	// numero du thread de console

	public static final double 	SEUIL_CHGT_COULEUR_PROGRESS_BAR_CONSOLE = 0.8;		// seuil de remlplissage (%) de la console pour passer le bargraphe en rouge
	public static final double 	SEUIL_CHGT_COULEUR_PROGRESS_BAR_MQ_CONSOLE = 0.7;	// seuil de remlplissage (%) de la console pour passer le bargraphe en rouge
	
	public static final int 	PRIORITE_CONSOLE = 1;			// priorite du thread de console

	public static final boolean MUTEX_CREE_LIBRE = true;		// si true, le Mutex est cree avec un jeton dispo. Si false, le Mutex est cree avec 0 jetons dedans
	
	public static final int 	TYPE_MESSAGE_SERVEUR = 1;
	public static final int 	NUM_MSG_NOT_USED = 0;
}
