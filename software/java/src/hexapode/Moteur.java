package hexapode;

import serial.Serial;

	/**
	 * Classe des moteurs.
	 * @author pf
	 *
	 */

public class Moteur {

	private Serial serie;
	
	public Moteur(Serial serie)
	{
		this.serie = serie;
	}
	
	void goto_etat(Etat e)
	{
		// TODO convertir en angle
	}
	
}
