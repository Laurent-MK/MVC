package fr.metakonsulting.mvc.utilitaires;

import java.util.concurrent.Semaphore;

/**
 * META KONSULTING
 * 
 * Classe de gestion des MUTEX. Un Mutex est un semaphore ne possèdant qu'un seul jeton.
 * Ces semaphores sont a utiliser pour proteger une section de code critique
 * 
 * @author balou
 *
 */
public class Mutex implements SemaphoreMK {
	
	private Semaphore sem;
	private static final int NB_JETONS_POUR_MUTEX = 1;

		
	/**
	 * Constructeur
	 * 
	 * @param libre
	 */
	public Mutex(boolean libre) {
		
		this.sem = semCreate(NB_JETONS_POUR_MUTEX);
		if (!libre)
			semGet(NB_JETONS_POUR_MUTEX);
	}


		
	/**
	 * obtenir le Mutex
	 */
	public void mutexGet() {
		semGet(NB_JETONS_POUR_MUTEX);
	}

	@Override
	public void semGet(int nbJetons) {
		try {
			sem.acquire(nbJetons);
		} catch (InterruptedException e) {
			System.out.println("ERREUR sur semGet()");

			e.printStackTrace();
		}

	}

	
	/**
	 * Relacher le Mutex
	 */
	public void mutexRelease() {
		semRelease(NB_JETONS_POUR_MUTEX);
	}
	
	@Override
	public void semRelease(int nbJetons) {
		sem.release(NB_JETONS_POUR_MUTEX);

	}

	
	/**
	 * Creer un Mutex
	 */
	@Override
	public Semaphore semCreate(int nbJetons) {
		
		this.sem = new Semaphore(nbJetons);

		return sem;
	}

}
