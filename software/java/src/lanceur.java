import serial.Serial;
import serial.SerialManager;
import serial.SerialManagerException;
import hexapode.Hexapode;
import hexapode.markov.EtatHexa;

public class lanceur {

	// TODO
	public static void main(String[] args) {
		SerialManager serialmanager;
		try {
			serialmanager = new SerialManager();
			Serial serie = serialmanager.getSerial("serieAsservissement");
			EtatHexa e = new EtatHexa(null);
			Hexapode hexa = new Hexapode(serie, e);
			hexa.desasserv();
			hexa.leverPatte(0);
			hexa.leverPatte(2);
			hexa.leverPatte(4);
			hexa.baisserPatte(1);
			hexa.baisserPatte(3);
			hexa.baisserPatte(5);
			serie.close();
		} catch (SerialManagerException e) {
			e.printStackTrace();
		}
				
	}

}
