package hexapode;

import java.util.Arrays;

import container.Service;
import hexapode.enums.Direction;
import hexapode.enums.EnumPatte;
import hexapode.enums.Evite;
import hexapode.enums.Marche;
import hexapode.exceptions.BordureException;
import hexapode.exceptions.EnnemiException;
import hexapode.exceptions.GoToException;
import util.Config;

/**
 * Classe de l'hexapode haut niveau.
 * A à sa disposition une classe déplacement, qui s'occupe du bas niveau.
 * @author pf
 *
 */

public class Hexapode implements Service {
	
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
	public Hexapode(Deplacement deplacement)
	{
	    this.deplacement = deplacement;
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
    public boolean initialiser_match()
    {
        try
        {
        	recaler();
            return deplacement.wait_jumper();
        } catch (Exception e)
        {
            // Exception impossible car le capteur est désactivé
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Initialise l'hexapode en position
     */
    public void initialiser()
    {
        deplacement.wait_jumper();
        try
        {
        	for(int i=0; i<6;++i)
            deplacement.lever(i);

        } catch (Exception e)
        {
            // Exception impossible car le capteur est désactivé
            e.printStackTrace();
        }
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
            deplacement.setCapteurOff();
            deplacement.setDirection(Direction.HAUT);
            
            // On lève les pattes 0 et 3
            deplacement.lever_droite(EnumPatte.HAUT_GAUCHE);
            deplacement.lever_gauche(EnumPatte.HAUT_DROITE);

            EnumPatte[] ignore = {EnumPatte.HAUT_DROITE, EnumPatte.HAUT_GAUCHE};
            avancer_pres_bord_en_ignorant(40, ignore);
                        
//            recaler();
            // TODO set position
            deplacement.setDirection(Direction.BAS);
            //deplacement.setCapteurOn();
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
    public void recaler()
    {
        deplacement.lever_droite(EnumPatte.HAUT_GAUCHE);
        deplacement.lever_gauche(EnumPatte.HAUT_DROITE);
        deplacement.lever(1);
        deplacement.lever(2);
        deplacement.lever(4);
        deplacement.lever(5);
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
    
    public void va_au_point_relatif(Vec2 relatif) throws EnnemiException, BordureException
    {
        Vec2 point = relatif.clone();
        point.add(deplacement.getPosition());
        va_au_point(point);
    }
    
	/**
     * Va au point
	 * @param points
	 * @throws BordureException 
	 * @throws EnnemiException 
	 */
	public void va_au_point(Vec2 point) throws EnnemiException, BordureException
	{
        Vec2 relatif = point.clone();
        relatif.sub(deplacement.getPosition());
        // Détermination de l'évitement (on évite du côté où on a de a place)
        Evite evite;
        Vec2 point_gauche = new Vec2(-100*Math.max(point.x, point.y)/point.x, 100*Math.max(point.x, point.y)/point.y);
        Vec2 point_droite = new Vec2(100*Math.max(point.x, point.y)/point.x, -100*Math.max(point.x, point.y)/point.y);
        point_gauche.add(deplacement.getPosition());
        point_droite.add(deplacement.getPosition());
        relatif.x /= 2;
        relatif.y /= 2;
        point_gauche.add(relatif);
        point_droite.add(relatif);
        
        if(point_droite.distance_au_bord() < point_gauche.distance_au_bord())
            evite = Evite.PAR_LA_GAUCHE;
        else
            evite = Evite.PAR_LA_DROITE;
        
//        System.out.println(evite);
        
        try
        {
            va_au_point_direct(point, evite);
        } catch (GoToException e)
        {
            // Problème de moteur: on utilise la méthode plus sûre de va_au_point_indirect
            e.printStackTrace();
            try
            {
                System.out.println("Erreur d'angles, on utilise les six directions usuelles");
                va_au_point_indirect(point, evite);
            } catch (GoToException e1)
            {
                // Cas normalement impossible.
                e1.printStackTrace();
            }
        }
	}

    private void va_au_point_direct(Vec2 point, Evite evite) throws EnnemiException, BordureException, GoToException
    {
        Vec2 relatif = new Vec2(point);
        relatif.sub(deplacement.position);
        
        double angle_consigne = Math.atan2(relatif.x,relatif.y);
        deplacement.setAngle(angle_consigne);
        avancer_et_evite((int) Math.round(relatif.length()), evite);
    }

	/**
	 * Va_au_point avec évitement
	 * @param point
	 * @param trajectoire_horaire
	 * @param insiste
	 * @throws EnnemiException
	 * @throws BordureException 
	 * @throws GoToException 
	 */
	public void va_au_point_indirect(Vec2 point, Evite evite) throws EnnemiException, BordureException, GoToException
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
/*
        System.out.println("relatif: "+relatif);        
        System.out.println("direction1: "+direction1+", direction2: "+direction2);
        System.out.println("orthogonal1: "+orthogonal[1]);
        System.out.println("longueur1: "+longueur1+", longueur2: "+longueur2);
        System.out.println("x: "+(longueur1*Math.cos(Math.PI/2-direction1*Math.PI/3)+longueur2*Math.cos(Math.PI/2-direction2*Math.PI/3)));
        System.out.println("y: "+(longueur1*Math.sin(Math.PI/2-direction1*Math.PI/3)+longueur2*Math.sin(Math.PI/2-direction2*Math.PI/3)));
*/
        // Si trajectoire_horaire est vrai, on tourne à gauche avant de tourner à droite
        // Sinon, c'est le contraire            
        if(evite == Evite.PAR_LA_GAUCHE)
        {
            deplacement.setDirection(direction1);
            avancer_et_evite((int) Math.round(longueur1), evite);
        }
        deplacement.setDirection(direction2);
        avancer_et_evite((int) Math.round(longueur2), evite);
        if(evite == Evite.PAR_LA_DROITE)
        {
            deplacement.setDirection(direction1);
            avancer_et_evite((int) Math.round(longueur1), evite);
        }
	}

	/**
	 * Avance dans la direction actuelle et procède à un évitement si on rencontre un ennemi.
	 * Attention! A la fin de l'évitement, on n'est pas au point où on aurait voulu être. Il
	 * faut donc relancer le mouvement.
	 * @param distance
	 * @throws BordureException
	 * @throws GoToException 
	 * @throws EnnemiException 
	 */
	public void avancer_et_evite(int distance, Evite evite) throws BordureException, GoToException, EnnemiException 
	{
	    try {
	        avancer(distance);
        }
        // l'exception BordureException n'est pas traité et est directement passé au cran supérieur
        catch(EnnemiException e)
        {
            try {
                System.out.println("Evitement de l'ennemi");
                // le coefficient 2 vient de 1/cos(PI/3).
                if(evite == Evite.PAR_LA_GAUCHE)
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
            }
            catch(EnnemiException e2)
            {
                System.out.println("Evitement échoué (on voit encore l'ennemi).");
            }
            catch(BordureException e2)
            {
                System.out.println("Evitement échoué (on s'approche trop près du bord).");
            }
        }
	    
	}
	

    /**
    * Avance l'hexapode de "distance" millimètres dans la direction actuelle.
    * @param distance
    * @throws EnnemiException 
     * @throws GoToException 
    * @throws BordureException 
    */
   public void avancer_pres_bord(int distance) throws EnnemiException, GoToException
   {
       avancer_pres_bord_en_ignorant(distance, null);
   }

    /**
    * Avance l'hexapode de "distance" millimètres dans la direction actuelle.
    * Ignore certaines pattes (null pour en ignorer aucune)
    * @param distance
    * @param ignore
    * @throws EnnemiException 
     * @throws GoToException 
    * @throws BordureException 
    */
   public void avancer_pres_bord_en_ignorant(int distance, EnumPatte[] ignore) throws EnnemiException, GoToException
   {
       int nb_iteration = (int) Math.round(((double)distance) / Patte.getAvancee_effective());
       while(nb_iteration > 0)
       {
           if(deplacement.avancer_elementaire_pres_bord(ignore));
               nb_iteration--;
//           System.out.println(nb_iteration + " " + deplacement.avancer_elementaire_pres_bord(ignore));
       }
   }

   /**
    * Avance l'hexapode de "distance" millimètres dans la direction actuelle.
    * @param distance
    * @throws EnnemiException 
    * @throws BordureException 
 * @throws GoToException 
    */
   public void avancer(int distance) throws EnnemiException, BordureException, GoToException
   {
       avancer_en_ignorant(distance, null);
   }
   
   /**
    * Avance en ignorant certaines pattes, qui ne bougeront donc pas.
    * @param distance
    * @param ignore
    * @throws EnnemiException
    * @throws BordureException
    * @throws GoToException 
    */
   public void avancer_en_ignorant(int distance, EnumPatte[] ignore) throws EnnemiException, BordureException, GoToException
   {
       int nb_iteration = (int) Math.round(((double)distance) / Patte.getAvancee_effective());
       while(nb_iteration > 0)
       {
           if(deplacement.avancer_elementaire(ignore)); // si on a bougé
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
   
   public Vec2 getPosition()
   {
       return deplacement.getPosition();
   }
   
   public boolean isFini()
   {
       return isFini();
   }
   
   public void setAngle(double angle)
   {
       deplacement.setAngle(angle);
   }
   
   /**
    * Fonction de dance
 * @throws GoToException 
    */
   
   public void lever_except(String mode, int[] ignore) throws GoToException
   {
	   	for(int i=0; i<6;++i)
	   		if(!contains(i, ignore))
	   		{System.out.println(Arrays.asList(ignore).contains(i));
	   			if(mode == "milieu")
	   		        deplacement.go_to_angle(i, 1200, 1600, 1700);
	   			else if(mode == "milieu gauche")
	   		   		deplacement.go_to_angle(i, 1500, 2000, 2000);
	   			else if(mode == "bas gauche")
	   		   		deplacement.go_to_angle(i, 1500, 2000, 2000);
	   			else if(mode == "bas")
	   		   		deplacement.go_to_angle(i, 1200, 2000, 2000);
	   			else if(mode == "haut")
	   				deplacement.go_to_angle(i, 1200, 1000, 1000);
	   			else if(mode == "haut gauche")
	   				deplacement.go_to_angle(i, 1500, 1000, 1000);
	   		}
   }
   
   private boolean contains(int i, int[] ignore)
   {
   	for(int in=0; in<ignore.length;++in)
   		if(i == ignore[in])
   			return true;
	return false;
   }
   
   public void lever_milieu() throws GoToException
   {
   	for(int i=0; i<6;++i)
        deplacement.go_to_angle(i, 1200, 1600, 1700);
   }
   

   public void lever_bas() throws GoToException
   {
   	for(int i=0; i<6;++i)
   		deplacement.go_to_angle(i, 1200, 2000, 2000);
   }
   

   public void lever_haut() throws GoToException
   {
   	for(int i=0; i<6;++i)
   		deplacement.go_to_angle(i, 1200, 1000, 1000);
   }
   
}
