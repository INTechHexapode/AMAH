package hexapode;

import hexapode.markov.EnumEtatPatte;
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
	
	public Hexapode(Serial serie)
	{
        pattes = new Patte[6][6];

        for(int i = 0; i < 6; i++)
		    pattes[0][i] = new Patte(serie, i);

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
	 * Modifie la direction.
	 * @param direction
	 */
	public void setDirectionAbsolue(int direction)
	{
	    // TODO Met à jour l'état
	    this.direction = direction;
	}

	/**
	 * "Tourne" vers la gauche (si différence < 0) ou vers la droite (si différence > 0)
	 * @param difference, entre -5 et +5
	 */
    public void setDirectioneRelatif(int difference)
    {
        setDirectionAbsolue((direction + difference + 6)%6);
    }

    /**
	 * L'hexapode fait l'action donnée par une chaîne binaire.
	 * @param e
     * @throws GoToException 
	 */
	public void goto_etat(String e) throws GoToException
	{
	   boolean mouvement = false;

       // on sépare les deux for pour lever/baisser. Ainsi, on lève toutes les pattes intéressées, puis on les abaisse en même temps
       // On ramène en arrière et on lève
       for(int i = 0; i < 6; i++)
           if(e.charAt(i) == '1' && pattes[direction][i].etat != EnumEtatPatte.AVANT)
           {
               mouvement = true;
               pattes[direction][i].goto_etat(i, EnumEtatPatte.DEBOUT);
           }
           else if(e.charAt(i) == '0' && pattes[direction][i].etat != EnumEtatPatte.ARRIERE)
           {
               mouvement = true;
               pattes[direction][i].goto_etat(i, EnumEtatPatte.POUSSE);
           }

       // On continue le mouvement que s'il y a un mouvement à continuer
       if(mouvement)
       {
           Sleep.sleep(200);

           for(int i = 0; i < 6; i++) // on baisse
               if(pattes[direction][i].etat == EnumEtatPatte.DEBOUT)
                   pattes[direction][i].goto_etat(i, EnumEtatPatte.AVANT);
               else if(pattes[direction][i].etat == EnumEtatPatte.POUSSE)
                   pattes[direction][i].goto_etat(i, EnumEtatPatte.ARRIERE);
    
           Sleep.sleep(200);
       }

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
        try
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
			pattes[direction][i].goto_etat(1500, 1800, 1800);
        } catch (GoToException e)
        {
            e.printStackTrace();
        }
		
	}

	/**
	 * Utilisé pour piétiner
	 * @param num
	 * @param angle0
	 * @param angle1
	 * @param angle2
	 * @throws GoToException 
	 */
	private void pietine(int num, int angle0, int angle1, int angle2) throws GoToException
	{
        pattes[direction][num].goto_etat(angle0, angle1, angle2-400);
		Sleep.sleep(100);
		pattes[direction][num].goto_etat(angle0, angle1, angle2);
		Sleep.sleep(100);
	}

	/**
	 * Dépose délicatement l'hexapode à terre, couché
	 */
	public void lay_down()
	{
	    try {
    		System.out.println("L'hexapode se couche");
    		for(int i = 0; i < 6; i++)
    			pattes[direction][i].moteurs[1].goto_etat(2000);
    		Sleep.sleep(1000);
    		for(int i = 0; i < 6; i++)
    			pattes[direction][i].goto_etat(1500, 1900, 1000);
	    }
	    catch(GoToException e)
	    {
	        e.printStackTrace();
	    }
	}
	
/*	Normalement, on n'en a pas besoin. Pour bouger l'hexapode, on passe par goto_etat
	public void setEtat_actuel(EtatHexa etat)
	{
		etat_actuel = etat;
	}*/
	
	public String toString()
	{
	    String s = new String();
	    for(int i = 0; i < 6; i++)
	    {
	        if(i > 0)
	            s += "\n";
	        s += pattes[direction][i].etat.toString();
        }
	    return s;
	}

}
