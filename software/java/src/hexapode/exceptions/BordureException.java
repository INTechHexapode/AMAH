package hexapode.exceptions;

/**
 * Exception levée si l'hexapode s'approche trop près d'une bordure du terrain.
 * @author pf
 *
 */

public class BordureException extends Exception {

    private static final long serialVersionUID = 0L;

    public BordureException()
    {
        super();
    }
    
    public BordureException(String m)
    {
        super(m);
    }


}
