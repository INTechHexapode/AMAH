package util;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import hexapode.markov.EtatHexa;
import hexapode.markov.Markov;

/**
 * Classe statique qui gere les sauvegardes et chargements.
 * @author Stud
 *
 */

public class DataSaver {

    private DataSaver()
    {
    	
    }
    
    public static <T> void sauvegarder_etat(EtatHexa etat, T resultat)
    {
    	
    }

    public static void sauvegarder_matrice(Markov markov, boolean sauvegarde_intermediaire)
    {
			if(sauvegarde_intermediaire)
			{
		    	try {
					long date = System.currentTimeMillis();
					sauvegarder(markov, "logs/markov-"+date+".dat");
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
    
    public static Markov charger_matrice(String filename)
	{
		try {
			Markov markov = (Markov) charger(filename);
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
