package hexapode.markov;

/**
 * Etat d'une patte
 * @author pf
 *
 */
public class EtatPatte {

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

	public void change_moteur(int nbMoteur, int angle)
	{
		em[nbMoteur].angle = angle;
	}
}
