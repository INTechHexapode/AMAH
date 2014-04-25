package test;

import util.DataSaver;
import hexapode.Hexapode;
import hexapode.markov.EtatHexa;
import hexapode.markov.Markov;

/**
 * Classe abstraite ï¿½ hï¿½riter pour coder les tests.
 * @author Stud
 * @author pf
 *
 */

public abstract class Test {
	
	protected int nbIteration;				//Nombre de tests ï¿½ executer
	protected double consecutiveLearnTime;	//Temps de test entre chaque pause en seconde
	protected double pauseTime;				//Temps d'arrï¿½t par pause en seconde
	protected Hexapode hexapode;
	protected Markov markov;
	protected boolean restartMarkov;
	private boolean validation;
	protected EtatHexa etat_actuel;
	protected EtatHexa etat_suivant;
	protected int note;
	
	//variables utilisées pour la sauvegarde
	protected int result;
	
	public Test(Hexapode hexapode, int nbIteration, double consecutiveLearnTime, double pauseTime, boolean restartMarkov, boolean validation)
	{
		this.hexapode = hexapode;
		this.nbIteration = nbIteration;
		this.consecutiveLearnTime = consecutiveLearnTime;
		this.pauseTime = pauseTime;
		this.restartMarkov = restartMarkov;
		this.validation = validation;
	}

	public abstract void onStart();		//Au départ de chaque test
	public abstract void onBreak();		//Pendant la pause
	public abstract void proceedTest();	//Lancement de chaque test
	public abstract void validTest();	//Routine de validation des tests (sert à refaire les tests sans apprentissage, pour valider les résultats)
	public abstract void init();		//Au lancement de tous les tests
	
	public void onExit()				//A la fin de chaque test
	{
		if(!validation)
			sauvegarde_matrice(false);
		DataSaver.sauvegarder_test(etat_suivant, result);
	}

	public void terminate()				//Fin de tous les tests
	{
		if(!validation)
			sauvegarde_matrice(true);
	}
	
	public int getNbIteration()			//Nombre d'itérations à effectuer
	{
		return nbIteration;
	}

	public boolean isValidation()		//En routine de validation ou non
	{
		return validation;
	}
	
	public double getConsecutiveLearnTime()	//Temps depuis la dernière pause
	{
		return consecutiveLearnTime;
	}

	public double getPauseTime()
	{
		return pauseTime;
	}
	
	protected void sauvegarde_matrice(boolean sauvegarde_intermediaire)
	{
		DataSaver.sauvegarder_matrice(markov, sauvegarde_intermediaire);
	}
	
	protected Markov chargement_matrice()
	{
		return chargement_matrice("markov.dat");
	}
	
	protected Markov chargement_matrice(String filename)
	{
		return DataSaver.charger_matrice(filename);
	}
}
