package hexapode.capteurs;

import serial.Serial;
import serial.SerialException;

/**
 * Regroupe tous les capteurs de l'hexapode.
 * Entrée A: interrupteur de couleur (discret)
 * Entrée B: jumper (discret)
 * Entrée D: infrarouge (analogique)
 * @author pf
 *
 */

public class Capteur {

    // Pourquoi static? PARCE QUE CE CODE EST TRÈS MOCHE, VOILÀ POURQUOI.
    private static Serial serie;
    private static int[] dernieres_mesures = new int[20];
    private static int indice = 0;
    private static int mediane = 100;
    
    
    public Capteur(Serial serie)
    {
        Capteur.serie = serie;
    }
    
    /**
     * Renvoie true si il y a un obstacle à moins de 300mm, false sinon.
     * 300mm est ajusté à l'évitement.
     * @return
     */
	public boolean mesure()
	{
        // Calcul provenant des datasheets (SSC-32 et GP2Y0A21YK0F (sérieux, c'est quoi ce nom?? on dirait de la mauvaise SF))
        // Le raisonnement: on a une entrée de 5*mesure/256 volts.
        // On capte à moins de 300mm si l'entrée est supérieur à 0.9V
        // On simplifie, et voilà...
	    return mediane > 46;
	}
		
	/**
	 * Tourne le capteur dans la direction désirée.
	 * @param direction
	 */
	public void tourner(int direction)
	{
	    // TODO: calcul de l'ordre en fonction de la direction
	    int angle = 1500;
	    if(serie != null)
            try
            {
                serie.communiquer("#15P"+Integer.toString(angle));
            } catch (SerialException e)
            {
                e.printStackTrace();
            }
	}
	
	/**
	 * Renvoie vrai si le jumper est retiré, faux sinon.
	 * @return
	 */
	public boolean jumper() // TODO vérifier
	{
        if(serie != null)
            try
            {
                serie.communiquer("B");
                return serie.readBoolean();
            } catch (SerialException e)
            {
                e.printStackTrace();
            }
        return true;
	}

	   /**
     * Renvoie vrai si on est rouge, faux sinon.
     * @return
     */
    public boolean getInverser() // TODO vérifier
    {
        if(serie != null)
            try
            {
                serie.communiquer("A");
                return serie.readBoolean();
            } catch (SerialException e)
            {
                e.printStackTrace();
            }
        return false;
    }
    
    /**
     * Met à jour le tableau dernieres_mesures
     */
    public static void genere_mesure()
    {
        if(serie != null)
            try
            {
                serie.communiquer("VD");
                int mesure = serie.readByte();
                dernieres_mesures[indice] = mesure;
                indice++;
                indice %= dernieres_mesures.length;
                
                // Calcul de médiane
            } catch (SerialException e)
            {
                e.printStackTrace();
            }
    }
    
    /**
     * Met à jour la variable mediane
     */
    public static void genere_mediane()
    {
        // TODO
        int[] copie = new int[dernieres_mesures.length];
        System.arraycopy(dernieres_mesures, 0, copie, 0, dernieres_mesures.length);
    }

    
}
