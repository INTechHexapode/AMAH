package hexapode.capteurs;

import container.Service;


/**
 * Classe de sleep. Contient également de temps d'action par défaut.
 * @author pf
 *
 */

public class Sleep implements Service {

    public static int temps_defaut = 300;
    private Capteur capteur;
    
    public Sleep(Capteur capteur)
    {
        this.capteur = capteur;
    }
        
    /**
     * Sleep pendant temps_defaut
     */
    public void sleep()
    {
        sleep(temps_defaut);
    }
    
    /**
     * Endors le processus pendant "millis" ms
     * @param millis
     */
	public void sleep(long millis)
	{
	    millis -= 80;
	    if(millis < 0)
	    	millis = 0;
		try {
		    for(int i = 0; i < 3; i++)
		    {
		        capteur.genere_mesure();
		        Thread.sleep(millis/3);
		    }
            capteur.genere_mediane();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	        
	}
	
}
