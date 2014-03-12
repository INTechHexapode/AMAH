package test;

import hexapode.Hexapode;

/**
 * Classe abstraite à hériter pour coder les tests.
 * @author Stud
 *
 */

public abstract class Test {
	
	protected int nbIteration;				//Nombre de tests à executer
	protected double consecutiveLearnTime;	//Temps de test entre chaque pause en seconde
	protected double pauseTime;				//Temps d'arrêt par pause en seconde
	private Hexapode hexapode;
	
	public Test(Hexapode hexapode, int nbIteration, double consecutiveLearnTime, double pauseTime)
	{
		this.hexapode = hexapode;
		this.nbIteration = nbIteration;
		this.consecutiveLearnTime = consecutiveLearnTime;
		this.pauseTime = pauseTime;
	}

	public abstract void onStart();
	public abstract void onExit();
	public abstract void proceedTest();
	
	public int getNbIteration()
	{
		return nbIteration;
	}
	
	public double getConsecutiveLearnTime()
	{
		return consecutiveLearnTime;
	}

	public double getPauseTime()
	{
		return pauseTime;
	}
	
}
