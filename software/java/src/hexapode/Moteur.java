package hexapode;

import hexapode.markov.EtatMoteur;
import serial.Serial;
import serial.SerialException;

	/**
	 * Classe des moteurs. Visibilité en "friendly".
	 * Retient toujours son état
	 * @author pf
	 *
	 */

class Moteur {

	private Serial serie;
	private int id;
	private boolean desasservi;

	@SuppressWarnings("unused")
	private EtatMoteur etat;		// la modification de cet état n'est pas utilisée ici, mais par hexapode (dans EtatHexa)

	public Moteur(Serial serie, int id, EtatMoteur etat)
	{
		this.etat = etat;
		this.serie = serie;
		this.id = id;
		desasserv();
	}
	
	/**
	 * Asservit le moteur à une nouvelle position (user-friendly) 
	 * @param angle, entre 1000 et 2000
	 */
	public void goto_etat(int angle)
	{
		goto_etat(new EtatMoteur(angle));
	}
	
	/**
	 * Asservit le moteur à une nouvelle position
	 * @param e, avec un angle entre 1000 et 2000
	 */
	public void goto_etat(EtatMoteur e)
	{	
		if(e.angle >= 1000 && e.angle <= 2000)
			try {
				serie.communiquer("#"+Integer.toString(id)+"P"+Integer.toString(e.angle));
				desasservi = false;
				etat = e;
			} catch (SerialException e1) {
				e1.printStackTrace();
			}
	}

	/**
	 * Désasservit le moteur
	 */
	public void desasserv()
	{
		try {
			serie.communiquer("#"+Integer.toString(id)+"L");
			desasservi = true;
		} catch (SerialException e1) {
			e1.printStackTrace();
		}		
	}
	
	/**
	 * Getter de desasservi
	 * @return true si le moteur est desasservi, false sinon
	 */
	public boolean isDesasservi()
	{
		return desasservi;
	}
	
}
