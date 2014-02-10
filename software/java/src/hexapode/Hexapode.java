package hexapode;

import serial.Serial;

public class Hexapode {
	
	private Patte[] pattes;
	
	public Hexapode(Serial serie)
	{
		pattes = new Patte[6];
		for(int i = 0; i < 6; i++)
			pattes[i] = new Patte(serie);
	}
	

}
