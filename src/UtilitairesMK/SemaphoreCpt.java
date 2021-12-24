package UtilitairesMK;

import java.util.concurrent.Semaphore;

class SemaphoreCpt implements SemaphoreMK {

	private Semaphore sem;
	private int nbJetons;
	
	
	/**
	 * constructeur
	 * 
	 * @param nbJetons : nombre de jetons dans le semaphore
	 * @return
	 */
	public Semaphore SemaphoreCpt(int nbJetons) {
				
		return semCreate(nbJetons);
	}


	/**
	 * prendre le semaphore
	 */
	@Override
	public void semGet() {
		
		try {
			sem.acquire();
		} catch (InterruptedException e) {
			System.out.println("ERREUR sur semGet()");

			e.printStackTrace();
		}
	}

	

	/**
	 * rendre le semaphore
	 */
	@Override
	public void semRelease() {
		
		sem.release();
	}

	
	
	/**
	 * creation du semaphore avec le nombre de jetons demandes
	 */
	@Override
	public Semaphore semCreate(int nbJetons) {
		
		this.sem = new Semaphore(nbJetons);

		return sem;
	}

}
