package test;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.GregorianCalendar;

import hexapode.Hexapode;
import hexapode.markov.Markov;

/**
 * Classe abstraite � h�riter pour coder les tests.
 * @author Stud
 *
 */

public abstract class Test {
	
	protected int nbIteration;				//Nombre de tests � executer
	protected double consecutiveLearnTime;	//Temps de test entre chaque pause en seconde
	protected double pauseTime;				//Temps d'arr�t par pause en seconde
	protected Hexapode hexapode;
	protected Markov markov;
	
	public Test(Hexapode hexapode, int nbIteration, double consecutiveLearnTime, double pauseTime)
	{
		this.hexapode = hexapode;
		this.nbIteration = nbIteration;
		this.consecutiveLearnTime = consecutiveLearnTime;
		this.pauseTime = pauseTime;
	}

	public abstract void onStart();
	public abstract void onExit();
	public abstract void onBreak();
	public abstract void proceedTest();
	public abstract void init();
	
	public void terminate()
	{
		sauvegarde_matrice();
	}
	
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
	
	public void sauvegarde_matrice()
	{
		try {
			FileOutputStream fichier = new FileOutputStream("logs/markov-"+System.currentTimeMillis()+"dat");
			ObjectOutputStream oos = new ObjectOutputStream(fichier);
			oos.writeObject(markov);
			oos.flush();
			oos.close();
			fichier = new FileOutputStream("markov.dat");
			oos = new ObjectOutputStream(fichier);
			oos.writeObject(markov);
			oos.flush();
			oos.close();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public Markov chargement_matrice()
	{
		return chargement_matrice("markov.dat");
	}
	
	public Markov chargement_matrice(String filename)
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
