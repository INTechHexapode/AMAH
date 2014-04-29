package hexapode;

import hexapode.capteurs.Capteur;
import hexapode.enums.Direction;
import hexapode.enums.EnumEtatPatte;
import hexapode.enums.EnumPatte;
import hexapode.enums.Marche;
import hexapode.enums.Mode;
import hexapode.exceptions.BordureException;
import hexapode.exceptions.EnnemiException;
import hexapode.exceptions.GoToException;
import serial.Serial;
import util.Sleep;

/**
 * Classe de l'hexapode, composé de six pattes. 
 * Le haut niveau est en haut du fichier, le bas niveau en bas.
 * @author pf
 *
 */

public class Hexapode {
	
    // ATTRIBUTS D'ÉTAT
    private Vec2 position; // position à laquelle l'hexapode se croit
	private Patte[][] pattes;
	private Serial serie;
	private int direction = 0;
	private int pas = 0; // indice pour la marche
	
	// Utilisé afin de pouvoir changer la direction
    private static final int[] pattes_rotation = {  3,0,1,4,5,2,
                                                    4,3,0,5,2,1,
                                                    5,4,3,2,1,0,
                                                    2,5,4,1,0,3,
                                                    1,2,5,0,3,4};
    
    // CAPTEURS
    private boolean capteur_actif = true;
    private static final int distance_detection = 300; // calculé de manière à pouvoir contourner
    private Capteur capteur;

    // DÉPLACEMENTS
    private static final String[][][] marche = {{  {new String("101010"), new String("010101")},
                                                {new String("100000"), new String("100001"), new String("100101"), new String("101101"), new String("000000")},
                                                {new String("001000"), new String("001010"), new String("001011"), new String("011011"), new String("000000")},
                                                {new String("101010"), new String("111111"), new String("000000"), new String("010101"), new String("111111"), new String("000000")}}};
    private Marche marche_actuelle = Marche.BASIQUE;
	private Vec2[] orthogonal = {};
	private static final double racinede3 = Math.sqrt(3);
	private static final int ecart_bordure = 400;
	private static final int attente_avant_evitement = 5000;
	
	private Mode mode = Mode.BIPHASE;
	
	// FIN DE MATCH
    private long date_debut = -1;

    /**
	 * Crée un hexapode. Le boolean inverser est vrai si on est rouge, faux si on est jaune.
	 * @param serie
	 * @param inverser
	 */
	public Hexapode(Serial serie, boolean inverser)
	{
        this.serie = serie;
        capteur = new Capteur(serie);
        
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
        
        setDirection(Direction.HAUT);

        arret();
        desasserv();
 //       position = new Vec2(1300,1800);
        position = new Vec2(0,500);
        }
			
	/**
     * Initialise l'hexapode en le recalant et attend le jumper
     */
    public void initialiser()
    {
        try
        {
            capteur_actif = false;
            recaler();
            setDirection(Direction.GAUCHE_BAS);
            avancer_pres_bord(100);
            setDirection(Direction.BAS);
            avancer_pres_bord(400);
        } catch (EnnemiException e)
        {
            // Exception impossible car le capteur est désactivé
            e.printStackTrace();
        }
        position = new Vec2(1300, 1800);
        while(!capteur.jumper())
            Sleep.sleep(100);
        date_debut = System.currentTimeMillis();
        capteur_actif = true;
    }
    
    /**
     * Pose les fresques
     * Les fresques sont accrochées aux pattes 0 et 3
     * @throws EnnemiException 
     */
    public void poser_fresques()
    {
        marche_actuelle = Marche.RECALAGE;
        try
        {
            capteur_actif = false;
            setDirection(Direction.HAUT);
            
            // On lève les pattes 0 et 3
            pattes[direction][0].lever_droite();
            pattes[direction][3].lever_gauche();

            EnumPatte[] ignore = {EnumPatte.HAUT_DROITE, EnumPatte.HAUT_GAUCHE};
            avancer_pres_bord_en_ignorant(1000, ignore);
                        
//            recaler();
            // TODO set position
            
            setDirection(Direction.BAS);
            capteur_actif = true;
        } catch (Exception e)
        {
            e.printStackTrace();
        }
        marche_actuelle = Marche.BASIQUE;

    }

