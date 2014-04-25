package hexapode;

import hexapode.markov.EtatPatte;
import serial.Serial;

	/**
	 * Classe des pattes, qui contiennent chacune 3 moteurs. Visibilité en "friendly"
	 * @author pf
	 *
	 */

public class Patte {

	public Moteur[] moteurs = new Moteur[3];
	public EtatPatte etat;

	/**
	 * Constructeur d'une patte
	 * @param serie
	 * @param id
	 * @param etat
	 */
	public Patte(Serial serie, int id, EtatPatte e)
	{
        this.etat = e;
		for(int i = 0; i < 3; i++)
			moteurs[i] = new Moteur(serie, 5*id+i+1, etat.em[i]);
	}
	
	/**
	 * Change l'état de la patte
	 * @param e
	 */
	public void goto_etat(EtatPatte e)
	{
		for(int i = 0; i < 3; i++)
			moteurs[i].goto_etat(e.em[i]);
        this.etat = e;
	}

	/**
	 * Change l'état de la patte, user-friendly
	 * @param angle0
	 * @param angle1
	 * @param angle2
	 */
	public void goto_etat(int angle0, int angle1, int angle2)
	{
		goto_etat(new EtatPatte(angle0, angle1, angle2));
	}

	/**
	 * Désasservit tous les moteurs de la patte
	 */
	public void desasserv()
	{
		for(int i = 0; i < 3; i++)
			moteurs[i].desasserv();	
	}
	
	/**
	 * Lève la patte
	 */
	public void lever()
	{
		goto_etat(new EtatPatte(true));
	}

	/**
	 * Baisse la patte
	 */
	public void baisser()
	{
		goto_etat(new EtatPatte(false));
	}
	
}
