package test;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import hexapode.Hexapode;
import hexapode.markov.Markov;

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
	private boolean validation;
	
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
		try {
			java.io.File fichier_creation;
			FileOutputStream fichier;
			ObjectOutputStream oos;
			if(sauvegarde_intermediaire)
			{
				long date = System.currentTimeMillis();
				fichier_creation = new java.io.File("logs/markov-"+date+".dat");
				fichier_creation.createNewFile();
				fichier = new FileOutputStream("logs/markov-"+date+".dat");
				oos = new ObjectOutputStream(fichier);
				oos.writeObject(markov);
				oos.flush();
				oos.close();
			}

			fichier_creation = new java.io.File("markov.dat");
			fichier_creation.createNewFile();
			fichier = new FileOutputStream("markov.dat");
			oos = new ObjectOutputStream(fichier);
			oos.writeObject(markov);
			oos.flush();
			oos.close();
		}
		catch(Exception e)
		{
			System.out.println("Veuillez créer un dossier logs dans le dossier hexapode/software/java");
			e.printStackTrace();
		}
	}
	
	protected Markov chargement_matrice()
	{
		return chargement_matrice("markov.dat");
	}
	
	protected Markov chargement_matrice(String filename)
	{
		try {
			FileInputStream fichier = new FileInputStream(filename);
			ObjectInputStream ois = new ObjectInputStream(fichier);
			Markov markov = (Markov) ois.readObject();
			ois.close();
			return markov;
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return null;
	}
	
}
