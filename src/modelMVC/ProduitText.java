package modelMVC;


/**
 * Classe Produit : implemente la classe Produit
 * 
 * @author balou
 *
 */
public class ProduitText implements Produit {
    private String name;
    private int numero;
    private int numProducteur;
    
  
    /**
     * 
     * @param name
     * @param numProducteur
     * @param numero
     */
    public ProduitText(String name, int numProducteur, int numero) {
        this.name = name;
        this.numero = numero;
        this.numProducteur = numProducteur;
    }
    
    /**
     *  methodes de la classe
     * @return
     */
    
    public String getInfo() {
        return ("Nom du produit : " + this.name + " numero => " + this.numProducteur + "_" + this.numero);
    }
    
    public String getName() {
    	return "" + this.name;
    }
    public String getNumero() {
    	return "" + this.numero;
    }
    public String getNumProducteur () {
    	return "" + numProducteur;
    }
}



