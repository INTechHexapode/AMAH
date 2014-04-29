package hexapode;

import hexapode.enums.Direction;
import hexapode.enums.EnumPatte;
import hexapode.enums.Marche;
import hexapode.exceptions.BordureException;
import hexapode.exceptions.EnnemiException;
import serial.Serial;
import util.Config;

/**
 * Classe de l'hexapode haut niveau.
 * A à sa disposition une classe déplacement, qui s'occupe du bas niveau.
 * @author pf
 *
 */

public class Hexapode {
	
    // DÉPLACEMENTS
    private Deplacement deplacement;
	private Vec2[] orthogonal = {};
	private static final double racinede3 = Math.sqrt(3);
	
    /**
	 * Crée un hexapode. Le boolean inverser est vrai si on est rouge, faux si on est jaune.
	 * @param serie
	 * @param inverser
	 * @param maj_position. On doit parfois désactiver la mise à jour de position,
	 * afin de ne plus lever de BordureException.
	 */
	public Hexapode(Serial serie, boolean inverser, boolean maj_position)
	{
	    deplacement = new Deplacement(serie, inverser, maj_position);

	    orthogonal = new Vec2[2*6];

	    for(int i = 0; i < 6; i++)
	    {
            // orthogonal à la direction de gauche
            orthogonal[2*i] = new Vec2(Math.cos(Math.PI/2-i*Math.PI/3+Math.PI/6),
                    Math.sin(Math.PI/2-i*Math.PI/3+Math.PI/6));
            // orthogonal à la direction de droite
            orthogonal[2*i+1] = new Vec2(Math.cos(Math.PI/2-(i+2)*Math.PI/3+Math.PI/6),
                    Math.sin(Math.PI/2-(i+2)*Math.PI/3+Math.PI/6));
	    }
	    
    }
			
	/**
     * Initialise l'hexapode en le recalant et attend le jumper
     */
    public void initialiser()
    {
        try
        {
            recaler();
            deplacement.setDirection(Direction.GAUCHE_BAS);
            avancer_pres_bord(100);
            deplacement.setDirection(Direction.BAS);
            avancer_pres_bord(400);
        } catch (EnnemiException e)
        {
            // Exception impossible car le capteur est désactivé
            e.printStackTrace();
        }
        deplacement.wait_jumper();
    }
    
    /**
     * Pose les fresques
     * Les fresques sont accrochées aux pattes 0 et 3
     * @throws EnnemiException 
     */
    public void poser_fresques()
    {
        deplacement.setMarche(Marche.RECALAGE);
        try
        {
            deplacement.capteur_actif = false;
            deplacement.setDirection(Direction.HAUT);
            
            // On lève les pattes 0 et 3
            deplacement.lever_droite(EnumPatte.HAUT_GAUCHE);
            deplacement.lever_gauche(EnumPatte.HAUT_DROITE);

            EnumPatte[] ignore = {EnumPatte.HAUT_DROITE, EnumPatte.HAUT_GAUCHE};
            avancer_pres_bord_en_ignorant(1000, ignore);
                        
//            recaler();
            // TODO set position
            
            deplacement.setDirection(Direction.BAS);
            deplacement.capteur_actif = true;
        } catch (Exception e)
        {
            e.printStackTrace();
        }
        deplacement.setMarche(Marche.BASIQUE);
    }

    /**
     * Se recale dans un coin supérieur droit (si jaune)
     * @throws EnnemiException 
     */
    public void recaler() throws EnnemiException
    {
        deplacement.setMarche(Marche.RECALAGE);
        deplacement.setDirection(Direction.DROITE_HAUT);
        deplacement.lever(EnumPatte.HAUT_DROITE);
        deplacement.lever_gauche(EnumPatte.HAUT_GAUCHE);

        EnumPatte[] ignore = {EnumPatte.HAUT_DROITE, EnumPatte.HAUT_GAUCHE};
        avancer_pres_bord_en_ignorant(1000, ignore);
        deplacement.arret();
        deplacement.setMarche(Marche.BASIQUE);
    }

    /**
     * Fait avancer l'hexapode et fait tomber un feu d'un certain côté
     * L'hexapode tend une patte et marche sans elle.
     * NOTE: à utiliser pour la torche, pas pour les feux debout
     * @param distance
     * @throws BordureException 
     * @throws EnnemiException 
     */
    public void avancer_tomber_feu(int distance, EnumPatte patteHorizontale) throws EnnemiException, BordureException
    {
        deplacement.setMarche(Marche.TORCHE);
        EnumPatte patteDebout = EnumPatte.GAUCHE;
        try
        {
            if(patteHorizontale == EnumPatte.GAUCHE)
                patteDebout = EnumPatte.DROITE;
//            pattes[direction][patteHorizontale.ordinal()].setEtatMoteurs(0, 160, 0); // TODO
            deplacement.lever(patteDebout);
            EnumPatte[] ignore = {patteHorizontale, patteDebout};
            avancer_en_ignorant(distance, ignore);
        } catch (Exception e)
        {
            e.printStackTrace();
        }
        deplacement.setMarche(Marche.BASIQUE);
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
	    try
        {
            va_au_point(point, trajectoire_horaire, true);
        } catch (Exception e)
        {
            e.printStackTrace();
        }
	}
	
