package hexapode;

import hexapode.markov.EtatHexa;
import hexapode.markov.EtatMoteur;
import hexapode.markov.EtatPatte;
import hexapode.markov.EtatPatteTest2;
import serial.Serial;
import util.Sleep;

/**
 * Classe de l'hexapode, composé de six pattes. 
 * @author pf
 *
 */

public class Hexapode {
	
	public Patte[][] pattes;
	private int direction = 0;
	private EtatHexa etat_actuel;
	
	public Hexapode(Serial serie)
	{
	    etat_actuel = new EtatHexa("000000");
        pattes = new Patte[6][6];

        for(int i = 0; i < 6; i++)
		    pattes[0][i] = new Patte(serie, i, etat_actuel.epattes[i]);

		pattes[1][0] = pattes[0][3];
        pattes[1][1] = pattes[0][0];
        pattes[1][2] = pattes[0][1];
        pattes[1][3] = pattes[0][4];
        pattes[1][4] = pattes[0][5];
        pattes[1][5] = pattes[0][2];

        pattes[2][0] = pattes[0][4];
        pattes[2][1] = pattes[0][3];
        pattes[2][2] = pattes[0][0];
        pattes[2][3] = pattes[0][5];
        pattes[2][4] = pattes[0][2];
        pattes[2][5] = pattes[0][1];

        pattes[3][0] = pattes[0][5];
        pattes[3][1] = pattes[0][4];
        pattes[3][2] = pattes[0][3];
        pattes[3][3] = pattes[0][2];
        pattes[3][4] = pattes[0][1];
        pattes[3][5] = pattes[0][0];

        pattes[4][0] = pattes[0][2];
        pattes[4][1] = pattes[0][5];
        pattes[4][2] = pattes[0][4];
        pattes[4][3] = pattes[0][1];
        pattes[4][4] = pattes[0][0];
        pattes[4][5] = pattes[0][3];

        pattes[5][0] = pattes[0][1];
        pattes[5][1] = pattes[0][2];
        pattes[5][2] = pattes[0][5];
        pattes[5][3] = pattes[0][0];
        pattes[5][4] = pattes[0][3];
        pattes[5][5] = pattes[0][4];

		desasserv();
	}
	
	/**
	 * L'hexapode fait l'action donnée par une chaîne binaire.
	 * @param e
	 */
	public void goto_etat(String e)
	{
	    // on sépare les deux for pour lever/baisser. Ainsi, on lève toutes les pattes intéressées, puis on les abaisse en même temps
       // On ramène en arrière et on lève
       for(int i = 0; i < 6; i++)
           if(e.charAt(i) == '1' && pattes[direction][i].etat.etat == EtatPatteTest2.ARRIERE)
               pattes[direction][i].goto_etat(new EtatPatte(i, EtatPatteTest2.DEBOUT));
           else if(e.charAt(i) == '0' && pattes[direction][i].etat.etat == EtatPatteTest2.AVANT)
               pattes[direction][i].goto_etat(new EtatPatte(i, EtatPatteTest2.ARRIERE));

       Sleep.sleep(200);

       boolean attendre = false;

       for(int i = 0; i < 6; i++) // on baisse
           if(e.charAt(i) == '1' && pattes[direction][i].etat.etat == EtatPatteTest2.DEBOUT)
           {
               pattes[direction][i].goto_etat(new EtatPatte(i, EtatPatteTest2.AVANT));
               attendre = true;
           }

       // On n'attend que si on a des pattes à baisser
       if(attendre)
           Sleep.sleep(200);

	}

	/**
	 * Change l'état de l'hexapode
	 * @param e
	 */
	public void goto_etat(EtatHexa e)
	{
		for(int i = 0; i < 6; i++)
			pattes[direction][i].goto_etat(e.epattes[i]);
	}
	
	/**
	 * Désasservit l'hexapode
	 */
	public void desasserv()
	{
		System.out.println("Desasservissement de l'hexapode");
		for(int i = 0; i < 6; i++)
			pattes[direction][i].desasserv();
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
			pattes[direction][i].baisser();
		Sleep.sleep(500);
		// On abaisse un peu l'hexapode
		for(int i = 0; i < 6; i++)
			pattes[direction][i].goto_etat(new EtatPatte(1500, 1800, 1800));
		
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
		pattes[direction][num].goto_etat(new EtatPatte(angle0, angle1, angle2-400));
		Sleep.sleep(100);
		pattes[direction][num].goto_etat(new EtatPatte(angle0, angle1, angle2));
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
			pattes[direction][i].moteurs[1].goto_etat(new EtatMoteur(2000));
		Sleep.sleep(1000);
		for(int i = 0; i < 6; i++)
			pattes[direction][i].goto_etat(ep);
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
		pattes[direction][i].lever();
	}

	/**
	 * Baisse la patte i
	 * @param i
	 */
	public void baisserPatte(int i)
	{
		pattes[direction][i].baisser();
	}
	
	/**
	 * Change dans la patte nbPatte le moteur nbMoteur à l'angle donné
	 * @param nbPatte (entre 0 et 5)
	 * @param nbMoteur (entre 0 et 2)
	 * @param angle
	 */
	public void change_moteur(int nbPatte, int nbMoteur, int angle)
	{
		pattes[direction][nbPatte].moteurs[nbMoteur].goto_etat(angle);
	}

}
