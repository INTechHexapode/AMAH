package hexapode.enums;

public enum Marche
{
    BASIQUE(0),
    TORCHE(1),
    RECALAGE(2),
    TIRER(3);
    
    public final int indice;
    
    private Marche(int indice)
    {
        this.indice = indice;
    }

}
