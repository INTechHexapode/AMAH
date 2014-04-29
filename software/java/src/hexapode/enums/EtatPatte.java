package hexapode.enums;

/**
 * Ne pas modifier l'ordre de l'enum; ça a une signification sémantique.
 * @author pf
 *
 */

public enum EtatPatte {
    ARRIERE(true, true),
    AVANT(true, true),
	POUSSE(true, true),
	DEBOUT(true, false), // = levé en avant
	HAUT(false, true),
	POSE(false, false),
	OTHER(false, false); // Other: position manuelle, désasservi, ...

    private boolean biphase, triphase;

    private EtatPatte(boolean biphase, boolean triphase)
    {
        this.biphase = biphase;
        this.triphase = triphase;
    }    
    
    public boolean isPossible(EtatPatte e)
    {
        return e.biphase;
    }

    public boolean isTriphase(EtatPatte e)
    {
        return e.triphase;
    }

}
