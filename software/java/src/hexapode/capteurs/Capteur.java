package hexapode.capteurs;

import serial.Serial;
import serial.SerialException;

// TODO

public class Capteur {

    Serial serie;
    
    public Capteur(Serial serie)
    {
        this.serie = serie;
    }
    
	public int mesure()
	{
	    // TODO
	    return 1000;
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
	
	public boolean jumper()
	{
	    // TODO
	    return true;
	}
    
    
}
