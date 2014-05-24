package container;

import hexapode.Deplacement;
import hexapode.Hexapode;
import hexapode.capteurs.Capteur;
import hexapode.capteurs.Sleep;

import java.util.Hashtable;
import java.util.Map;

import scripts.Decision;
import serial.Serial;

public class Container
{
    private Serial serie;
    private boolean maj_position;
    private boolean bordure_activee;
    
    public Container(Serial serie, boolean maj_position, boolean bordure_activee)
    {
        this.serie = serie;
        this.maj_position = maj_position;
        this.bordure_activee = bordure_activee;
    }
    
    private Map<String,Service> services = new Hashtable<String,Service>();

    public Service getService(String nom)
    {
        if(services.containsKey(nom))
            ;
        else if(nom == "Capteur")
            services.put(nom, (Service)new Capteur(serie));
        else if(nom == "Sleep")
            services.put(nom, (Service)new Sleep((Capteur)getService("Capteur")));
        else if(nom == "Deplacement")
            services.put(nom, (Service)new Deplacement((Capteur)getService("Capteur"), serie, (Sleep)getService("Sleep"), maj_position, bordure_activee));
        else if(nom == "Hexapode")
            services.put(nom, (Service)new Hexapode((Deplacement)getService("Deplacement")));
        else if(nom == "Decision")
            services.put(nom, (Service)new Decision((Hexapode)getService("Hexapode")));
        return services.get(nom);
    }
}
