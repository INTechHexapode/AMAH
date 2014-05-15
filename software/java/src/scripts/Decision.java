package scripts;

import java.util.ArrayList;

import hexapode.Hexapode;
import hexapode.exceptions.BordureException;
import hexapode.exceptions.EnnemiException;
import container.Service;

public class Decision implements Service
{
    private Script[] instancesScripts;
    private Hexapode hexa;
    
    public Decision(Hexapode hexa)
    {
        this.hexa = hexa;
        instancesScripts = new Script[3];
        instancesScripts[0] = new ScriptFresques(hexa);
        instancesScripts[1] = new ScriptShootFeu(hexa);
        instancesScripts[2] = new ScriptTorches(hexa);
    }

    public void lancement()
    {
        // recalage
        
        hexa.initialiser(); // initialise et attend le jumper
        
        // boucle infinie, parce qu'il adooooore réfléchir.
        while(true)
        {
            double distance_min = 100000000;
            int id_script = 0;
            int id_version = 0;

            // On trouve l'action la plus proche...
            for(int i = 0; i < 3; i++)
            {
                ArrayList<Integer> versions = instancesScripts[i].versions();
                for(Integer id: versions)
                    if(instancesScripts[i].point_entree(id).squared_distance(hexa.getPosition()) < distance_min)
                    {
                        distance_min = instancesScripts[i].point_entree(id).squared_distance(hexa.getPosition());
                        id_script = i;
                        id_version = id;
                    }
            }

            // Et on l'exécute
            try
            {
                hexa.va_au_point(instancesScripts[id_script].point_entree(id_version));
                instancesScripts[id_script].execute(id_version);
            } catch (EnnemiException | BordureException e)
            {
            }
        }
    }
    
}
