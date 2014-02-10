import markov.Etat;
import markov.Markov;
import serial.Serial;
import serial.SerialManager;
import serial.SerialManagerException;
import hexapode.Hexapode;


public class lanceur {

	public static void main(String[] args) {
		try {
			// Initialisation de la série, de l'hexapode et de la chaîne de Markov
			SerialManager serialmanager = new SerialManager();
			Serial serie = serialmanager.getSerial("serieAsservissement");
			Hexapode hexa = new Hexapode(serie);
			Markov markov = new Markov();
			
			// Etat initial
			Etat e = new Etat();
			
			while(true)
			{
				e = markov.next(e);
				hexa.goto_etat(e);
			}
		} catch (SerialManagerException e) {
			e.printStackTrace();
		}
		
	}

}
