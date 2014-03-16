package hexapode;

import hexapode.markov.EtatMoteur;
import hexapode.markov.EtatPatte;
import serial.Serial;

	/**
	 * Classe des pattes, qui contiennent chacune 3 moteurs. Visibilité en "friendly"
	 * @author pf
	 *
	 */

class Patte {

	private Moteur[] moteurs = new Moteur[3];
	
	public Patte(Serial serie, int id)
	{
		for(int i = 0; i < 3; i++)
			moteurs[i] = new Moteur(serie, 5*id+i+1);
	}
	
	public void goto_etat(EtatPatte e)
	{
		for(int i = 0; i < 3; i++)
			moteurs[i].goto_etat(e.em[i]);
	}
	
	public void desasserv()
	{
		for(int i = 0; i < 3; i++)
			moteurs[i].desasserv();	
	}
	
	public void lever()
	{
		goto_etat(new EtatPatte(true));
	}
	
	public void baisser()
	{
		goto_etat(new EtatPatte(false));
	}
}
