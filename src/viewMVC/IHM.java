package viewMVC;


import controlerMVC.Controler;
import utilitairesMK_MVC.MsgToConsole;

public interface IHM {
	
	public void initIHM();
	public void affichageConsole(MsgToConsole msgConsole);
	public void affichageRemplissageMQ_Console(int nbVal);
	public Controler getControler();

}
