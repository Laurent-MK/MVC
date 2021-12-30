package modelMVC;

public interface Consommateur {
	
	
	/**
	 * méthodes à redefinir
	 */
	public void run();
	public void consommer(Object produit) throws InterruptedException;
	
	public String getNom();
	public void setNom(String nom);
	
	public int getNumero();
}
