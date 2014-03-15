package hexapode.markov;

import java.util.Random;

/**
 * Etat d'un moteur
 * @author pf
 *
 */
public class EtatMoteur {

	public int angle;

	/**
	 * Constructeur classique
	 * @param angle
	 */
	public EtatMoteur(int angle)
	{
		this.angle = angle;
	}	
	
	/**
	 * Constructeur aléatoire
	 */
	public EtatMoteur()
	{
		// TODO: traitement supplémentaire nécessaire pour les bornes (nextFloat: entre 0 et 1)
		angle = new Random().nextInt();
	}
	
}
