package fr.metakonsulting.mvc.utilitaires;

import java.util.concurrent.Semaphore;

public interface SemaphoreMK {
	public void semGet(int nbJetons);
	public void semRelease(int nbJetons);

	public Semaphore semCreate(int nbJetons);

}
