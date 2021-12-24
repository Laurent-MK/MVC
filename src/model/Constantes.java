package model;

public interface Constantes {
	public static final int TAILLE_MESSAGE_Q_PC = 5;
	public static final int TAILLE_MSG_Q_CONSOLE = 100;
	public static final int NUMERO_CONSOLE = 1;
	public static final int MAX_MSG_CONSOLE = 100;
	
	public static final int PRIORITE_CONSOMMATEUR = 5;
	public static final int PRIORITE_PRODUCTEUR = 10;
	public static final int PRIORITE_CONSOLE = 1;
	public static final int FREQ_PRODUCTION = 100;
	public static final int DELAY_AVANT_NOUVEL_ESSAI = 500;

	
	public static final int DEFAULT_NB_THREAD_CONS = 3;
	public static final int DEFAULT_NB_THREAD_PROD = 3;
	public static final int FREQ_POLLING_THREADS = 500;
}
