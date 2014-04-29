package test;

import util.DataSaver;
import hexapode.Hexapode;

/**
 * Classe abstraite � h�riter pour coder les tests.
 * @author Stud
 * @author pf
 *
 */

public abstract class Test {
	
	protected int nbIteration;				//Nombre de tests � executer
	protected double consecutiveLearnTime;	//Temps de test entre chaque pause en seconde
	protected double pauseTime;				//Temps d'arr�t par pause en seconde
	protected Hexapode hexapode;
	protected Markov markov;
	protected boolean restartMarkov;
	protected boolean validation;
	protected String etat_actuel;
	protected String etat_suivant;
	protected int note;
	
	//variables utilis�es pour la sauvegarde
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

	public abstract void onStart();		//Au d�part de chaque test
	public abstract void onBreak();		//Pendant la pause
	public abstract void proceedTest();	//Lancement de chaque test
	public abstract void validTest();	//Routine de validation des tests (sert � refaire les tests sans apprentissage, pour valider les r�sultats)
	public abstract void init();		//Au lancement de tous les tests
	public abstract void updateNote();  //Met à jour la note après proceedTest
	
	public void onExit()				//A la fin de chaque test
	{
        updateNote();
        if(!validation)
            markov.updateMatrix(note, etat_actuel, etat_suivant);
//		if(!validation)
//			sauvegarde_matrice(false);
		//DataSaver.sauvegarder_test(etat_suivant, result);
	}

	public void terminate()				//Fin de tous les tests
	{
		if(!validation)
		{
			sauvegarde_matrice(true);		
			System.out.println(markov.toString());
		}
	}
	
	public int getNbIteration()			//Nombre d'it�rations � effectuer
	{
		return nbIteration;
	}

	public boolean isValidation()		//En routine de validation ou non
	{
		return validation;
	}
	
	public double getConsecutiveLearnTime()	//Temps depuis la derni�re pause
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
