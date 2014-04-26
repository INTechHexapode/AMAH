package util;

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
			Thread.sleep(millis);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
}
