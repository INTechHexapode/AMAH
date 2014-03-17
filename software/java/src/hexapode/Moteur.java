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
	@SuppressWarnings("unused")
	private EtatMoteur etat;
	
	public Moteur(Serial serie, int id, EtatMoteur etat)
	{
		this.etat = etat;
		this.serie = serie;
		this.id = id;
	}
	
	/**
	 * Donne l'ordre 
	 * @param e
	 */
	public void goto_etat(EtatMoteur e)
	{	
		if(e.angle >= 1000 && e.angle <= 2000)
			try {
				serie.communiquer("#"+Integer.toString(id)+"P"+Integer.toString(e.angle));
				etat = e;
			} catch (SerialException e1) {
				e1.printStackTrace();
			}
	}
	
	public void desasserv()
	{
		try {
			serie.communiquer("#"+Integer.toString(id)+"L");
		} catch (SerialException e1) {
			e1.printStackTrace();
		}		
	}
	
}
