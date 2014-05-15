package scripts;

import hexapode.Hexapode;
import hexapode.Vec2;

import java.util.ArrayList;

/**
 * Script où on shoot dans les feux. On ne vérifie pas la couleur.
 * @author pf
 *
 */
public class ScriptShootFeu extends Script
{
    boolean[] done = new boolean[6];

    public ScriptShootFeu(Hexapode hexa)
    {
        super(hexa);
    }

    @Override
    public Vec2 point_entree(int id)
    {
        switch(id)
        {
            case 0: return new Vec2(600, 1400);
            case 1: return new Vec2(1100, 900);
            case 2: return new Vec2(600, 400);
            case 3: return new Vec2(-600, 1400);
            case 4: return new Vec2(-1100, 900);
            default: return new Vec2(-600, 400);
        }
    }

    @Override
    public void execute(int id)
    {
        // Voilà, on a shooté dedans.
        done[id] = true;
    }

    @Override
    public ArrayList<Integer> versions()
    {
        ArrayList<Integer> out = new ArrayList<Integer>();
        for(int i = 0; i < 6; i++)
            if(!done[i])
                out.add(i);
        return out;
    }

}
