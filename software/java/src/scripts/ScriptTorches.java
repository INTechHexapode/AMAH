package scripts;

import hexapode.Hexapode;
import hexapode.Vec2;

import java.util.ArrayList;

public class ScriptTorches extends Script
{

    boolean[] done = new boolean[2];
    
    public ScriptTorches(Hexapode hexa)
    {
        super(hexa);
    }

    @Override
    public Vec2 point_entree(int id)
    {
        if(id == 0)
            return new Vec2(600, 600);
        else
            return new Vec2(-600, 600);
    }

    @Override
    public void execute(int id)
    {
        done[id] = true;
    }

    @Override
    public ArrayList<Integer> versions()
    {
        ArrayList<Integer> out = new ArrayList<Integer>();
        for(int i = 0; i < 2; i++)
            if(!done[i])
                out.add(i);
        return out;
    }

}
