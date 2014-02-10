import serial.Serial;
import serial.SerialManager;
import serial.SerialManagerException;
import hexapode.Hexapode;


public class lanceur {

	public static void main(String[] args) {
		try {
			SerialManager serialmanager = new SerialManager();
			Serial serie = serialmanager.getSerial("serieAsservissement");
			Hexapode hexa = new Hexapode(serie);
		} catch (SerialManagerException e) {
			e.printStackTrace();
		}
		
	}

}
