package UtilitairesMK;

import java.util.concurrent.Semaphore;



public class Mutex implements SemaphoreMK {
	
	private Semaphore sem;
	private static final int NB_JETONS_POUR_MUTEX = 1;

		
	public Mutex() {
		
		this.sem = semCreate(NB_JETONS_POUR_MUTEX);
	}

	
	
	@Override
	public void semGet() {
		try {
			sem.acquire();
		} catch (InterruptedException e) {
			System.out.println("ERREUR sur semGet()");

			e.printStackTrace();
		}

	}

	
	
	@Override
	public void semRelease() {
		sem.release();

	}

	
	
	@Override
	public Semaphore semCreate(int nbJetons) {
		
		this.sem = new Semaphore(nbJetons);

		return sem;
	}

}
