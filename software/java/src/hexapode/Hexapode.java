package hexapode;

import hexapode.markov.EtatHexa;
import hexapode.markov.EtatMoteur;
import hexapode.markov.EtatPatte;
import serial.Serial;
import util.Sleep;

/**
 * Classe de l'hexapode, composé de six pattes. 
 * @author pf
 *
 */

public class Hexapode {
	
	public Patte[] pattes;
	private EtatHexa etat_actuel;
	
	public Hexapode(Serial serie)
	{
		etat_actuel = new EtatHexa(new EtatPatte(true));
		pattes = new Patte[6];
		for(int i = 0; i < 6; i++)
			pattes[i] = new Patte(serie, i, etat_actuel.epattes[i]);
		desasserv();
	}

	/**
	 * Change l'état de l'hexapode
	 * @param e
	 */
	public void goto_etat(EtatHexa e)
	{
		for(int i = 0; i < 6; i++)
			pattes[i].goto_etat(e.epattes[i]);
	}

	/**
	 * Désasservit l'hexapode
	 */
	public void desasserv()
	{
		System.out.println("Desasservissement de l'hexapode");
		for(int i = 0; i < 6; i++)
			pattes[i].desasserv();
	}
	
	/**
	 * Lève l'hexapode (en piétinant)
	 */
	public void stand_up()
	{
		System.out.println("L'hexapode se lève");
		// On piétine pour monter
		for(int i = 0; i < 9; i++)
		{
			pietine(i%3, 1500, 1800-25*i, 1600+25*i);
			pietine(5-i%3, 1500, 1800-25*i, 1600+25*i);
		}
		// On baisse toutes les pattes
		for(int i = 0; i < 6; i++)
			pattes[i].baisser();
		Sleep.sleep(500);
		// On abaisse un peu l'hexapode
		for(int i = 0; i < 6; i++)
			pattes[i].goto_etat(new EtatPatte(1500, 1800, 1800));
		
	}

	/**
	 * Utilisé pour piétiner
	 * @param num
	 * @param angle0
	 * @param angle1
	 * @param angle2
	 */
	private void pietine(int num, int angle0, int angle1, int angle2)
	{
		pattes[num].goto_etat(new EtatPatte(angle0, angle1, angle2-400));
		Sleep.sleep(100);
		pattes[num].goto_etat(new EtatPatte(angle0, angle1, angle2));
		Sleep.sleep(100);
	}
	
	/**
	 * Dépose délicatement l'hexapode à terre, couché
	 */
	public void lay_down()
	{
		System.out.println("L'hexapode se couche");
		EtatPatte ep = new EtatPatte(1500, 1900, 1000);
		for(int i = 0; i < 6; i++)
			pattes[i].moteurs[1].goto_etat(new EtatMoteur(2000));
		Sleep.sleep(1000);
		for(int i = 0; i < 6; i++)
			pattes[i].goto_etat(ep);
	}

	/**
	 * Getter de etat_actuel
	 * @return
	 */
	public EtatHexa getEtat_actuel()
	{
		return etat_actuel;
	}
	
/*	Normalement, on n'en a pas besoin. Pour bouger l'hexapode, on passe par goto_etat
	public void setEtat_actuel(EtatHexa etat)
	{
		etat_actuel = etat;
	}*/
	
	/**
	 * Lève la patte i
	 * @param i
	 */
	public void leverPatte(int i)
	{
		pattes[i].lever();
	}

	/**
	 * Baisse la patte i
	 * @param i
	 */
	public void baisserPatte(int i)
	{
		pattes[i].baisser();
	}
	
	/**
	 * Change dans la patte nbPatte le moteur nbMoteur à l'angle donné
	 * @param nbPatte (entre 0 et 5)
	 * @param nbMoteur (entre 0 et 2)
	 * @param angle
	 */
	public void change_moteur(int nbPatte, int nbMoteur, int angle)
	{
		pattes[nbPatte].moteurs[nbMoteur].goto_etat(angle);
	}

}
