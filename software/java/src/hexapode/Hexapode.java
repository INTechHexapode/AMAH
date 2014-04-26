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
	
    private Vec2 position; // position à laquelle l'hexapode se croit
	private Patte[][] pattes;
//	private Serial serie; // TODO
	private int direction = 0;
	private int pas = 0; // indice pour la marche
    private static final int[] pattes_rotation = {  3,0,1,4,5,2,
                                                    4,3,0,5,2,1,
                                                    5,4,3,2,1,0,
                                                    2,5,4,1,0,3,
                                                    1,2,5,0,3,4};
    private boolean capteur_active = true;
	private String[] marche;
//	private long date_debut = -1; // TODO
	
	private double[] orthogonal = {};
	private static final double racinede3 = Math.sqrt(3);
	/**
	 * Crée un hexapode. Le boolean inverser est vrai si on est rouge, faux si on est jaune.
	 * @param serie
	 * @param inverser
	 */
	public Hexapode(Serial serie, boolean inverser)
	{
//      this.serie = serie; // TODO

	    position = new Vec2(0,0); // TODO
	    
	    marche = new String[2];
	    marche[0] = new String("101010");
        marche[1] = new String("010101");
        
	    orthogonal = new double[4*6];
	    for(int i = 0; i < 6; i++)
	    {
	        orthogonal[4*i] = Math.cos(i*Math.PI/3-Math.PI/6);
            orthogonal[4*i+1] = Math.sin(i*Math.PI/3-Math.PI/6);
            orthogonal[4*i+2] = Math.cos((i+1)*Math.PI/3+Math.PI/6);
            orthogonal[4*i+3] = Math.sin((i+1)*Math.PI/3+Math.PI/6);
	    }
	    
        pattes = new Patte[6][6];

        // Pattes pour la direction 0
        for(int i = 0; i < 6; i++)
		    pattes[0][i] = new Patte(serie, i);

        // La couleur modifie les directions.
        // On applique une symétrie verticale (la direction 1 devient la 5)
        for(int i = 1; i < 6; i++)
            for(int j = 0; j < 6; j++)
                if(inverser)
                    pattes[6-i][j] = pattes[0][pattes_rotation[(i-1)*6+j]];
                else
                    pattes[i][j] = pattes[0][pattes_rotation[(i-1)*6+j]];

        arret();
        desasserv();
	}
	
	/**
	 * Surcouche user-friendly de setDirection
	 * @param dir
	 */
	public void setDirection(Direction dir)
	{
	    setDirection(dir.ordinal());
	}
	
	/**
	 * Modifie la direction.
	 * @param direction, entre -6 et 6
	 */
	public void setDirection(int direction)
	{
	    // Afin d'avoir direction entre 0 et 5
	    direction += 6;
	    direction %= 6;
	    if(direction !=  this.direction)
	    {
            // TODO: tourner le capteur
            try
            {
        	    EnumEtatPatte[] sauv = new EnumEtatPatte[6];
        	    for(int i = 0; i < 6; i++)
        	        sauv[i] = pattes[direction][i].getEtat();
        	    this.direction = direction;
                for(int i = 0; i < 6; i++)
                    if(sauv[i] != EnumEtatPatte.OTHER)
                        pattes[direction][i].goto_etat(i, sauv[i], Sleep.temps_defaut);
                Sleep.sleep(Sleep.temps_defaut);
            } catch (GoToException e)
            {
                e.printStackTrace();
            }
	    }
	}

	/**
	 * "Tourne" vers la gauche (si différence < 0) ou vers la droite (si différence > 0)
	 * @param difference, entre -5 et +5
	 */
    public void setDirectioneRelatif(int difference)
    {
        setDirection(direction + difference);
    }

    /**
	 * L'hexapode fait l'action donnée par une chaîne binaire.
	 * Note: on peut ignorer une patte en mettant un autre caractère.
	 * @param e
	 */
	public void goto_etat(String e)
	{
        while(detecter_ennemi())
            Sleep.sleep(1000);

        // TODO: quand on aura un jumper
/*        if(System.currentTimeMillis() - date_debut > 90000)
        {
            arret();
            desasserv();
            serie.close();
        }*/

        boolean mouvement = false;

       // on sépare les deux for pour lever/baisser. Ainsi, on lève toutes les pattes intéressées, puis on les abaisse en même temps
       // On ramène en arrière et on lève
	    try {
           for(int i = 0; i < 6; i++)
               if(e.charAt(i) == '1' && pattes[direction][i].getEtat() != EnumEtatPatte.AVANT)
               {
                   mouvement = true;
                   pattes[direction][i].goto_etat(i, EnumEtatPatte.DEBOUT, Sleep.temps_defaut);
               }
               else if(e.charAt(i) == '0' && pattes[direction][i].getEtat() != EnumEtatPatte.ARRIERE)
               {
                   mouvement = true;
                   pattes[direction][i].goto_etat(i, EnumEtatPatte.POUSSE, Sleep.temps_defaut);
               }
           
           // On continue le mouvement que s'il y a un mouvement à continuer
           if(mouvement)
           {
               Sleep.sleep(Sleep.temps_defaut);
    
               for(int i = 0; i < 6; i++) // on baisse
                   if(pattes[direction][i].getEtat() == EnumEtatPatte.DEBOUT)
                       pattes[direction][i].goto_etat(i, EnumEtatPatte.AVANT, Sleep.temps_defaut/4);
                   else if(pattes[direction][i].getEtat() == EnumEtatPatte.POUSSE)
                       pattes[direction][i].goto_etat(i, EnumEtatPatte.ARRIERE, Sleep.temps_defaut/4);
        
               Sleep.sleep(Sleep.temps_defaut/4);
           }
	    }
	    catch(GoToException exception)
	    {
	        exception.printStackTrace();
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
/*	public void lay_down()
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
	}*/
	
	/**
	 * Met l'hexapode en position "normale" (toute patte baissée).
	 */
	public void arret()
	{
        for(int i = 0; i < 6; i++)
            pattes[direction][i].baisser();
        Sleep.sleep();
    }
	
	@Override
	public String toString()
	{
	    String s = new String();
	    for(int i = 0; i < 6; i++)
	    {
	        if(i > 0)
	            s += "\n";
	        s += pattes[direction][i].getEtat().toString();
        }
	    return s;
	}
		
	/**
	 * Avance l'hexapode de "distance" millimètres dans la direction actuelle.
	 * @param distance
	 */
	private void avancer(int distance)
	{
	    int nb_iteration = distance / ((int) Patte.avancee);
	    for(int i = 0; i < nb_iteration; i++)
	        avancer_elementaire();
	}

	/**
     * Suit un itinéraire
	 * @param points
	 */
	public void suit_chemin(Vec2[] points)
	{
	    for(Vec2 point: points)
	        va_au_point(point, true);
	}
	
	/**
	 * Va au point (coordonnées absolues)
	 * @param x
	 * @param y
	 */
	public void va_au_point(Vec2 point, boolean trajectoire_horaire)
	{
	    // On décompose le vecteur (x,y) sur la base formée par les deux vecteurs direction les plus proches de (x,y).
	    // Cette base n'étant pas orthogonale, la formule est peu plus complexe qu'un produit scalaire.
	    // En notant a et b ces deux vecteurs de base, on a (x,y) = C*a+D*b.
	    // Soit c, un vecteur orthogonal à b, et distant de a d'un angle de PI/6.
	    // Alors (x,y).c = C*cos(PI/6) soit C = (x.y).c*2/racine(3).
	    // On recommence avec un c' ortogonal à a.
	    // Le tableau "orthogonal" contient ces c et c'
	    Vec2 relatif = new Vec2(point);
	    relatif.sub(position);
	    point.copy(position);
	    
	    double angle_consigne = Math.atan2(relatif.x,relatif.y);
	    int direction1 = (int)(Math.floor((3*angle_consigne/Math.PI)));
	    int direction2 = direction1+1;
        double longueur1 = 2*(orthogonal[4*((direction1+6)%6)]*relatif.x+orthogonal[4*((direction1+6)%6)+1]*relatif.y)/racinede3;
        double longueur2 = 2*(orthogonal[4*((direction1+6)%6)+2]*relatif.x+orthogonal[4*((direction1+6)%6)+3]*relatif.y)/racinede3;

        if(trajectoire_horaire)
        {
            setDirection(direction1);
            avancer((int)longueur1);
        }
        setDirection(direction2);
        avancer((int)longueur2);
        if(!trajectoire_horaire)
        {
            setDirection(direction1);
            avancer((int)longueur1);
        }
        
	}
	
	/**
	 * Avance de "Patte.avancee" millimètres dans la direction actuelle
	 */
	private void avancer_elementaire()
	{
        goto_etat(marche[pas]);
	    pas++;
	    pas %= marche.length;
	}
	
	private boolean detecter_ennemi()
	{
	    if(!capteur_active)
	        return false;
	    // TODO
	    return false;
	}
	
	/**
	 * Met l'hexapode en "carré" pour le mettre manuellement de manière facile
	 */
	public void recaler()
	{
        try
        {
            pattes[0][0].setEtatMoteurs(Math.PI/6, 70, -90);
            pattes[0][3].setEtatMoteurs(-Math.PI/6, 70, -90);
            pattes[0][1].setEtatMoteurs(0, 70, -90);
            pattes[0][4].setEtatMoteurs(0, 70, -90);
            Sleep.sleep();
        } catch (GoToException e)
        {
            e.printStackTrace();
        }
	}
	
	/**
	 * Pose les fresques
	 */
	public void poser_fresques()
	{
        try
        {
    	    capteur_active = false;
    	    setDirection(Direction.HAUT);
    	    
    	    // On lève la patte 0
            pattes[0][0].setEtatMoteurs(Math.PI/6, 70, -90);

            // On se rapproche du mur qui est loin
            for(int i = 0; i < 4; i++)
            {
                goto_etat("?01010");
                goto_etat("?10101");
            }
            // On redescend la patte 0
            goto_etat("010101");

            // On lève la patte 3
            pattes[0][3].setEtatMoteurs(-Math.PI/6, 70, -90);
            
            // On se rapproche du mur qui est proche
            goto_etat("101?10");
            goto_etat("010?01");
            
            // On repose la patte 3
            goto_etat("010101");
            // TODO mise à jour position
            setDirection(Direction.BAS);
            capteur_active = true;
        } catch (GoToException e)
        {
            e.printStackTrace();
        }

	}

}
