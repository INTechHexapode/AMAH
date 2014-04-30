package util;

import hexapode.capteurs.Capteur;

/**
 * Classe statique de sleep. Contient également de temps d'action par défaut.
 * @author pf
 *
 */

public class Sleep {

    public static int temps_defaut = 300;
    
    /**
     * Sleep pendant temps_defaut
     */
    public static void sleep()
    {
        sleep(temps_defaut);
    }
    
    /**
     * Endors le processus pendant "millis" ms
     * @param millis
     */
	public static void sleep(long millis)
	{
		try {
		    for(int i = 0; i < 5; i++)
		    {
		        Capteur.genere_mesure();
		        Thread.sleep(millis/5);
		    }
            Capteur.genere_mediane();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
}
