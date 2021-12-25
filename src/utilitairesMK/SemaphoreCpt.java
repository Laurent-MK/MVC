package utilitairesMK;

import java.util.concurrent.Semaphore;

class SemaphoreCpt implements SemaphoreMK {

	private Semaphore sem;
	
	
	/**
	 * constructeur
	 * 
	 * @param nbJetons : nombre de jetons dans le semaphore
	 * @return
	 */
	public SemaphoreCpt(int nbJetons) {
		this.sem = new Semaphore(nbJetons);
				
	}


	/**
	 * prendre le semaphore
	 */
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
	 * rendre le semaphore
	 */
	@Override
	public void semRelease(int nbJetons) {
		
		sem.release(nbJetons);
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
