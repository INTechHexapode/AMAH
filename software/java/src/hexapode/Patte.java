package hexapode;

import hexapode.exceptions.GoToException;
import hexapode.markov.EnumEtatPatte;
import serial.Serial;
import util.Sleep;

	/**
	 * Classe des pattes, qui contiennent chacune 3 moteurs. Visibilité en "friendly"
	 * Retient son état, il faut donc toujours veiller à le mettre à jour.
	 * @author pf
	 *
	 */

class Patte {

	private TriMoteur moteurs;
	private EnumEtatPatte etat;

	public static int profil_actuel = 0;
	public static final int profil_basique = 0;
    public static final int profil_enjamber = 1;	
	
    // Constantes
    private static final double a = 60, b = 120; // longueur des pattes
    private static final double[] r = {60, 60};    // rayon d'une patte posée
    private static final double[] avancee = {30, 10}; // avancée en millimètres
    private static final double[] hauteur_debout = {-80, -80};
    private static final double[] hauteur_baisse = {-100, -150};
    private static final double[] hauteur_pousse = {-110, -160};
    private static final double[] angles = {-Math.PI/6, -Math.PI/2, -5*Math.PI/6, Math.PI/6, Math.PI/2, 5*Math.PI/6};
    public static double avancee_effective = 1; //avancee[profil_actuel]*130./50.; // mesurée
    
	/**
	 * Constructeur d'une patte
	 * @param serie
	 * @param id
	 * @param etat
	 */
	public Patte(Serial serie, int id)
	{
        this.etat = EnumEtatPatte.OTHER;
        moteurs = new TriMoteur(serie, 5*id+1);
	}
	
	/**
	 * Surcouche de goto_etat, avec temps de parcours par défaut.
	 * @param role
	 * @param etat
	 * @throws GoToException
	 */
	public void goto_etat(int role, EnumEtatPatte etat) throws GoToException
	{
	    goto_etat(role, etat, Sleep.temps_defaut);
	}
	
	/**
	 * Change l'état de la patte
	 * @param role: le rôle joué par la patte (avant gauche? etc.). Dépend de l'orientation (géré par hexapode)
	 * @param etat: l'état souhaité
	 * @throws GoToException 
	 */
	public void goto_etat(int role, EnumEtatPatte etat, int temps) throws GoToException
	{
	    // On n'a pas le droit de demander à aller à OTHER
	    if(etat == EnumEtatPatte.OTHER)
	        throw new GoToException();
	    if(etat == EnumEtatPatte.DEBOUT)
	        lever();
	    else
	    {
    	    double angle = angles[role];
    
            // Pour additionner deux vecteurs en polaires, il faut forcément repasser en cartésien
            // ATTENTION: ce ne sont pas les mêmes x et y que setEtatMoteurs!
            // (ici, c'est vu du dessus; dans setEtaMoteurs, c'est vu de côté)
            double x = r[profil_actuel]*Math.sin(angle);
            double y;
            if(etat == EnumEtatPatte.ARRIERE || etat == EnumEtatPatte.POUSSE)
                y = r[profil_actuel]*Math.cos(angle) - avancee[profil_actuel];
            else if(etat == EnumEtatPatte.DEBOUT || etat == EnumEtatPatte.AVANT)
                y = r[profil_actuel]*Math.cos(angle) + avancee[profil_actuel];
            else // HAUT
                y = r[profil_actuel]*Math.cos(angle);
        
            double new_r = Math.sqrt(x*x+y*y);
            double new_angle = Math.atan2(x,y)-angle; // on cherche l'angle relatif pour la patte
            
            if(etat == EnumEtatPatte.DEBOUT)
                setEtatMoteurs(new_angle, new_r, hauteur_debout[profil_actuel], temps);
            else if(etat == EnumEtatPatte.AVANT)
                setEtatMoteurs(new_angle, new_r, hauteur_baisse[profil_actuel], temps);
            else if(etat == EnumEtatPatte.POUSSE)
                setEtatMoteurs(new_angle, new_r, hauteur_pousse[profil_actuel], temps); // et on pousse un peu sur les pattes
            else if(etat == EnumEtatPatte.ARRIERE)
                setEtatMoteurs(new_angle, new_r, hauteur_baisse[profil_actuel], temps);
            else if(etat == EnumEtatPatte.HAUT)
                setEtatMoteurs(new_angle, new_r, hauteur_debout[profil_actuel], temps);
	    }
        this.etat = etat; // le faire après setEtatMoteur qui met l'état à OTHER
    }
    
