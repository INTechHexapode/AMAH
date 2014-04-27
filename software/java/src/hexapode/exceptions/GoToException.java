package hexapode.exceptions;

/**
 * Exception levée si un état n'est pas atteignable
 * @author pf
 *
 */

public class GoToException extends Exception {

    private static final long serialVersionUID = 0L;

    public GoToException()
    {
        super();
    }
    
    public GoToException(String m)
    {
        super(m);
    }

    public GoToException(int id)
    {
        super();
        System.out.println("Moteur "+id);
    }


}
