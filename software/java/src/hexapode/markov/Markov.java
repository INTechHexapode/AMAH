package hexapode.markov;

import java.util.Random;

/**
 * Contient la matrice de transition. Sérialisable afin d'avoir des données persistantes.
 * @author pf
 *
 */

public class Markov implements java.io.Serializable {

	private static final long serialVersionUID = 1L;
	private float matrice[][];
	private Random randomgenerator = new Random();
	
	/**
	 * Constructeur pour une nouvelle matrice
	 * @param dimension
	 */
	public Markov(int nbEtatsParPattes)
	{
		// Dimension = nbEtatsParPattes^6
		int dimension = nbEtatsParPattes*nbEtatsParPattes*nbEtatsParPattes;
		dimension *= dimension;
		matrice = new float[dimension][dimension];
		for(int i = 0; i < dimension; i++)
			for(int j = 0; j < dimension; j++)
				matrice[i][j] = 1f/dimension;
	}
	
	public EtatHexa next(EtatHexa e)
	{
		EtatPatte[] ep = new EtatPatte[6];
		for(int i = 0; i < 6; i++)
			ep[i] = new EtatPatte(randomgenerator.nextBoolean());
		return new EtatHexa(ep);
	}
	
	public void updateMatrix(int resultat)
	{
		
	}
	
}
