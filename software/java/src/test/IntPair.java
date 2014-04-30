package test;

public class IntPair implements java.io.Serializable
{
    private static final long serialVersionUID = 1L;
    public int etat_suivant, score;

    public IntPair(int etat_suivant, int score)
    {
        this.etat_suivant = etat_suivant;
        this.score = score;
    }
    
}
