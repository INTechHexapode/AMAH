package hexapode.markov;

/**
 * Etat d'une patte
 * @author pf
 *
 */
public class EtatPatte {

	public EtatPatteTest2 etat;
	public EtatMoteur[] em = new EtatMoteur[3];

	/**
	 * Constructeur classique
	 * @param em
	 */
	public EtatPatte(EtatMoteur[] em)
	{
		this.em = em;
	}

	/**
	 * Constructeur aléatoire
	 */
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
	public EtatPatte(boolean leve)
	{
		em[0] = new EtatMoteur(1500);
		if(leve)
			em[1] = new EtatMoteur(2000);
		else
			em[1] = new EtatMoteur(1200);
		em[2] = new EtatMoteur(1200);
	}

	
	public EtatPatte(double angle, double x, double y)
	{
		double a = 60, b = 120;
		em[0] = new EtatMoteur((int)(angle*12.5)+1500);

		double alpha = Math.PI-Math.acos((x*x+y*y-a*a-b*b)/(2*a*b));
		double beta = Math.PI-Math.acos((b*b-x*x-y*y-a*a)/(2*a*Math.sqrt(x*x+y*y)));
		double gamma = Math.asin(y/Math.sqrt(x*x+y*y))+Math.PI/2;
		
        double ordre1 = 300./25.*180./Math.PI*(beta+gamma)+(1500.-300./25.*90.);
        double ordre2 = -400./40.*180./Math.PI*alpha+(1600.+400./40.*90.);
        
        em[1] = new EtatMoteur((int)ordre1);
        em[2] = new EtatMoteur((int)ordre2);
        
        /*		
		double sinc = (carre(x*x+y*y-a*a-b*b)-4*a*a*b*b)/(4*a*a*b*b);
		double c = Math.asin(sinc)/2*180/Math.PI;
		double alpha = Math.atan2(y-b*sinc, b*Math.cos(c)-x)*180/Math.PI;
		System.out.println(alpha);
		System.out.println(c+alpha);
		em[1] = new EtatMoteur((int)((-alpha+210)*12.5));
		em[2] = new EtatMoteur((int)((-c-alpha+210)*12.5));
		System.out.println("moteur 0: "+em[0].angle);
		System.out.println("moteur 1: "+em[1].angle);
		System.out.println("moteur 2: "+em[2].angle);*/
	}
	
	public EtatPatte(EtatPatteTest2 nEtat)
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
	}

	/**
	 * La patte est-elle levée?
	 * @return true si c'est le cas, false sinon
	 */
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

}
