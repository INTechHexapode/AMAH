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
    @Deprecated
	public EtatHexa()
	{
		for(int i = 0; i < 6; i++)
			epattes[i] = new EtatPatte();
	}
	
	/**
	 * On lève les pattes qu'il faut, puis on les abaisse.
	 * @param e
	 */
    @Deprecated
	public EtatHexa(char[] e)
	{
	    for(int i = 0; i < 6; i++)
	        if(e[i] == '1')
	            epattes[i] = new EtatPatte(i, EtatPatteTest2.AVANT);
	        else
                epattes[i] = new EtatPatte(i, EtatPatteTest2.ARRIERE);
	}
	
	public EtatHexa(String e)
	{
	    for(int i = 0; i < 6; ++i)
	    {
	        if(i < e.length() && e.charAt(i) == '1')
	            epattes[i] = new EtatPatte(i, EtatPatteTest2.AVANT);
	        else
                epattes[i] = new EtatPatte(i, EtatPatteTest2.ARRIERE);
	    }
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
	    String s = new String();
        for(int i = 0; i < 6; i++)
        {
            if(i > 0)
                s += '\n';
            s += epattes[i].toString();
        }
        return s;
	}
	

	public String etatString()
	{
		String out = new String();

        for(int i = 0; i < 6; i++)
        {
        	out += epattes[i].etat();
        }
		return out;
	}
	
}