	/**
	 * Va_au_point avec évitement
	 * @param point
	 * @param trajectoire_horaire
	 * @param insiste
	 * @throws EnnemiException
	 * @throws BordureException 
	 */
	private void va_au_point(Vec2 point, boolean trajectoire_horaire, boolean insiste) throws EnnemiException, BordureException
	{
	    // On décompose le vecteur (x,y) sur la base formée par les deux vecteurs direction les plus proches de (x,y).
	    // Cette base n'étant pas orthogonale, la formule est peu plus complexe qu'un produit scalaire.
	    // En notant a et b ces deux vecteurs de base, on a (x,y) = C*a+D*b.
	    // Soit c, un vecteur orthogonal à b, et distant de a d'un angle de PI/6.
	    // Alors (x,y).c = C*cos(PI/6) soit C = (x.y).c*2/racine(3).
	    // On recommence avec un c' ortogonal à a.
	    // Le tableau "orthogonal" contient ces c et c'
	    Vec2 relatif = new Vec2(point);
	    relatif.sub(deplacement.position);
	    
	    double angle_consigne = Math.atan2(relatif.x,relatif.y);
	    int direction1 = (int)(Math.floor((3*angle_consigne/Math.PI)));
	    int direction2 = direction1+1;
	    
        
        double longueur1 = 2*(Vec2.scalaire(relatif, orthogonal[2*((direction1+6)%6)]))/racinede3;
        double longueur2 = 2*(Vec2.scalaire(relatif, orthogonal[2*((direction1+6)%6)+1]))/racinede3;

        System.out.println("relatif: "+relatif);        
        System.out.println("direction1: "+direction1+", direction2: "+direction2);
        System.out.println("orthogonal1: "+orthogonal[1]);
        System.out.println("longueur1: "+longueur1+", longueur2: "+longueur2);
        System.out.println("x: "+(longueur1*Math.cos(Math.PI/2-direction1*Math.PI/3)+longueur2*Math.cos(Math.PI/2-direction2*Math.PI/3)));
        System.out.println("y: "+(longueur1*Math.sin(Math.PI/2-direction1*Math.PI/3)+longueur2*Math.sin(Math.PI/2-direction2*Math.PI/3)));

        try {
            // Si trajectoire_horaire est vrai, on tourne à gauche avant de tourner à droite
            // Sinon, c'est le contraire            
            if(trajectoire_horaire)
            {
                deplacement.setDirection(direction1);
                avancer((int) Math.round(longueur1));
            }
            deplacement.setDirection(direction2);
            avancer((int) Math.round(longueur2));
            if(!trajectoire_horaire)
            {
                deplacement.setDirection(direction1);
                avancer((int) Math.round(longueur1));
            }
        }
        // l'exception BordureException n'est pas traité et est directement passé au cran supérieur
        catch(EnnemiException e)
        {
            if(!insiste)
                throw e;
            try {
                System.out.println("Evitement de l'ennemi");
                // le coefficient 2 vient de 1/cos(PI/3).
                if(trajectoire_horaire)
                {
                    // On évite par la gauche
                    deplacement.setDirectionRelatif(-1);
                    avancer(Config.distance_detection*2);
                    deplacement.setDirectionRelatif(2);
                    avancer(Config.distance_detection*2);
                }
                else
                {
                    // On évite par la droite
                    deplacement.setDirectionRelatif(1);
                    avancer(Config.distance_detection*2);
                    deplacement.setDirectionRelatif(-2);
                    avancer(Config.distance_detection*2);                    
                }
                va_au_point(point, trajectoire_horaire, false);
            }
            catch(EnnemiException e2)
            {
                System.out.println("Evitement échoué (on voit encore l'ennemi). On passe au point suivant.");
            }
            catch(BordureException e2)
            {
                System.out.println("Evitement échoué (on s'approche trop près du bord). On passe au point suivant.");
            }
        }
	}


    /**
    * Avance l'hexapode de "distance" millimètres dans la direction actuelle.
    * @param distance
    * @throws EnnemiException 
    * @throws BordureException 
    */
   public void avancer_pres_bord(int distance) throws EnnemiException
   {
       avancer_pres_bord_en_ignorant(distance, null);
   }

    /**
    * Avance l'hexapode de "distance" millimètres dans la direction actuelle.
    * Ignore certaines pattes (null pour en ignorer aucune)
    * @param distance
    * @param ignore
    * @throws EnnemiException 
    * @throws BordureException 
    */
   public void avancer_pres_bord_en_ignorant(int distance, EnumPatte[] ignore) throws EnnemiException
   {
       int nb_iteration = (int) Math.round(distance / Patte.avancee_effective);
       while(nb_iteration > 0)
       {
           Vec2 sauv = new Vec2(deplacement.position);
           deplacement.avancer_elementaire_pres_bord(ignore);
           if(!sauv.equals(deplacement.position))
               nb_iteration--;
       }
   }

   /**
    * Avance l'hexapode de "distance" millimètres dans la direction actuelle.
    * @param distance
    * @throws EnnemiException 
    * @throws BordureException 
    */
   public void avancer(int distance) throws EnnemiException, BordureException
   {
       avancer_en_ignorant(distance, null);
   }
   
   /**
    * Avance en ignorant certaines pattes, qui ne bougeront donc pas.
    * @param distance
    * @param ignore
    * @throws EnnemiException
    * @throws BordureException
    */
   public void avancer_en_ignorant(int distance, EnumPatte[] ignore) throws EnnemiException, BordureException
   {
       int nb_iteration = (int) Math.round(distance / Patte.avancee_effective);
       while(nb_iteration > 0)
       {
           Vec2 sauv = new Vec2(deplacement.position);
           deplacement.avancer_elementaire(ignore);
           if(!sauv.equals(deplacement.position))
               nb_iteration--;
       }
   }
   
   /**
    * Désasservit l'hexapode.
    */
   public void desasserv()
   {
       deplacement.desasserv();
   }

}
