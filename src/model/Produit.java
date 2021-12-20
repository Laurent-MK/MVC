package model;


/**
 * Classe Produit
 * 
 * @author balou
 *
 */
public class Produit {
    private String name;
    private int numero;
    private int numProducteur;
    
    // les constructeurs
    public Produit(String name, int numero) {
        this.name = name;
        this.numero = numero;
    }
    
    public Produit(String name, int numProducteur, int numero) {
        this.name = name;
        this.numero = numero;
        this.numProducteur = numProducteur;
    }
    
    // les méthodes de la classe
    public String getInfo() {
        return ("Nom du produit : " + this.name + " ayant le num�ro => " + this.numProducteur + "_" + this.numero);
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



