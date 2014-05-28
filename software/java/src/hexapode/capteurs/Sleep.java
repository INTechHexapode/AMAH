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
		try {
	        if(capteur.isOn())
	        {
    		    for(int i = 0; i < 5; i++)
    		    {
    		        capteur.genere_mesure();
    		        Thread.sleep(millis/5);
    		    }
                capteur.genere_mediane();
	        }
	        else
	            Thread.sleep(millis);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	        
	}
	
}
