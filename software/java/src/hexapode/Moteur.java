package hexapode;

import hexapode.markov.EtatMoteur;
import serial.Serial;
import serial.SerialException;

	/**
	 * Classe des moteurs. Visibilité en "friendly".
	 * @author pf
	 *
	 */

class Moteur {

	private Serial serie;
	private int id;
	
	public Moteur(Serial serie, int id)
	{
		this.serie = serie;
		this.id = id;
	}
	
	/**
	 * Donne l'ordre 
	 * @param e
	 */
	public void goto_etat(EtatMoteur e)
	{	
		String chaines[] = {"m", Integer.toString(id), Float.toString(e.angle)};
		try {
			serie.communiquer(chaines, 0);
		} catch (SerialException e1) {
			e1.printStackTrace();
		}
	}
	
}
