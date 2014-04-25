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
	 * On lève les pattes qu'il faut, puis on les abaisse.
	 * @param e
	 */
	public EtatHexa(String e)
	{
	    for(int i = 0; i < 6; i++)
	        if(e.charAt(i) == '1')
	            epattes[i] = new EtatPatte(i, EtatPatteTest2.AVANT);
	        else
                epattes[i] = new EtatPatte(i, EtatPatteTest2.ARRIERE);
	}
	
	/**
	 * Constructeur si toutes les pattes sont au même état
	 */
	public EtatHexa(EtatPatte e)
	{
		for(int i = 0; i < 6; i++)
			epattes[i] = e;		
	}
	
	/**
	 * Change l'état d'un moteur uniquement
	 * @param nbPatte (entre 0 et 5)
	 * @param nbMoteur (entre 0 et 2)
	 * @param angle
	 */
	public void change_moteur(int nbPatte, int nbMoteur, int angle)
	{
		epattes[nbPatte].em[nbMoteur].angle = angle;
	}
	
	@Override
	public String toString()
	{
		String out = new String();
		for(EtatPatte pate: epattes)
		{
			out += pate.toString();
		}
		return out;
	}
	
}
