package view;

import java.net.UnknownHostException;

import utilitairesMK.MsgToConsole;

public interface IHM {
	
	public void initIHM();
	public void affichageConsole(MsgToConsole msgConsole);
	public void affichageRemplissageMQ_Console(int nbVal);

}
