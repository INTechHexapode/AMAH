package hexapode;

/**
 * Exception levée si un ennemi est détecté et qu'il ne semble pas vouloir partir.
 * @author pf
 *
 */

public class EnnemiException extends Exception {

    private static final long serialVersionUID = 0L;

    public EnnemiException()
    {
        super();
    }
    
    public EnnemiException(String m)
    {
        super(m);
    }


}
