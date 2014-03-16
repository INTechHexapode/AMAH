import serial.Serial;
import serial.SerialManager;
import serial.SerialManagerException;
import hexapode.Hexapode;
import hexapode.markov.EtatHexa;
import hexapode.markov.Markov;
import java.io.*;

public class lanceur {

	// TODO
	public static void main(String[] args) {
		SerialManager serialmanager;
		try {
			serialmanager = new SerialManager();
		Serial serie = serialmanager.getSerial("serieAsservissement");
		} catch (SerialManagerException e) {
			e.printStackTrace();
		}
		
		/*
		try {
			// Etat initial
			EtatHexa e = new EtatHexa(null);

			// Récupération de la matrice de l'hexapode
			Markov markov = chargement_matrice();
			
			// Initialisation de la série, de l'hexapode et de la chaîne de Markov
			SerialManager serialmanager = new SerialManager();
			Serial serie = serialmanager.getSerial("serieAsservissement");
			Hexapode hexa = new Hexapode(serie, e, markov);
			
			for(int i = 0; i < 100; i++)
			{
				hexa.next();
			}
			sauvegarde_matrice(markov);
		} catch (SerialManagerException e) {
			e.printStackTrace();
		}*/
		
	}

}
