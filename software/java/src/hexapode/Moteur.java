package hexapode;

import serial.Serial;
import serial.SerialException;

	/**
	 * Classe des moteurs. Visibilité en "friendly".
	 * Retient toujours son état
	 * @author pf
	 *
	 */

class Moteur {

	private Serial serie;
	private int id;
	private int angle_min;
	private int angle_max;

	/**
	 * Construit un moteur, désasservi.
	 * @param serie
	 * @param id
	 * @param etat
	 * @param angle_min
	 * @param angle_max
	 */
	public Moteur(Serial serie, int id, int angle_min, int angle_max)
	{
		this.serie = serie;
		this.id = id;
		this.angle_min = angle_min;
        this.angle_max = angle_max;
		desasserv();
	}

	/**
	 * Asservit le moteur à une nouvelle position.
	 * Lève une exception si l'angle ne satisfait pas les bornes.
	 * @param e
	 * @throws GoToException 
	 */
	public void goto_etat(int angle) throws GoToException
	{	
		if(angle >= angle_min && angle <= angle_max)
			try {
				if(serie != null)
					serie.communiquer("#"+Integer.toString(id)+"P"+Integer.toString(angle));
			} catch (SerialException e1) {
				e1.printStackTrace();
			}
		else
			throw new GoToException();
	}

	/**
	 * Désasservit le moteur
	 */
	public void desasserv()
	{
		try {
			if(serie != null)
				serie.communiquer("#"+Integer.toString(id)+"L");
		} catch (SerialException e1) {
			e1.printStackTrace();
		}		
	}
	
}
