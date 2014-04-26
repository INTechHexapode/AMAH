package hexapode;

import hexapode.markov.EnumEtatPatte;
import serial.Serial;

	/**
	 * Classe des pattes, qui contiennent chacune 3 moteurs. Visibilité en "friendly"
	 * @author pf
	 *
	 */

public class Patte {

	public Moteur[] moteurs = new Moteur[3];
	public EnumEtatPatte etat;

    // Constantes
    private static final double a = 60, b = 120; // longueur des pattes
    private static final double r = 70;    // rayon d'une patte posée
    private static final double avancee = 20; // avancée en millimètres
    private static final double hauteur_debout = -100;
    private static final double hauteur_baisse = -120;
    private static final int[] angle_min = {1000, 500, 1000}; 
    private static final int[] angle_max = {1600, 2000, 2000}; 
    private static final double[] angles = {-Math.PI/6., -Math.PI/2., -5.*Math.PI/6., Math.PI/6., Math.PI/2., 5.*Math.PI/6.};

	/**
	 * Constructeur d'une patte
	 * @param serie
	 * @param id
	 * @param etat
	 */
	public Patte(Serial serie, int id)
	{
        this.etat = EnumEtatPatte.OTHER;
		for(int i = 0; i < 3; i++)
			moteurs[i] = new Moteur(serie, 5*id+i+1, angle_min[i], angle_max[i]);
	}
	
	/**
	 * Change l'état de la patte
	 * @param role: le rôle joué par la patte (avant gauche? etc.). Dépend de l'orientation (géré par hexapode)
	 * @param etat: l'état souhaité
	 * @throws GoToException 
	 */
	public void goto_etat(int role, EnumEtatPatte etat) throws GoToException
	{
	    // On n'a pas le droit de demander à aller à OTHER
	    if(etat == EnumEtatPatte.OTHER)
	        throw new GoToException();

	    this.etat = etat;
        double angle = angles[role];
        // Pour additionner deux vecteurs en polaires, il faut forcément repasser en cartésien
        // ATTENTION: ce ne sont pas les mêmes x et y que setEtatMoteurs!
        // (ici, c'est vu du dessus; dans setEtaMoteurs, c'est vu de côté)
        double x = r*Math.sin(angle);
        double y;
        if(etat == EnumEtatPatte.ARRIERE || etat == EnumEtatPatte.POUSSE)
            y = r*Math.cos(angle) - avancee;
        else // DEBOUT ou AVANT
            y = r*Math.cos(angle) + avancee;
    
        double new_r = Math.sqrt(x*x+y*y);
        double new_angle = Math.atan2(x,y)-angle; // on cherche l'angle relatif pour la patte
    
        if(role == 5)
          System.out.println("new_angle: "+new_angle*180./Math.PI);
    
        if(etat == EnumEtatPatte.DEBOUT)
            setEtatMoteurs(new_angle, new_r, hauteur_debout);
        else if(etat == EnumEtatPatte.AVANT)
            setEtatMoteurs(new_angle, new_r, hauteur_baisse);
        else if(etat == EnumEtatPatte.POUSSE)
            setEtatMoteurs(new_angle, new_r, hauteur_baisse-10); // et on pousse un peu sur les pattes
        else if(etat == EnumEtatPatte.ARRIERE)
            setEtatMoteurs(new_angle, new_r, hauteur_baisse);
    }
    
    /**
     * Angle positif: sens horaire.
     * @param angle en radians
     * @throws GoToException 
     */
    private void setEtatMoteurs(double angle) throws GoToException
    {
        angle = angle*180./Math.PI;
        double alpha = -300./30.*(angle+90.)+1500.+300./30.*60.;
        moteurs[0].goto_etat((int)alpha);
    }
    
    private void setEtatMoteurs(double angle, double x, double y) throws GoToException
    {
        setEtatMoteurs(angle);
        setEtatMoteurs(x,y);
    }
    
    private void setEtatMoteurs(double x, double y) throws GoToException
    {
        double alpha = Math.PI-Math.acos((x*x+y*y-a*a-b*b)/(2*a*b));
        double beta = Math.PI-Math.acos((b*b-x*x-y*y-a*a)/(2*a*Math.sqrt(x*x+y*y)));
        double gamma = Math.asin(y/Math.sqrt(x*x+y*y))+Math.PI/2;
        
        double ordre1 = 300./25.*180./Math.PI*(beta+gamma)+(1500.-300./25.*90.);
        double ordre2 = -400./40.*180./Math.PI*alpha+(1600.+400./40.*90.);
        
        moteurs[1].goto_etat((int)ordre1);
        moteurs[2].goto_etat((int)ordre2);
    }
    
	/**
    * Baisse la patte
	 * @throws GoToException 
    */
    public void baisser() throws GoToException
    {
        etat = EnumEtatPatte.OTHER;
        goto_etat(1500, 1200, 1200);
    }
    
    public void goto_etat(int m0, int m1, int m2) throws GoToException
    {
        etat = EnumEtatPatte.OTHER;
        moteurs[0].goto_etat(m0);
        moteurs[1].goto_etat(m1);
        moteurs[2].goto_etat(m2);
    }
	
	/**
	 * Désasservit tous les moteurs de la patte
	 */
	public void desasserv()
	{
        etat = EnumEtatPatte.OTHER;
		for(int i = 0; i < 3; i++)
			moteurs[i].desasserv();	
	}
}