    /**
     * Angle positif: sens horaire.
     * @param angle en radians
     * @param x en mm
     * @param y en mm
     * @throws GoToException 
     */    
	public void setEtatMoteurs(double angle, double x, double y) throws GoToException
	{
	    setEtatMoteurs(angle, x, y, Sleep.temps_defaut);
	}
	
    /**
     * Angle positif: sens horaire.
     * @param angle en radians
     * @param x en mm
     * @param y en mm
     * @param temps en ms
     * @throws GoToException 
     */    
    public void setEtatMoteurs(double angle, double x, double y, int temps) throws GoToException
    {
        etat = EnumEtatPatte.OTHER;
        int[] ordres = new int[3];
        double alpha = Math.PI-Math.acos((x*x+y*y-a*a-b*b)/(2*a*b));
        double beta = Math.PI-Math.acos((b*b-x*x-y*y-a*a)/(2*a*Math.sqrt(x*x+y*y)));
        double gamma = Math.asin(y/Math.sqrt(x*x+y*y))+Math.PI/2;
        
        ordres[1] = (int) Math.round(300./25.*180./Math.PI*(beta+gamma)+(1500.-300./25.*90.));
        ordres[2] = (int) Math.round(-400./40.*180./Math.PI*alpha+(1600.+400./40.*90.));        
        ordres[0] = (int) Math.round(-300./30.*(angle*180./Math.PI+90.)+1500.+300./30.*60.);
        
        moteurs.goto_etat(ordres, temps);
    }
    
	/**
    * Baisse la patte
    */
    public void baisser()
    {
        try {
            goto_etat(1200, 1200, 1200);
//            setEtatMoteurs(0, 60, -120);
        }
        catch(GoToException e)
        {
            e.printStackTrace();
        }
    }

    
    /**
    * Lève la patte
    */
    public void lever()
    {
        try {
            goto_etat(1200, 2000, 2000);
        }
        catch(GoToException e)
        {
            e.printStackTrace();
        }
    }

    /**
    * Lève la patte vers la gauche
    */
    public void lever_gauche()
    {
        try {
            goto_etat(1500, 2000, 2000);
        }
        catch(GoToException e)
        {
            e.printStackTrace();
        }
    }

    /**
    * Lève la patte vers la droite
    */
    public void lever_droite()
    {
        try {
            goto_etat(900, 2000, 2000);
        }
        catch(GoToException e)
        {
            e.printStackTrace();
        }
    }

    /**
     * Affecte aux moteurs les angles données.
     * @param m0
     * @param m1
     * @param m2
     * @throws GoToException
     */
    public void goto_etat(int m0, int m1, int m2) throws GoToException
    {
        goto_etat(m0, m1, m2, Sleep.temps_defaut);
    }

    /**
     * Affecte aux moteurs les angles données en un certain temps.
     * @param m0
     * @param m1
     * @param m2
     * @param temps
     * @throws GoToException
     */
    public void goto_etat(int m0, int m1, int m2, int temps) throws GoToException
    {
        etat = EnumEtatPatte.OTHER;
        int[] ordres = {m0, m1, m2};
        moteurs.goto_etat(ordres, temps);
    }
	
	/**
	 * Désasservit tous les moteurs de la patte
	 */
	public void desasserv()
	{
        etat = EnumEtatPatte.OTHER;
	    moteurs.desasserv();
	}
	
	/**
	 * Retourne l'état de la patte
	 * @return
	 */
	public EnumEtatPatte getEtat()
	{
	    return etat;
	}
}
