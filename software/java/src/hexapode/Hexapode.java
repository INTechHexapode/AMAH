package hexapode;

import markov.Etat;
import serial.Serial;

public class Hexapode {
	
	private Patte[] pattes;
	
	public Hexapode(Serial serie)
	{
		pattes = new Patte[6];
		for(int i = 0; i < 6; i++)
			pattes[i] = new Patte(serie);
	}
	
	public void goto_etat(Etat e)
	{
		for(int i = 0; i < 6; i++)
			pattes[i].goto_etat(e);
	}
	

}
