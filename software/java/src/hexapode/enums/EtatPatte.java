package hexapode.enums;

/**
 * Enumération des états des pattes.
 * @author pf
 *
 */

public enum EtatPatte {
    ARRIERE(1, -1),
    AVANT(1, 1),
	POUSSE(2, -1),
	HAUT(0, 0),
	POSE(1, 0),
	HAUT_ARRIERE(0, -1),
	HAUT_AVANT(0, 1),
	OTHER(0, 0); // Other: position manuelle, désasservi, ... haut et avance n'ont plus de sens.

    public final int haut;
    public final int avancee;
    
    /**
     * Constructeur des EtatPatte
     * @param haut 0 si la patte est en haut, 1 si elle est au sol, 2 si elle s'enfonce.
     * @param avance -1 si en arrière, 0 si au milieu, 1 si devant
     */
    private EtatPatte(int haut, int avancee)
    {
        this.haut = haut;
        this.avancee = avancee;
    }
    
    public EtatPatte intermediaire(EtatPatte apres)
    {
        if(apres == EtatPatte.ARRIERE)
            return EtatPatte.POUSSE;
        else if(this == EtatPatte.ARRIERE && apres == EtatPatte.AVANT)
            return EtatPatte.HAUT_AVANT;
        return null;
    }
    
    /**
     * Renvoie la valeur par la laquelle il faut diviser le temps standard
     * de sleep pour effectuer cette transition.
     * @param apres
     * @return
     */
    public int division_sleep_transition(EtatPatte apres)
    {
        if(this == apres)
            return 0; // on n'attend pas si on ne bouge pas
        
        else if(this == EtatPatte.POUSSE && apres == EtatPatte.ARRIERE)
            return 4; // on n'attend qu'un quart de sleep
        
        // Par défaut, il faut un sleep entier
        return 1;
    }
    
}
