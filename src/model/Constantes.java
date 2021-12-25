package model;

public interface Constantes {
	/**
	 * Definition de toutes les constantes de l'application
	 */
	public static final boolean VERBOSE_ON = false;		// pour declencher les affichage dans la console systeme
	
	public static final int 	TAILLE_MESSAGE_Q_PC = 5;	// taille de la message queue entre producteurs et consommateurs
	public static final int 	TAILLE_MSG_Q_CONSOLE = 250;	// taille de la message queue du thread de console IHM
	public static final int 	MAX_MSG_CONSOLE = 500;		// maximum de messages stockes dans la console
	
	public static final int 	NUMERO_CONSOLE = 1;			// numero du thread de console

	public static final double 	SEUIL_CHGT_COULEUR_PROGRESS_BAR_CONSOLE = 0.8;		// seuil de remlplissage (%) de la console pour passer le bargraphe en rouge
	public static final double 	SEUIL_CHGT_COULEUR_PROGRESS_BAR_MQ_CONSOLE = 0.7;	// seuil de remlplissage (%) de la console pour passer le bargraphe en rouge
	
	public static final int 	PRIORITE_MIN_CONSOMMATEUR = 1;	// priorite min des consommateurs
	public static final int 	PRIORITE_MAX_CONSOMMATEUR = 7;	// priorite max des consommateurs
	public static final int 	PRIORITE_PRODUCTEUR = 10;		// priorite des producteurs
	public static final int 	PRIORITE_CONSOLE = 1;			// priorite du thread de console

	public static final int 	FREQ_PRODUCTION = 100;			// frequence de production des producteurs
	public static final int 	FREQ_POLLING_THREADS = 500;		// frequence de surveillance des threads producteur e0t consommateurs
	
	public static final int 	DEFAULT_NB_THREAD_CONS = 3;		// nbr threads consommateurs par defaut
	public static final int 	DEFAULT_NB_THREAD_PROD = 3;		// nbr threads producteurs par defaut

	public static final boolean MUTEX_CREE_LIBRE = true;		// si true, le Mutex est cree avec un jeton dispo. Si false, le Mutex est cree avec 0 jetons dedans
}
