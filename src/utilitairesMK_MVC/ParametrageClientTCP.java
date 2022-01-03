package utilitairesMK_MVC;

import java.util.concurrent.ArrayBlockingQueue;

import modelMVC.Constantes;

public class ParametrageClientTCP implements Constantes {
	
	// proprietes
	private String nomConsommateur = "nom inconnu";
    private final ArrayBlockingQueue<Object/*MessageMK*/> queue;
    private int identifiant;
    private int priorite;
    private String adresseIPServeur;
    private int numPortServer; 
    private int typeThreadClient;
	

	public ParametrageClientTCP(
			String nom,
    		int identifiant,
    		int priority,
    		ArrayBlockingQueue<Object/*MessageMK*/> q,
    		String adresseIPServeur,
    		int numPort,
    		int typeThreadClient)
    {
		this.nomConsommateur = nom;
		this.queue = q;
        this.identifiant = identifiant;
        this.priorite = priority;
        this.numPortServer = numPort;
        this.adresseIPServeur = String.copyValueOf(adresseIPServeur.toCharArray());
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
