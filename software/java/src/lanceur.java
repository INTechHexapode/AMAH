import hexapode.Deplacement;
import hexapode.Hexapode;
import hexapode.Vec2;
import hexapode.capteurs.Capteur;
import hexapode.capteurs.Sleep;
import hexapode.enums.Evite;
import hexapode.enums.Marche;
import hexapode.enums.Profil;
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
            Sleep.temps_defaut = 250;
//            for(int i = 0; i < 6; i++)
//            deplacement.lever(i);
            //hexa.avancer(3000);
//            hexa.avancer(1000);
            //hexa.desasserv();
            //hexa.initialiser();
            //hexa.desasserv();
//            deplacement.arret();
//            hexa.avancer(600);
//            hexa.avancer_et_evite(1000, Evite.PAR_LA_DROITE);
//            deplacement.tendre(0);
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
        boolean symetrie = hexa.initialiser();
        
        try {
            if(!symetrie)
            {
                hexa.setAngle(-Math.PI*5/6);
                hexa.avancer(700);
                hexa.va_au_point_relatif((new Vec2(-250, 500)));
                Thread.sleep(2000);
                Thread.sleep(2000);
                
                hexa.avancer(500);
            }
            else
            {
                hexa.setAngle(-Math.PI*5/6);
                hexa.avancer(650);

                hexa.va_au_point_relatif(new Vec2(-550, 400));
                
                hexa.va_au_point_relatif(new Vec2(100, -900));
            }
		} catch (EnnemiException e) {
			e.printStackTrace();
		} catch (BordureException e) {
			e.printStackTrace();
		} catch (GoToException e) {
            e.printStackTrace();
        } catch (InterruptedException e)
        {
            // TODO Auto-generated catch block
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
