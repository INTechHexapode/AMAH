package scripts;

import java.util.ArrayList;

import hexapode.Hexapode;
import hexapode.Vec2;

public class ScriptFresques extends Script
{
    boolean done;
    
    public ScriptFresques(Hexapode hexa)
    {
        super(hexa);
    }

    @Override
    public Vec2 point_entree(int id)
    {
        return new Vec2(0, 1600);
    }

    @Override
    public void execute(int id)
    {
        done = true;
        hexa.poser_fresques(); 
    }

    @Override
    public ArrayList<Integer> versions()
    {
        ArrayList<Integer> out = new ArrayList<Integer>();
        if(!done)
            out.add(0);
        return out;
    }

}
