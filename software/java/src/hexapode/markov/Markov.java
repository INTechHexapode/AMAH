package hexapode.markov;

import java.util.Random;

/**
 * Contient la matrice de transition. Sérialisable afin d'avoir des données persistantes.
 * Changera à chaque changement de tests
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
		EtatPatte[] ep = new EtatPatte[6];
/*		nb_total++;
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
		} while(nb_debout > 3);*/
		for(int j = 0; j < 6; j++)
		{
		int i = randomgenerator.nextInt()%4;
		if(i == 0)
			ep[j] = new EtatPatte(EtatPatteTest2.ARRIERE);
		else if(i == 1)
			ep[j] = new EtatPatte(EtatPatteTest2.AVANT);
		else if(i == 2)
			ep[j] = new EtatPatte(EtatPatteTest2.LEVE);
		else
			ep[j] = new EtatPatte(EtatPatteTest2.BAISSE);
		}
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
	
	public double getMat(EtatHexa e)
	{
		return matrice[getNum(e)];
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
