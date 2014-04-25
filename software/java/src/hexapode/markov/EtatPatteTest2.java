package hexapode.markov;

/**
 * Ne pas modifier l'ordre de l'enum; ça a une signification sémantique.
 * @author pf
 *
 */

public enum EtatPatteTest2 {
    ARRIERE,
    AVANT,
	POUSSE,
	DEBOUT;

    public static EtatPatteTest2 getEtat(int nb)
    {
        for(EtatPatteTest2 e: EtatPatteTest2.values())
            if(e.ordinal() == nb)
                return e;
        return null;
    }

}
