package test;

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
	private short matrice[][];
	private transient List<char[]> positionsViables; // transient = pas sauvegardé
	private Random randomgenerator = new Random();
	private int nbEtatsParPattes;
	private int dimension;
	private int diviseur = 0; // en puissance de 2
	
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
				matrice[i][j] = 1;
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
	
	private void getPositionsViables()
	{
		double pos[] = DataSaver.charger_matrice_equilibre("markov_equilibre.dat");
		positionsViables = new LinkedList<char[]>();
		for(int i=0; i< pos.length; i++)
			if(pos[i] > 4)
				positionsViables.add(Integer.toBinaryString(i).toCharArray());
	}
	
	public String getRandomPositionViable()
	{
		/* Ce bloc permet de piocher une transition parmi les �tats d'�quilibres */
		int r = randomgenerator.nextInt(positionsViables.size());//Pioche un int
		String out = String.valueOf(positionsViables.get(r));

		while(out.length() < 6)
			out = "0" + out;

		return out;
	}
	
	public String next()
	{
		return getRandomPositionViable();
	}
	
	public String nextValidation(int numeroEtatActuel)
	{
		int lineSum = 0;
		for(int j = 0; j < dimension; ++j)
		{
			lineSum += matrice[numeroEtatActuel][j];
		}
		
		int r = randomgenerator.nextInt(lineSum+1);
		
		lineSum = 0;
		for(int j = 0; j < dimension; ++j)
		{
			lineSum += matrice[numeroEtatActuel][j];
			if(lineSum >= r)
			{
			    return Index2String(j);
			}
		}
		return null;
	}
	
	public void updateMatrix(int resultat, String etatPrecedent, String etatSuivant)
	{
	    int note_actuelle = matrice[String2Index(etatPrecedent)][String2Index(etatSuivant)];
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
	        
		matrice[String2Index(etatPrecedent)][String2Index(etatSuivant)]+=resultat;
	}
	
	public short[][] getMat()
	{
		return matrice;
	}
	
	public int String2Index(String e)
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

	   public String Index2String(int num)
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
		{
			for(int j = 0; j < dimension; j++)
			    if(matrice[i][j] != 0)
			        s += Index2String(i)+" "+Index2String(j)+": "+Short.toString(matrice[i][j])+"\n";
		}
		return s;
	}
	
}
