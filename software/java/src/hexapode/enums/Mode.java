package hexapode.enums;

public enum Mode
{
    BIPHASE(new EtatPatte[] {EtatPatte.ARRIERE, EtatPatte.AVANT}),
    TRIPHASE(new EtatPatte[] {EtatPatte.ARRIERE, EtatPatte.HAUT, EtatPatte.AVANT}),
    QUADRIPHASE(new EtatPatte[] {EtatPatte.ARRIERE, EtatPatte.HAUT_ARRIERE, EtatPatte.HAUT_AVANT, EtatPatte.AVANT});
    
    private EtatPatte[] e;
    
    private Mode(EtatPatte[] e)
    {
        this.e = e;
    }

    /**
     * Convertit un nombre de la chaîne en un EtatPatte.
     * @param n
     * @return
     */
    public EtatPatte getEtatPatte(char c)
    {
        if(c == '?')
            return EtatPatte.OTHER;
        return e[c-'0'];
    }
    
}
