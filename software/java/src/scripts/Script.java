package scripts;

import java.util.ArrayList;

import hexapode.Hexapode;
import hexapode.Vec2;

/**
 * Classe abstraite dont hérite tous les différents scripts.
 * @author pf
 *
 */
public abstract class Script
{
    protected Hexapode hexa;
    
    public Script(Hexapode hexa)
    {
        this.hexa = hexa;
    }
    
    // Utilisé par la prise de décision
    public abstract Vec2 point_entree(int id);
    public abstract void execute(int id);
    public abstract ArrayList<Integer> versions();

}
