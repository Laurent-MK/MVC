package fr.metakonsulting.mvc.view;


import fr.metakonsulting.mvc.controler.Controler;
import fr.metakonsulting.mvc.utilitaires.MsgToConsole;

public interface IHM {
	
	public void initIHM();
	public void affichageConsole(MsgToConsole msgConsole);
	public void affichageRemplissageMQ_Console(int nbVal);
	public Controler getControler();

}
