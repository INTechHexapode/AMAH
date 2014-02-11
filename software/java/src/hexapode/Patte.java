package hexapode;

import hexapode.markov.EtatPatte;
import serial.Serial;

	/**
	 * Classe des pattes, qui contiennent chacune 3 moteurs
	 * @author pf
	 *
	 */

class Patte {

	private Moteur[] moteurs;
	
	public Patte(Serial serie, int id)
	{
		for(int i = 0; i < 3; i++)
			moteurs[i] = new Moteur(serie, 10*id+i);
	}
	
	public void goto_etat(EtatPatte e)
	{
		for(int i = 0; i < 3; i++)
			moteurs[i].goto_etat(e.em[i]);
	}
	
}
