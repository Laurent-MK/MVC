package model;

public interface Producteur {

	public final static int numeroProduit = 0;		// num�ro de produit
    public final static int numProducteur = 0; 		// num�ro de producteur
    public final static String producerName = "";	// nom du producteur
	
    
    /**
     * m�thodes a redefinir
     */
	public void run();
	public Object produce();
	public String getNom();
	public int getNumProducteur();
	

}
