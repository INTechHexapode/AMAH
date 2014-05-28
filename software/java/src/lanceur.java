import hexapode.Deplacement;
import hexapode.Hexapode;
import hexapode.Vec2;
import hexapode.capteurs.Capteur;
import hexapode.capteurs.Sleep;
import hexapode.enums.Evite;
import hexapode.exceptions.BordureException;
import hexapode.exceptions.EnnemiException;
import hexapode.exceptions.GoToException;
import container.Container;
import serial.Serial;
import serial.SerialManager;
import test.*;

/*
 * TODO list (méca/élec)
 * Lipo
 */

public class lanceur {

	public static void main(String[] args) {
		SerialManager serialmanager;
		Serial serie = null;
        Sleep.temps_defaut = 250;
		try {
		    serialmanager = new SerialManager();
			serie = serialmanager.getSerial("serieAsservissement");
        } catch (Exception e) {
            Sleep.temps_defaut = 0;
            System.out.println("Pas de série: "+e);
        }
		
		try {
		    Container container = new Container(serie, false, false);
    	    Deplacement deplacement = (Deplacement)container.getService("Deplacement");
            Hexapode hexa = (Hexapode)container.getService("Hexapode");
            if(serie == null)
                throw new Exception();
//            deplacement.arret();
//            hexa.avancer(600);
              lanceur_coupe(hexa, deplacement);
		}
		catch(Exception e)
		{
		    e.printStackTrace();
		}
		if(serie != null)
		{
		    serie.close();
		}
	}

	public static void test_validation(Deplacement deplacement)
	{
        TestCoordinationPattesSimulation test = new TestCoordinationPattesSimulation(deplacement, 10000, 50, 1, true, true);
        TestEngine testEngine = new TestEngine(test);
        testEngine.start();
	}
	
	public static void test_capteur(Capteur capteur)
	{
	    capteur.setOn();
        for(int i = 0; i < 1000; i++)
        {
            capteur.genere_mesure();
            capteur.genere_mediane();
            System.out.println(capteur.mesure());
            try
            {
                Thread.sleep(50);
            } catch (InterruptedException e)
            {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

	}
	
	public static void lanceur_coupe(Hexapode hexa, Deplacement deplacement)
	{
        hexa.initialiser();
        
        try {
			hexa.va_au_point_indirect(new Vec2(-1500, -100), Evite.PAR_LA_GAUCHE);
		} catch (EnnemiException e) {
			e.printStackTrace();
		} catch (BordureException e) {
			e.printStackTrace();
		} catch (GoToException e) {
            e.printStackTrace();
        }
        
	}
	
	public static void danse(Deplacement deplacement, Sleep sleep)
	{
        Sleep.temps_defaut = 500;
	    
	    int[] tab = {0,1,2,5,4,3};
	    for(int i = 0; i < 8; i++)
	    {
	        for(int j = 0; j < 6; j++)
	        {
                int k = tab[j];
	            deplacement.lever(k);
	            sleep.sleep();
                deplacement.baisser((k+5)%6);
	        }
	    }
	}
	
}
