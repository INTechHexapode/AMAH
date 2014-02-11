package hexapode;

import hexapode.markov.EtatHexa;
import hexapode.markov.Markov;
import serial.Serial;

public class Hexapode {
	
	private Patte[] pattes;
	private Markov markov;
	private EtatHexa etat_actuel;
	
	public Hexapode(Serial serie, EtatHexa etat_initial)
	{
		etat_actuel = etat_initial;
		// TODO sérialisation
		markov = new Markov();
		pattes = new Patte[6];
		for(int i = 0; i < 6; i++)
			pattes[i] = new Patte(serie, i);
	}
	
	private void goto_etat(EtatHexa e)
	{
		for(int i = 0; i < 6; i++)
			pattes[i].goto_etat(e.epattes[i]);
	}
	
	public void next()
	{
		etat_actuel = markov.next(etat_actuel);
		goto_etat(etat_actuel);

		// on attend que l'hexapode finisse le mouvement (appels à la série non bloquants)
		// valeur à ajuster expérimetalement
		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
}
