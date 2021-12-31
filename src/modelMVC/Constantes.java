package modelMVC;

public interface Constantes {
	/**
	 * Definition de toutes les constantes de l'application
	 */
	public static final boolean VERBOSE_ON = false;		// pour declencher les affichage dans la console systeme
	
	public static final int 	TAILLE_MESSAGE_Q_PC = 5;	// taille de la message queue entre producteurs et consommateurs
	public static final int 	NB_CONSOLE = 5;				// taille de la message queue entre producteurs et consommateurs
	public static final int 	TAILLE_MSG_Q_CONSOLE = 250;	// taille de la message queue du thread de console IHM
	public static final int 	MAX_MSG_CONSOLE = 500;		// maximum de messages stockes dans la console

	public static final int 	NUM_CONSOLE_CONSOLE = 0;		// numero de la console d'affichage
	public static final int 	NUM_CONSOLE_TEST_SEMAPHORE = 1;	// numero de la console d'affichage
	public static final int 	NUM_CONSOLE_TEST_MUTEX = 2;		// numero de la console d'affichage
	public static final int 	NUM_CONSOLE_TEST_POOL = 3;		// numero de la console d'affichage
	public static final boolean AJOUTER_NUM_MESSAGE = true;		// indique si l'IHM doit ajouter le numero global du message

	public static final int 	NUMERO_CONSOLE = 1;			// numero du thread de console
	public static final int		NUMERO_PORT_SERVEUR_TCP = 9999;
	public static final String	ADR_IP_SERVEUR_TCP = "192.168.1.116";

	public static final double 	SEUIL_CHGT_COULEUR_PROGRESS_BAR_CONSOLE = 0.8;		// seuil de remlplissage (%) de la console pour passer le bargraphe en rouge
	public static final double 	SEUIL_CHGT_COULEUR_PROGRESS_BAR_MQ_CONSOLE = 0.7;	// seuil de remlplissage (%) de la console pour passer le bargraphe en rouge
	
	public static final int 	PRIORITE_MIN_CONSOMMATEUR = 1;		// priorite min des consommateurs
	public static final int 	PRIORITE_MAX_CONSOMMATEUR = 5;		// priorite max des consommateurs
	public static final int 	PRIORITE_MIN_PRODUCTEUR = 6;		// priorite min des producteurs
	public static final int 	PRIORITE_MAX_PRODUCTEUR = 10;		// priorite max des producteurs
	public static final int 	PRIORITE_PRODUCTEUR_DEFAUT = 10;	// priorite par defaut des producteurs
	public static final int 	PRIORITE_CONSOLE = 1;				// priorite du thread de console
	public static final String	NOM_PRODUIT_TXT = "ProductTXT";		// nom donne au produit texte à produire

	public static final int 	FREQ_PRODUCTION = 100;			// frequence de production des producteurs
	public static final int		AJOUT_DELAY_PROD_MIN = 0;		// temps minimum (en msec) ajoute (par calcul alleatoire) entre chaque production
	public static final int		AJOUT_DELAY_PROD_MAX = 10;		// temps max (en msec) ajoute (par calcul alleatoire) entre chaque production
	public static final int 	FREQ_POLLING_THREADS = 500;		// frequence de surveillance des threads producteur e0t consommateurs
	
	public static final int 	DEFAULT_NB_THREAD_CONS = 3;		// nbr threads consommateurs par defaut
	public static final int 	DEFAULT_NB_THREAD_PROD = 3;		// nbr threads producteurs par defaut

	public static final boolean MUTEX_CREE_LIBRE = true;		// si true, le Mutex est cree avec un jeton dispo. Si false, le Mutex est cree avec 0 jetons dedans
	public static final int		NB_BOUCLE_EN_ZONE_CRITIQUE = 10;	// nbr de tour de boucle dans une zone critique

	public static final int 	TYPE_MSG_INCONNU = -1;
	public static final int 	TYPE_MSG_SERVEUR = 1;
	public static final int		TYPE_MSG_CONSOLE = TYPE_MSG_SERVEUR+1;
	public static final int		TYPE_MSG_FIN_CONNEXION = TYPE_MSG_CONSOLE+1;
	public static final int		TYPE_MSG_CONTROLE = TYPE_MSG_FIN_CONNEXION+1;

	public static final int 	NUM_MSG_NOT_USED = 0;
}
