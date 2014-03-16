import serial.Serial;
import serial.SerialManager;
import serial.SerialManagerException;
import test.StandStillTest;
import test.TestEngine;
import hexapode.Hexapode;
import hexapode.markov.EtatHexa;

public class lanceur {

	public static void main(String[] args) {
		SerialManager serialmanager;
		try {
			serialmanager = new SerialManager();
			Serial serie = serialmanager.getSerial("serieAsservissement");
			EtatHexa e = new EtatHexa(null);
			Hexapode hexa = new Hexapode(serie, e);
			StandStillTest standstilltest = new StandStillTest(hexa, 10, 60., 15., true);
			TestEngine testengine = new TestEngine(standstilltest);
			testengine.start();
			serie.close();
		} catch (SerialManagerException e) {
			e.printStackTrace();
		}
				
	}

}
