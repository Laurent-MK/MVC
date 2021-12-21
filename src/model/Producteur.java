package model;

public interface Producteur {

	public final static int numeroProduit = 0;		// numéro de produit
    public final static int numProducteur = 0; 		// numéro de producteur
    public final static String producerName = "";	// nom du producteur
	
    
    /**
     * méthodes a redefinir
     */
	public void run();
	public Object produce();
	public String getNom();
	public int getNumProducteur();
	

}
