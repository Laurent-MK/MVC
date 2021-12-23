package model;

public interface Producteur {

    
    /**
     * m�thodes a redefinir
     */
	public void run();
	public Object produire();
	public String getNom();
	public void setNom(String nom);
	
	public int getNumero();
	
}
