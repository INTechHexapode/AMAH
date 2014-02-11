import serial.Serial;
import serial.SerialManager;
import serial.SerialManagerException;
import hexapode.Hexapode;
import hexapode.markov.EtatHexa;


public class lanceur {

	// TODO
	public static void main(String[] args) {
		try {
			// Etat initial
			EtatHexa e = new EtatHexa(null);

			// Initialisation de la série, de l'hexapode et de la chaîne de Markov
			SerialManager serialmanager = new SerialManager();
			Serial serie = serialmanager.getSerial("serieAsservissement");
			Hexapode hexa = new Hexapode(serie, e);
			
			while(true)
			{
				hexa.next();
			}
		} catch (SerialManagerException e) {
			e.printStackTrace();
		}
		
	}

}
