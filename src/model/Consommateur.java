package model;

public interface Consommateur {
	
	
	/**
	 * méthodes à redefinir
	 */
	public void run();
	public void consommer(Object produit);
	
	public String getNom();
	public void setNom(String nom);
	
	public int getNumero();
}
