package hexapode.markov;

/**
 * Contient l'état de l'hexapode. Manipulé par la chaîne de Markov et l'hexapode.
 * @author pf
 *
 */

public class EtatHexa {

	public EtatPatte[] epattes;

	public EtatHexa(EtatPatte[] epattes)
	{
		this.epattes = epattes;
	}
	
}
