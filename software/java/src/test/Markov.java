package test;

import hexapode.enums.Mode;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import util.DataSaver;

/**
 * Contient la matrice de transition. Sérialisable afin d'avoir des données persistantes.
 * Changera à chaque changement de tests
 * @author pf
 *
 */

public class Markov implements java.io.Serializable {

	private static final long serialVersionUID = 1L;
	private transient short matrice[][]; // utilisé pour le remplissage seulement, à ne pas sauvegarder
	private transient List<char[]> positionsViables; // transient = pas sauvegardé
	private ArrayList<ArrayList<IntPair>> compressed_matrix;
	private transient Random randomgenerator = new Random();
	private int nbEtatsParPattes;
	private int dimension;
	private int diviseur = 0; // en puissance de 2
	
	/**
	 * Chargement de l'apprentissage réalisé pour ce mode.
	 * @param mode (biphasé ou triphasé)
	 */
	public static Markov getLearnedMarkov(Mode mode)
	{
	    return DataSaver.charger_matrice("markov_"+mode+".dat");
	}
	
	/**
	 * Prépare l'objet pour la sauvegarde, en remplissant compressed_matrix
	 */
	public void prepareForSave()
	{
	    compressed_matrix = new ArrayList<ArrayList<IntPair>>();
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
	    this.nbEtatsParPattes = nbEtatsParPattes;
		// dimension = nbEtatsParPattes^6
		int dimension = nbEtatsParPattes*nbEtatsParPattes*nbEtatsParPattes;
		dimension *= dimension;
		this.dimension = dimension;
		matrice = new short[dimension][dimension];
		for(int i = 0; i < dimension; i++)
			for(int j = 0; j < dimension; j++)
				matrice[i][j] = 0;
		getPositionsViables();
	}
	
	/**
	 * Retourne vrai si la position est viable, faux sinon.
	 * @param position
	 * @return
	 */
	public boolean estViable(String position)
	{
	    for(char[] pos: positionsViables)
	        if(Integer.parseInt(String.valueOf(pos)) == Integer.parseInt(position))
	            return true;
	    return false;
	}
	
	/**
	 * Charge les positions stables.
	 */
	private void getPositionsViables()
	{
		double pos[] = DataSaver.charger_matrice_equilibre("markov_equilibre.dat");
		positionsViables = new LinkedList<char[]>();
		for(int i=0; i< pos.length; i++)
			if(pos[i] > 4)
				positionsViables.add(Integer.toBinaryString(i).toCharArray());
	}
	
	/**
	 * Renvoie une position stable au hasard.
	 * @return
	 */
	private String getRandomPositionViable()
	{
		/* Ce bloc permet de piocher une transition parmi les �tats d'�quilibres */
		int r = randomgenerator.nextInt(positionsViables.size());//Pioche un int
		String out = String.valueOf(positionsViables.get(r));

		while(out.length() < 6)
			out = "0" + out;

		return out;
	}
	
	/**
	 * Donne le prochain état de manière équiprobable. Utilisé pour les tests.
	 * @return
	 */
	public String next()
	{
		return getRandomPositionViable();
	}

	/**
	 * Surcouche user-friendly de nextValidation
	 * @param e
	 * @return
	 */
	public String nextValidation(String e)
	{
	    return nextValidation(string2index(e));
	}

	/**
	 * Renvoie la prochain état en validation. Utilise les résultats obtenus
	 * précédemment et stockés dans compressed_matrix
	 * @param numeroEtatActuel
	 * @return
	 */
	public String nextValidation(int numeroEtatActuel)
	{
	    if(compressed_matrix == null)
	        return null;
	    
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
	 * Met à jour la matrice avec le résultat d'un test.
	 * @param resultat
	 * @param etatPrecedent
	 * @param etatSuivant
	 */
	public void updateMatrix(int resultat, String etatPrecedent, String etatSuivant)
	{
	    int note_actuelle = matrice[string2index(etatPrecedent)][string2index(etatSuivant)];
	    resultat >>= diviseur;
	    // S'il y a un overflow, on divise par 2 toutes les notes de la lignes
		// Les prochaines notes aussi seront divisées par deux.
	    if((note_actuelle+resultat) != (int)(short)(note_actuelle+resultat))
	    {
	        System.out.println("Overflow des notes!");
	        for(int i = 0; i < dimension; i++)
	            for(int j = 0; j < dimension; j++)
	                matrice[i][j] >>= 1;
	        diviseur++;
	        resultat >>= 1;
	    }
	        
		matrice[string2index(etatPrecedent)][string2index(etatSuivant)]+=resultat;
	}
	
	/**
	 * Convertit, à partir du nombre d'états, un String en nombre.
	 * @param e
	 * @return
	 */
	public int string2index(String e)
	{
		int num = 0;
		for(int i = 0; i < 6; i++)
		{
			num *= nbEtatsParPattes;
			try {
			    num += Integer.parseInt(String.valueOf(e.charAt(5-i)));
			}
			catch(Exception exception)
			{
			}
		}
		return num;
	}

	/**
     * Convertit, à partir du nombre d'états, un nombre en String.
	 * @param num
	 * @return
	 */
	   public String index2string(int num)
	    {
	        String out = new String();
	        for(int i = 0; i < 6; i++)
	        {
	            out += String.valueOf(num%nbEtatsParPattes);
	            num /= nbEtatsParPattes;
	        }
	        return out;
	    }


	   /**
	    * N'affiche que les cases non nulles!
	    */
	@Override
	public String toString()
	{
		String s = "";
		for(int i = 0; i < dimension; i++)
			for(IntPair e: compressed_matrix.get(i))
			    s += index2string(i)+" "+index2string(e.etat_suivant)+": "+e.score+"\n";
		return s;
	}
	
}
