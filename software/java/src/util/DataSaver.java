package util;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import test.Markov;
import test.MarkovNCoups;
import test.TestStep;

/**
 * Classe statique qui gere les sauvegardes et chargements.
 * @author Stud, pf
 *
 */

public class DataSaver {

    private DataSaver()
    {
    	
    }
    
    public static void supprimer(String filename)
    {
        // System.out.println("Suppression de " + filename);
        try
        {
            (new java.io.File(filename)).delete();
        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public static <T> void sauvegarder_test(String etat, int resultat)
    {
    	try {
    		TestStep stored = (TestStep)charger("steps.dat");
        	System.out.println(stored.toString());
        	stored.addStep(etat, resultat);
        	sauvegarder(stored, "steps.dat");
    	}
    	catch(Exception e)
		{
    		System.out.println("Cr�ation d'une nouvelle sauvegarde dans steps.dat");
			e.printStackTrace();
		}
    }

    public static void sauvegarder_matrice(MarkovNCoups markov, boolean sauvegarde_intermediaire)
    {
        String filename;
        long date = System.currentTimeMillis();
        if(markov instanceof Markov)
        {
            
            ((Markov)markov).prepareForSave();
            filename = "logs/markov-"+date+".dat";
        }
        else
            filename = "logs/markovNCoups-"+date+".dat";

        if(sauvegarde_intermediaire)
		{
	    	try {
				sauvegarder(markov, filename);
	    	}
			catch(Exception e)
			{
				e.printStackTrace();
			}
		}

    	try {
			sauvegarder(markov, "markov.dat");
    	}
		catch(Exception e)
		{
			e.printStackTrace();
		}
    }
    
    private static <T> void sauvegarder(T obj, String filename)
    {
    	try {
			java.io.File fichier_creation;
			FileOutputStream fichier;
			ObjectOutputStream oos;
			
			fichier_creation = new java.io.File(filename);
			fichier_creation.createNewFile();
			fichier = new FileOutputStream(filename);
			oos = new ObjectOutputStream(fichier);
			oos.writeObject(obj);
			oos.flush();
			oos.close();
		}
		catch(Exception e)
		{
			System.out.println("Veuillez créer un dossier logs dans le dossier hexapode/software/java");
			e.printStackTrace();
		}
    }
    
    public static MarkovNCoups charger_matrice(String filename)
	{
		try {
		    MarkovNCoups markov = (MarkovNCoups) charger(filename);
			return markov;
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return null;
	}

    public static MarkovNCoups charger_matrice_deux_coups(String filename)
    {
        try {
            MarkovNCoups markov = (MarkovNCoups) charger(filename);
            return markov;
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        return null;
    }

    public static double[] charger_matrice_equilibre(String filename)
	{
		try {
			double[] markov = (double[]) charger(filename);
			return markov;
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return null;
	}
    
    private static Object charger(String filename)
    {
		try {
			FileInputStream fichier = new FileInputStream(filename);
			ObjectInputStream ois = new ObjectInputStream(fichier);
			Object obj = ois.readObject();
			ois.close();
			return obj;
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return null;
    }
}
