package hexapode.markov;

/**
 * Etat d'une patte
 * @author pf
 *
 */
public class EtatPatte {

	public EtatPatteTest2 etat;
	public EtatMoteur[] em = new EtatMoteur[3];

	// Longueur des pattes
    private static final double a = 60, b = 120;
    private static final double r = 70;    // rayon d'une patte posée
    private static final double avancee = 40; // avancée en millimètres
    private static final double hauteur_debout = -90;
    private static final double hauteur_baisse = -130;

    // On peut augmenter avancee à condition d'augmenter r.
    // Exemple: r = 80, avancee = 45
    
	/**
	 * Constructeur classique
	 * @param em
	 */
    @Deprecated
	public EtatPatte(EtatMoteur[] em)
	{
		this.em = em;
	}

	/**
	 * Constructeur aléatoire
	 */
    @Deprecated
	public EtatPatte()
	{
		for(int i = 0; i < 3; i++)
			em[i] = new EtatMoteur();
	}

	/**
	 * Constructeur user-friendly
	 * @param angle0
	 * @param angle1
	 * @param angle2
	 */
    @Deprecated
	public EtatPatte(int angle0, int angle1, int angle2)
	{
		em[0] = new EtatMoteur(angle0);
		em[1] = new EtatMoteur(angle1);
		em[2] = new EtatMoteur(angle2);
	}
	
	/**
	 * Constructeur de patte debout ou baissée
	 * @param leve
	 */
	@Deprecated
	public EtatPatte(boolean leve)
	{
		em[0] = new EtatMoteur(1500);
		if(leve)
			em[1] = new EtatMoteur(2000);
		else
			em[1] = new EtatMoteur(1200);
		em[2] = new EtatMoteur(1200);
	}
	
	public EtatPatte(int nb, int etat)
	{
	    this(nb, EtatPatteTest2.getEtat(etat));
	}
	
	public EtatPatte(int nb, EtatPatteTest2 etat)
	{
	    this.etat = etat;
	    double[] angles = {-30-25, -90-25, -150-25, 30-25, 90-25, 150-25};
	    double angle = angles[nb]*Math.PI/180.;
	    // Pour additionner deux vecteurs en polaires, il faut forcément repasser en cartésien
	    // ATTENTION: ce ne sont pas les mêmes x et y que setEtatMoteurs!
	    // (ici, c'est vu du dessus; dans setEtaMoteurs, c'est vu de côté)
	    double x = r*Math.sin(angle);
	    double y;
        if(etat == EtatPatteTest2.ARRIERE || etat == EtatPatteTest2.POUSSE)
            y = r*Math.cos(angle) - avancee;
        else // DEBOUT ou AVANT
            y = r*Math.cos(angle) + avancee;

//        System.out.println("x: "+x+", y: "+y);
                
	    double new_r = Math.sqrt(x*x+y*y);
	    double new_angle = -25.*Math.PI/180.-Math.atan2(x, y)+angle; // on cherche l'angle relatif pour la patte

        if(nb == 5)
          System.out.println("new_angle: "+new_angle*180./Math.PI);

//	    System.out.println("angle début: "+angle+", angle fin: "+Math.atan2(x,y));

//	    System.out.println("new_angle: "+new_angle);
	    
	    if(etat == EtatPatteTest2.DEBOUT)
	        setEtatMoteurs(new_angle, new_r, hauteur_debout);
	    else if(etat == EtatPatteTest2.AVANT)
            setEtatMoteurs(new_angle, new_r, hauteur_baisse);
        else if(etat == EtatPatteTest2.POUSSE)
            setEtatMoteurs(new_angle, new_r, hauteur_baisse-10); // et on pousse un peu sur les pattes
        else if(etat == EtatPatteTest2.ARRIERE)
            setEtatMoteurs(new_angle, new_r, hauteur_baisse); // et on pousse un peu sur les pattes
	}

	/**
	 * Angle positif: sens horaire. Doit être compris entre -60° et 10°
	 * @param angle en degré
	 */
    private void setEtatMoteurs(double angle)
    {
        angle = angle*180./Math.PI;
//        System.out.println("Angle: "+angle);
        double alpha = -300./30.*(-25.+90.)+1500.+300./30.*60.;
        if(angle >= -60 && angle <= 10)
            alpha = -300./30.*(angle+90.)+1500.+300./30.*60.;
        else
            System.out.println("L'angle doit être compris entre -60° et 10° (angle demandé: "+angle+")");
        em[0] = new EtatMoteur((int)alpha);
    }
   
    private void setEtatMoteurs(double angle, double x, double y)
    {
        setEtatMoteurs(angle);
        setEtatMoteurs(x,y);
    }
   
	private void setEtatMoteurs(double x, double y)
	{
        double alpha = Math.PI-Math.acos((x*x+y*y-a*a-b*b)/(2*a*b));
        double beta = Math.PI-Math.acos((b*b-x*x-y*y-a*a)/(2*a*Math.sqrt(x*x+y*y)));
        double gamma = Math.asin(y/Math.sqrt(x*x+y*y))+Math.PI/2;
        
        double ordre1 = 300./25.*180./Math.PI*(beta+gamma)+(1500.-300./25.*90.);
        double ordre2 = -400./40.*180./Math.PI*alpha+(1600.+400./40.*90.);
        
        em[1] = new EtatMoteur((int)ordre1);
        em[2] = new EtatMoteur((int)ordre2);	    
	}
	
	@Deprecated
	public EtatPatte(double angle, double x, double y)
	{
	    setEtatMoteurs(angle, x, y);
	}
	
/*	public EtatPatte(EtatPatteTest2 nEtat)
	{
		etat = nEtat;
		if(nEtat == EtatPatteTest2.LEVE || nEtat == EtatPatteTest2.BAISSE)
			em[0] = new EtatMoteur(1500);
		else if(nEtat == EtatPatteTest2.ARRIERE)
			em[0] = new EtatMoteur(1200);
		else if(nEtat == EtatPatteTest2.AVANT)
			em[0] = new EtatMoteur(1800);
		if(nEtat == EtatPatteTest2.LEVE)
			em[1] = new EtatMoteur(2000);
		else
			em[1] = new EtatMoteur(1200);
		if(nEtat == EtatPatteTest2.LEVE)
			em[2] = new EtatMoteur(1800);
		else
			em[2] = new EtatMoteur(1200);
	}*/

	/**
	 * La patte est-elle levée?
	 * @return true si c'est le cas, false sinon
	 */
	@Deprecated
	public boolean isLeve()
	{
		return equals(new EtatPatte(true));
	}

	/**
	 * Méthode equals
	 */
	@Override
	public boolean equals(Object e)
	{
		return e instanceof EtatPatte
				&& ((EtatPatte) e).em[0].angle == em[0].angle
				&& ((EtatPatte) e).em[1].angle == em[1].angle
				&& ((EtatPatte) e).em[2].angle == em[2].angle;
	}
	
	@Override
	public String toString()
	{
	    return etat.toString();
	}
	

	public char etat()
	{
	    if(etat == EtatPatteTest2.AVANT)
	    	return '1';
	    else
	    	return '0';
	}

}
