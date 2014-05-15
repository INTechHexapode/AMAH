package hexapode.capteurs;

import java.util.Arrays;

import container.Service;
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

public class Capteur implements Service {

    // Pourquoi static? PARCE QUE CE CODE EST TRÈS MOCHE, VOILÀ POURQUOI.
    private static Serial serie;
    private static final int nb_mesures = 20;
    private int[] dernieres_mesures = new int[nb_mesures];
    private int indice = 0;
    private int mediane = 100;
    private boolean on = false;
    
    
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
	    if(!on)
	        return false;
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
	public void tourner(double angle)
	{
	    // TODO: calcul de l'ordre en fonction de la direction
	    int ordre = 1500;
	    if(serie != null)
            try
            {
                serie.communiquer("#15P"+Integer.toString(ordre));
            } catch (SerialException e)
            {
                e.printStackTrace();
            }
	}
	
	/**
	 * Renvoie vrai si le jumper est retiré, faux sinon.
	 * @return
	 */
	public boolean jumper()
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
    public boolean getInverser()
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
    public void genere_mesure()
    {
        if(serie != null)
            try
            {
                serie.communiquer("VD");
                int mesure = serie.readByte();
                dernieres_mesures[indice] = mesure;
                indice++;
                indice %= nb_mesures;
                
                // Calcul de médiane
            } catch (SerialException e)
            {
                e.printStackTrace();
            }
    }
    
    /**
     * Met à jour la variable mediane
     */
    public void genere_mediane()
    {
        int[] copie = new int[nb_mesures];
        System.arraycopy(dernieres_mesures, 0, copie, 0, nb_mesures);
        Arrays.sort(copie);
        mediane = copie[nb_mesures/2];
    }

    public void setOn()
    {
        System.out.println("Capteur activé");
        on = true;
    }
    
    public void setOff()
    {
        System.out.println("Capteur désactivé");
        on = false;
    }
    
    public boolean isOn()
    {
        return on;
    }
    
}
