package hexapode;

import serial.Serial;
import serial.SerialException;

	/**
	 * Classe des moteurs. Visibilité en "friendly".
	 * Retient toujours son état
	 * @author pf
	 *
	 */

class TriMoteur {

	private Serial serie;
	private int id;

    private static final int[] angle_min = {900, 500, 1000};  // sécurité des moteurs
    private static final int[] angle_max = {1500, 2000, 2000}; // sécurité des moteurs
    
	/**
	 * Construit un moteur, désasservi.
	 * @param serie
	 * @param id
	 * @param etat
	 * @param angle_min
	 * @param angle_max
	 */
	public TriMoteur(Serial serie, int id)
	{
		this.serie = serie;
		this.id = id;
		desasserv();
	}

    /**
     * Deplacement groupé des trois moteurs
     * Lève une exception si un angle ne satisfait pas les bornes.
     * @param angles
     * @param temps de déplaceme,t en ms
     * @throws GoToException
     */
    public void goto_etat(int[] angles, int temps) throws GoToException
	{	
	    for(int i = 0; i < 2; i++)
	        if(angles[i] < angle_min[i] || angles[i] > angle_max[i])
	            throw new GoToException(i);
        if(serie != null)
            try {
                String ordre = new String();
                // S500 pour régler la vitesse maximale
                for(int i = 0; i < 3; i++)
                    ordre += "#"+Integer.toString(id+i)+"P"+Integer.toString(angles[i])+" ";
                ordre += "T"+Integer.toString(temps);
                serie.communiquer(ordre);
            } catch (SerialException e1) {
                e1.printStackTrace();
            }
	}

	/**
	 * Désasservit le moteur
	 */
	public void desasserv()
	{
        if(serie != null)
    		try {
                for(int i = 0; i < 3; i++)
                    serie.communiquer("#"+Integer.toString(id+i)+"L");
    		} catch (SerialException e1) {
    			e1.printStackTrace();
    		}		
	}
	
}
