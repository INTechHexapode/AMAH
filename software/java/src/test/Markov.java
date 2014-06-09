package test;

import hexapode.enums.Mode;

import java.util.ArrayList;
import java.util.Random;

import util.DataSaver;

/**
 * Contient la matrice de transition. Sérialisable afin d'avoir des données persistantes.
 * Changera à chaque changement de tests
 * @author pf
 *
 */

@Deprecated
public class Markov extends MarkovNCoups implements java.io.Serializable {

	private static final long serialVersionUID = 1L;
	private transient short matrice[][]; // utilisé pour le remplissage seulement, à ne pas sauvegarder
	private ArrayList<ArrayList<IntPair>> compressed_matrix;
	private int dimension;
	
	/**
	 * Chargement de l'apprentissage réalisé pour ce mode.
	 * @param mode (biphasé ou triphasé)
	 */
	public static Markov getLearnedMarkov(Mode mode)
	{
	    return charger_matrice("markov_"+mode+".dat");
	}
	
	/**
	 * Chargement d'une matrice.
	 */
	public static Markov charger_matrice(String filename)
	{
        Markov out = (Markov)DataSaver.charger_matrice(filename);
        // Initialisation des attributs transient
        out.randomgenerator = new Random();
        out.getPositionsViables();
        return out;
	}
	
	/**
	 * Prépare l'objet pour la sauvegarde, en remplissant compressed_matrix
	 */
	public void prepareForSave()
	{
	    compressed_matrix = new ArrayList<ArrayList<IntPair>>();
        System.out.println("This = "+this);
	    System.out.println("Matrice = "+matrice);
	    for(int i = 0; i < dimension; i++)
	    {
	        ArrayList<IntPair> ligne = new ArrayList<IntPair>();
	        int lineSum = 0;
	        for(int j = 0; j < dimension; j++)
	            lineSum += matrice[i][j];
            for(int j = 0; j < dimension; j++)
                if(matrice[i][j]*dimension > lineSum) // on ne conserve que ceux qui sont supérieurs à la moyenne
                    ligne.add(new IntPair(j, matrice[i][j]));
            if(ligne.isEmpty())
                ligne.add(new IntPair(randomgenerator.nextInt(dimension),1));
	        compressed_matrix.add(ligne);
	    }
	}

	/**
	 * Constructeur pour une nouvelle matrice
	 * @param dimension
	 */
	public Markov(int nbEtatsParPattes)
	{
	    super(1, nbEtatsParPattes);
	    dimension = dimension_x;
	}

	/**
	 * Renvoie la prochain état en validation. Utilise les résultats obtenus
	 * précédemment et stockés dans compressed_matrix
	 * @param numeroEtatActuel
	 * @return
	 */
	@Override
	public String nextValidation(int numeroEtatActuel)
	{
	    if(compressed_matrix == null)
	    {
	        System.out.println("Matrice compressée absente!");
            return null;
	    }
	    
		int lineSum = 0;
		
		for(IntPair e: compressed_matrix.get(numeroEtatActuel))
			lineSum += e.score;
		
		if(lineSum == 0)
		    return index2string(randomgenerator.nextInt(dimension));
		
		int r = randomgenerator.nextInt(lineSum);
		
		lineSum = 0;
        for(IntPair e: compressed_matrix.get(numeroEtatActuel))
		{
			lineSum += e.score;
			if(lineSum > r)
			    return index2string(e.etat_suivant);
		}
		// Cas impossible
		return null;
	}
	

   /**
    * N'affiche que les cases non nulles!
    */
/*	@Override
	public String toString()
	{
		String s = "";
		for(int i = 0; i < dimension; i++)
			for(IntPair e: compressed_matrix.get(i))
			    s += index2string(i)+" "+index2string(e.etat_suivant)+": "+e.score+"\n";
		return s;
	}*/
	
}
