package hexapode.markov;

/**
 * Contient l'état de l'hexapode. Manipulé par la chaîne de Markov et l'hexapode.
 * @author pf
 *
 */

public class EtatHexa {

	public EtatPatte[] epattes = new EtatPatte[6];

	/**
	 * Constructeur classique
	 * @param epattes
	 */
	public EtatHexa(EtatPatte[] epattes)
	{
		this.epattes = epattes;
	}
	
	/**
	 * Constructeur aleatoire
	 */
	public EtatHexa()
	{
		for(int i = 0; i < 6; i++)
			epattes[i] = new EtatPatte();
	}
	
	/**
	 * Constructeur si toutes les pattes sont au même état
	 */
	public EtatHexa(EtatPatte e)
	{
		for(int i = 0; i < 6; i++)
			epattes[i] = e;		
	}
	
	public void change_moteur(int nbPatte, int nbMoteur, int angle)
	{
		epattes[nbPatte].change_moteur(nbMoteur, angle);
	}
	
}
