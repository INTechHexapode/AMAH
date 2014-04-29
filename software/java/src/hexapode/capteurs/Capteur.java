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

    Serial serie;
    
    public Capteur(Serial serie)
    {
        this.serie = serie;
    }
    
	public int mesure()
	{
	    int mesure = 1000;
        if(serie != null)
            try
            {
                serie.communiquer("VD");
                mesure = serie.readByte();
            } catch (SerialException e)
            {
                e.printStackTrace();
            }
	    
	    return mesure;
	}
	
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

    
}