    /**
     * Se recale dans un coin supérieur droit (si jaune)
     * @throws EnnemiException 
     */
    public void recaler() throws EnnemiException
    {
        marche_actuelle = Marche.RECALAGE;
        setDirection(Direction.DROITE_HAUT);
        pattes[direction][EnumPatte.HAUT_DROITE.ordinal()].lever();
        pattes[direction][EnumPatte.HAUT_GAUCHE.ordinal()].lever_gauche();

        EnumPatte[] ignore = {EnumPatte.HAUT_DROITE, EnumPatte.HAUT_GAUCHE};
        avancer_pres_bord_en_ignorant(1000, ignore);
        arret();
        marche_actuelle = Marche.BASIQUE;
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
        marche_actuelle = Marche.TORCHE;
        EnumPatte patteDebout = EnumPatte.GAUCHE;
        try
        {
            if(patteHorizontale == EnumPatte.GAUCHE)
                patteDebout = EnumPatte.DROITE;
            pattes[direction][patteHorizontale.ordinal()].setEtatMoteurs(0, 160, 0);
            pattes[direction][patteDebout.ordinal()].lever();
            EnumPatte[] ignore = {patteHorizontale, patteDebout};
            avancer_en_ignorant(distance, ignore);
        } catch (GoToException e)
        {
            e.printStackTrace();
        }
        marche_actuelle = Marche.BASIQUE;
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
	    relatif.sub(position);
	    
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
                setDirection(direction1);
                avancer((int) Math.round(longueur1));
            }
            setDirection(direction2);
            avancer((int) Math.round(longueur2));
            if(!trajectoire_horaire)
            {
                setDirection(direction1);
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
                    setDirectionRelatif(-1);
                    avancer(distance_detection*2);
                    setDirectionRelatif(2);
                    avancer(distance_detection*2);
                }
                else
                {
                    // On évite par la droite
                    setDirectionRelatif(1);
                    avancer(distance_detection*2);
                    setDirectionRelatif(-2);
                    avancer(distance_detection*2);                    
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
           Vec2 sauv = new Vec2(position);
           avancer_elementaire_pres_bord(ignore);
           if(!sauv.equals(position))
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
           Vec2 sauv = new Vec2(position);
           avancer_elementaire(ignore);
           if(!sauv.equals(position))
               nb_iteration--;
       }
   }

   /**
    * Fais un pas, en ignorant les bordures
    * @param ignore
    * @throws EnnemiException
    */
   private void avancer_elementaire_pres_bord(EnumPatte[] ignore) throws EnnemiException
   {
       try
       {
           avancer_elementaire(ignore);
       } catch (BordureException e)
       {
       }       
   }


	/**
	 * Avance de "Patte.avancee" millimètres dans la direction actuelle
	 * Ignore certaines pattes, qui resteront dans leur position initiale.
	 * @param ignore
	 * @throws EnnemiException 
	 * @throws BordureException 
	 */
	private void avancer_elementaire(EnumPatte[] ignore) throws EnnemiException, BordureException
	{
	    String prochain_pas = marche[mode.ordinal()][marche_actuelle.indice][pas];

	    // On ignore certaines pattes dans le mouvement
	    if(ignore != null)
    	    for(EnumPatte patte: ignore)
    	        prochain_pas = prochain_pas.substring(0,patte.ordinal()) + "?" + prochain_pas.substring(patte.ordinal()+1);

	    goto_etat(prochain_pas);
	    pas++;
	    pas %= marche[mode.ordinal()][marche_actuelle.indice].length;
	    if(position.x > 1500-ecart_bordure || position.x < -1500+ecart_bordure || position.y > 2000-ecart_bordure || position.y < ecart_bordure)
	        throw new BordureException();
	}
	
	/**
	 * Méthode qui répond à la question "y a-t-il un ennemi devant moi?"
	 * @return true s'il y a un ennemi devant false sinon
	 */
	private boolean detecter_ennemi()
	{
	    if(!capteur_actif)
	        return false;
	    return capteur.mesure() < distance_detection;
	}

	/**
	 * L'hexapode fait une action, biphasée ou triphasée.
	 * @param e
	 * @throws EnnemiException
	 */
    public void goto_etat(String e) throws EnnemiException
    {
        if(mode == Mode.BIPHASE)
            goto_etat_biphase(e);
        else if(mode == Mode.TRIPHASE)
            goto_etat_triphase(e);
    }

    /**
     * L'hexapode fait l'action donnée par une chaîne binaire.
     * Note: on peut ignorer une patte en mettant un autre caractère.
     * @param e
     * @throws EnnemiException 
     */
    private void goto_etat_biphase(String e) throws EnnemiException
    {
        verif_avant_mouvement();
        
        boolean mouvement = false, avance = false;

       // on sépare les deux for pour lever/baisser. Ainsi, on lève toutes les pattes intéressées, puis on les abaisse en même temps
       // On ramène en arrière et on lève
        try {
           for(int i = 0; i < 6; i++)
               if(e.charAt(i) == '1' && pattes[direction][i].getEtat() != EnumEtatPatte.AVANT)
               {
                   mouvement = true;
                   pattes[direction][i].goto_etat(i, EnumEtatPatte.DEBOUT);
               }
               else if(e.charAt(i) == '0' && pattes[direction][i].getEtat() != EnumEtatPatte.ARRIERE)
               {
                   avance = true; // si on ramène une patte en arrière, alors c'est que l'hexapode avance
                   mouvement = true;
                   pattes[direction][i].goto_etat(i, EnumEtatPatte.POUSSE);
               }
           
           // On continue le mouvement que s'il y a un mouvement à continuer
           if(mouvement)
           {
               Sleep.sleep();
    
               for(int i = 0; i < 6; i++) // on baisse
                   if(pattes[direction][i].getEtat() == EnumEtatPatte.DEBOUT)
                       pattes[direction][i].goto_etat(i, EnumEtatPatte.AVANT, Sleep.temps_defaut/4);
                   else if(pattes[direction][i].getEtat() == EnumEtatPatte.POUSSE)
                       pattes[direction][i].goto_etat(i, EnumEtatPatte.ARRIERE, Sleep.temps_defaut/4);
        
               Sleep.sleep(Sleep.temps_defaut/4);

               if(avance)
                   maj_position();
               
               System.out.println(position);

           }
        }
        catch(GoToException exception)
        {
            exception.printStackTrace();
        }

    }

    /**
     * L'hexapode fait l'action donnée par une chaîne ternaire.
     * Note: on peut ignorer une patte en mettant un autre caractère.
     * 0: POUSSE
     * 1: DEBOUT
     * 2: AVANT
     * @param e
     * @throws EnnemiException 
     */
    private void goto_etat_triphase(String e) throws EnnemiException
    {
        verif_avant_mouvement();

        boolean mouvement = false, avance = false;

       // on sépare les deux for pour lever/baisser. Ainsi, on lève toutes les pattes intéressées, puis on les abaisse en même temps
       // On ramène en arrière et on lève
        try {
           for(int i = 0; i < 6; i++)
               if(e.charAt(i) == '0' && pattes[direction][i].getEtat() != EnumEtatPatte.ARRIERE)
               {
                   mouvement = true;
                   avance = true; // si on ramène une patte en arrière, alors c'est que l'hexapode avance
                   pattes[direction][i].goto_etat(i, EnumEtatPatte.POUSSE);
               }
               else if(e.charAt(i) == '1' && pattes[direction][i].getEtat() != EnumEtatPatte.DEBOUT)
               {
                   mouvement = true;
                   pattes[direction][i].goto_etat(i, EnumEtatPatte.DEBOUT);
               }
               else if(e.charAt(i) == '2' && pattes[direction][i].getEtat() != EnumEtatPatte.AVANT)
               {
                   mouvement = true;
                   pattes[direction][i].goto_etat(i, EnumEtatPatte.AVANT);
               }
           
           // On continue le mouvement que s'il y a un mouvement à continuer
           if(mouvement)
           {
               Sleep.sleep();

               for(int i = 0; i < 6; i++) // on relève
                   if(pattes[direction][i].getEtat() == EnumEtatPatte.POUSSE)
                       pattes[direction][i].goto_etat(i, EnumEtatPatte.ARRIERE, Sleep.temps_defaut/4);

               if(avance)
               {
                   Sleep.sleep(Sleep.temps_defaut/4);
                   maj_position();
               }

           }
        }
        catch(GoToException exception)
        {
            exception.printStackTrace();
        }

    }
    
    /**
     * Met à jour la position. On n'a pas d'odométrie, on estime juste.
     */
    private void maj_position()
    {
        position.add(new Vec2((int) Math.round(Patte.avancee_effective*Math.cos(Math.PI/2-direction*Math.PI/3)),
                (int) Math.round(Patte.avancee_effective*Math.sin(Math.PI/2-direction*Math.PI/3))));
//        System.out.println(position);
    }
    
    /**
     * Vérifications avant de bouger. On vérifie:
     * - qu'il n'y a pas d'ennemi
     * - que le temps n'est pas écoulé
     * @throws EnnemiException
     */
    private void verif_avant_mouvement() throws EnnemiException
    {
        int attente = 0;
        
        while(detecter_ennemi())
        {
            System.out.println("Ennemi détecté! Attente.");
            Sleep.sleep(attente_avant_evitement/5);
            attente++;
            if(attente == 5)
                throw new EnnemiException();
        }

        if(date_debut != -1 && System.currentTimeMillis() - date_debut > 90000)
            fin_match();        
    }

	/**
	 * Méthode exécutée au bout de 90s
	 */
	private void fin_match()
    {
        arret();
        desasserv();
        serie.close();
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
     * "Tourne" vers la gauche (si différence < 0) ou vers la droite (si différence > 0)
     * @param difference, entre -5 et +5
     */
    public void setDirectionRelatif(int difference)
    {
        setDirection(direction + difference);
    }

    /**
     * Modifie la direction. Prend du temps, mais c'est nécessaire si on ne veut
     * pas des mouvements parasites qui font bouger un peu le robot.
     * @param direction, entre -6 et 6
     */
    public void setDirection(int direction)
    {
        // Afin d'avoir direction entre 0 et 5
        direction += 6;
        direction %= 6;
        if(direction != this.direction)
        {
            capteur.tourner(direction);
            this.direction = direction;
            try
            {
                EnumEtatPatte[] sauv = new EnumEtatPatte[6];
                for(int i = 0; i < 6; i++)
                    sauv[i] = pattes[direction][i].getEtat();
                
                arret();

                for(int i = 0; i < 3; i++)
                    if(sauv[2*i] != EnumEtatPatte.OTHER)
                        pattes[direction][2*i].goto_etat(2*i, sauv[2*i]);
                Sleep.sleep();
                for(int i = 0; i < 3; i++)
                    if(sauv[2*i+1] != EnumEtatPatte.OTHER)
                        pattes[direction][2*i+1].goto_etat(2*i+1, sauv[2*i+1]);
                Sleep.sleep();

            } catch (GoToException e)
            {
                e.printStackTrace();
            }
            System.out.println("Nouvelle direction: "+this.direction);
        }
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
     * Met l'hexapode en position "normale" (toute patte baissée).
     */
    public void arret()
    {
        try {
            for(int i = 0; i < 3; i++)
                pattes[direction][2*i].lever();
            Sleep.sleep();
            for(int i = 0; i < 3; i++)
                pattes[direction][2*i].goto_etat(2*i, EnumEtatPatte.POSE);
            Sleep.sleep();
            for(int i = 0; i < 3; i++)
                pattes[direction][2*i+1].lever();
            Sleep.sleep();
            for(int i = 0; i < 3; i++)
                pattes[direction][2*i+1].goto_etat(2*i+1, EnumEtatPatte.POSE);
            Sleep.sleep();
        }
        catch(Exception e)
        {
            e.printStackTrace();
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
     * Modifie les constantes (lever les pattes plus haut,
     * faire de plus grands pas, ...)
     */
    public void setProfil(int profil)
    {
        Patte.profil_actuel = profil;
    }

    /**
     * Modifie le mode (biphasé, triphasé)
     * @param mode
     */
    public void setMode(Mode mode)
    {
        this.mode = mode;        
    }
    
    /**
     * Modifie la marche, c'est-à-dire l'enchaînement d'états.
     * @param marche
     */
    public void setMarche(Marche marche)
    {
        this.marche_actuelle = marche;
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

}
