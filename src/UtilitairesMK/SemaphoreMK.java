package UtilitairesMK;

import java.util.concurrent.Semaphore;

public interface SemaphoreMK {
	public void semGet();
	public void semRelease();

	public Semaphore semCreate(int nbJetons);

}
