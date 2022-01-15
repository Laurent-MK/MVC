package utilitairesMK_MVC;

import java.util.concurrent.ArrayBlockingQueue;


public class ParametrageClientTCP {
	
	// proprietes
	private String nomConsommateur = "nom inconnu";
    private int identifiant;
    private int priorite;
    private final ArrayBlockingQueue<Object> queue;
    private String adresseIPServeur;
    private int numPortServer; 
    private int typeThreadClient;
	

	public ParametrageClientTCP(
			String nom,
    		int identifiant,
    		int priority,
    		ArrayBlockingQueue<Object> q,
    		String adresseIPServeur,
    		int numPort,
    		int typeThreadClient)
    {
		this.nomConsommateur = nom;
        this.identifiant = identifiant;
        this.priorite = priority;
		this.queue = q;
        this.adresseIPServeur = String.copyValueOf(adresseIPServeur.toCharArray());
        this.numPortServer = numPort;
        this.typeThreadClient = typeThreadClient;
    }


	public int getTypeThreadClient() {
		return typeThreadClient;
	}


	public int getNumPortServer() {
		return numPortServer;
	}


	public String getAdresseIPServeur() {
		return adresseIPServeur;
	}


	public ArrayBlockingQueue<Object /*MessageMK*/> getQueue() {
		return queue;
	}

	public int getPriorite() {
		return priorite;
	}

	public String getNomConsommateur() {
		return nomConsommateur;
	}

	public int getIdentifiant() {
		return identifiant;
	}
	
}
