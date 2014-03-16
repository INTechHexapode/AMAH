package hexapode;

import hexapode.markov.EtatHexa;
import serial.Serial;

public class Hexapode {
	
	private Patte[] pattes;
	private EtatHexa etat_actuel;
	
	public Hexapode(Serial serie, EtatHexa etat_initial)
	{
		etat_actuel = etat_initial;
		pattes = new Patte[6];
		for(int i = 0; i < 6; i++)
			pattes[i] = new Patte(serie, i);
	}
	
	private void goto_etat(EtatHexa e)
	{
		for(int i = 0; i < 6; i++)
			pattes[i].goto_etat(e.epattes[i]);
	}
	
	public void desasserv()
	{
		for(int i = 0; i < 6; i++)
			pattes[i].desasserv();
	}
	
	public void stand_up()
	{
		
//		1/3/5
//		0/2/4
	}
	
	public void leverPatte(int i)
	{
		pattes[i].lever();
	}

	public void baisserPatte(int i)
	{
		pattes[i].baisser();
	}

}
