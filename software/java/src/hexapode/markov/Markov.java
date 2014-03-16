package hexapode.markov;

/**
 * Contient la matrice de transition. Sérialisable afin d'avoir des données persistantes.
 * @author pf
 *
 */

public class Markov implements java.io.Serializable {

	private static final long serialVersionUID = 1L;
	private float matrice[][];
	
	/**
	 * Constructeur pour une nouvelle matrice
	 * @param dimension
	 */
	public Markov(int dimension)
	{
		matrice = new float[dimension][dimension];
		for(int i = 0; i < dimension; i++)
			for(int j = 0; j < dimension; j++)
				matrice[i][j] = 0;
	}
	
	public EtatHexa next(EtatHexa e)
	{
		// TODO
		return e;
	}
	
	public void updateMatrix(int resultat)
	{
		
	}
	
}
