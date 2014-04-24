package hexapode.markov;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import util.DataSaver;

/**
 * Contient la matrice de transition. SÃ©rialisable afin d'avoir des donnÃ©es persistantes.
 * Changera Ã  chaque changement de tests
 * @author pf
 *
 */

public class Markov implements java.io.Serializable {

	private static final long serialVersionUID = 1L;
	private double matrice[][];
	private List<char[]> positionsViables;
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
		dimension = 64;
		matrice = new double[dimension][dimension];
		for(int i = 0; i < dimension; i++)
			for(int j = 0; j < dimension; j++)
				matrice[i][j] = 0;
		getPositionsViables();
	}
	
	private void getPositionsViables()
	{
		double pos[] = DataSaver.charger_matrice_equilibre("C:\\Users\\Stud\\Documents\\Workspace\\hexapode\\software\\java\\markov_equilibre.dat");
		positionsViables = new LinkedList<char[]>();
		for(int i=0; i< pos.length; i++)
		{
			if(pos[i] > 4)
			{
				positionsViables.add(Integer.toBinaryString(i).toCharArray());
				System.out.println(positionsViables.get(positionsViables.size()-1));
			}
		}
	}
	
	public EtatHexa next(EtatHexa etatActuel)
	{
		EtatPatte[] ep = new EtatPatte[6];
		EtatHexa nEtatHexa;		

		/* Ce bloc permet de piocher une transition parmis les états d'équilibres */
		int r = randomgenerator.nextInt(positionsViables.size());//Pioche un int entre 0 et 63
		char[] binaryString =  (char[]) positionsViables.get(r);//Chope l'état de l'hexapode associé
		for(int i = 0; i < 6; ++i)
		{
			if(binaryString[i] == '1' && i < binaryString.length)//On met la patte en avant si il y a un 1
			{
				ep[i] = new EtatPatte(EtatPatteTest2.AVANT);
			}
			else
			{
				ep[i] = new EtatPatte(EtatPatteTest2.ARRIERE);//On laise la patte en place si il y a un 0 ou si le string a moins de 6 char
			}
		}
		
		/* Ici on compare l'état actuel avec l'état objectif pour déplacer seulement les pattes qui en ont besoin */
		

		nEtatHexa = new EtatHexa(ep);
		return nEtatHexa;
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
	
	
	public void updateMatrix(int resultat, EtatHexa etatPrecedent, EtatHexa etatSuivant)
	{
		matrice[getNum(etatPrecedent)][getNum(etatSuivant)]+=resultat;
		affiche_matrice();
	}
	
	public void affiche_matrice()
	{
		String s = "";
		for(int i = 0; i < matrice.length; i++)
		{
			for(int j = 0; j < matrice.length; j++)
				s += Double.toString(matrice[i][j])+" ";
			s += "\n";
		}
		System.out.println(s);
	}
	
	public double[][] getMat()
	{
		return matrice;
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
