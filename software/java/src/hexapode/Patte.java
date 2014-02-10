package hexapode;

import serial.Serial;

	/**
	 * Classe des pattes, qui contiennent chacune 3 moteurs
	 * @author pf
	 *
	 */

public class Patte {

	// TODO renommer les moteurs afin d'avoir des noms clairs
	private Moteur moteur0, moteur1, moteur2;
	
	public Patte(Serial serie)
	{
		moteur0 = new Moteur(serie);
		moteur1 = new Moteur(serie);
		moteur2 = new Moteur(serie);
	}
	
	public void goto_etat(Etat e)
	{
		moteur0.goto_etat(e);
		moteur1.goto_etat(e);
		moteur2.goto_etat(e);
	}
	
}
