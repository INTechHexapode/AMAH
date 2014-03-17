package hexapode.markov;

import java.util.Random;

/**
 * Contient la matrice de transition. Sérialisable afin d'avoir des données persistantes.
 * @author pf
 *
 */

public class Markov implements java.io.Serializable {

	private static final long serialVersionUID = 1L;
	private double matrice[];
	private Random randomgenerator = new Random();
	private int nb_total = 0;
	
	/**
	 * Constructeur pour une nouvelle matrice
	 * @param dimension
	 */
	public Markov(int nbEtatsParPattes)
	{
		// Dimension = nbEtatsParPattes^6
		int dimension = nbEtatsParPattes*nbEtatsParPattes*nbEtatsParPattes;
		dimension *= dimension;
		dimension = 64;
		matrice = new double[dimension];
		for(int i = 0; i < dimension; i++)
			matrice[i] = 0;
	}
	
	public EtatHexa next()
	{
		nb_total++;
		EtatPatte[] ep;
		int nb_debout;
		do {
			nb_debout = 0;
			ep = new EtatPatte[6];
			for(int i = 0; i < 6; i++)
			{
				boolean debout = randomgenerator.nextBoolean();
				ep[i] = new EtatPatte(debout);
				if(debout)
					nb_debout++;
			}
		} while(nb_debout > 3);
		return new EtatHexa(ep);
	}
	
	public void updateMatrix(int resultat, EtatHexa e)
	{
		matrice[getNum(e)]+=resultat;
		affiche_matrice();
	}
	
	public void affiche_matrice()
	{
		String s = "";
		for(int i = 0; i < 64; i++)
		{
			s += Double.toString(matrice[i])+" ";
		}
		System.out.println(s);
	}
	
	public int getNum(EtatHexa e)
	{
		int num = 0;
		for(int i = 0; i < 6; i++)
		{
			num *= 2;
			if(e.epattes[i].isLeve())
				num++;
		}
		return num;
	}
	
}
