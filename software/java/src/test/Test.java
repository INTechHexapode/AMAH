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
	protected EtatHexa etat_suivant;
	
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

	public abstract void onStart();
	public abstract void onBreak();
	public abstract void proceedTest();
	public abstract void validTest();
	public abstract void init();
	
	public void onExit()
	{
		if(!validation)
			sauvegarde_matrice(false);
		DataSaver.sauvegarder_test(etat_suivant, result);
	}

	public void terminate()
	{
		if(!validation)
			sauvegarde_matrice(true);
	}
	
	public int getNbIteration()
	{
		return nbIteration;
	}

	public boolean isValidation()
	{
		return validation;
	}
	
	public double getConsecutiveLearnTime()
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
