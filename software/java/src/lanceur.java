import hexapode.Deplacement;
import hexapode.Hexapode;
import hexapode.Vec2;
import hexapode.capteurs.Capteur;
import hexapode.capteurs.Sleep;
import hexapode.enums.Marche;
import hexapode.enums.Mode;

import java.util.*;

import container.Container;
import serial.Serial;
import serial.SerialManager;
import test.*;

/*
 * TODO list (méca/élec)
 * Cartes électroniques (alim/capteurs)
 * Capteur 2 ultrason ou infrarouge + servo (on tourne le capteur dans la direction où on va)
 * Jumper
 * Bouton d'arrêt d'urgence
 * Raspberry
 * Lipo
 * Un ou deux boutons de manière à pouvoir le configurer sans pc?...
 * (un commutateur pour la couleur, un bouton poussoir pour le recalage)
 */

public class lanceur {

	public static void main(String[] args) {
		SerialManager serialmanager;
		Serial serie = null;
		Scanner scanner = null;
        Sleep.temps_defaut = 250;
		try {
		    serialmanager = new SerialManager();
			serie = serialmanager.getSerial("serieAsservissement");
        } catch (Exception e) {
            Sleep.temps_defaut = 0;
            System.out.println("Pas de série: "+e);
        }
		
		try {
		    Container container = new Container(serie, false);
    	    Hexapode hexa = (Hexapode)container.getService("Hexapode");
    	    Deplacement deplacement = (Deplacement)container.getService("Deplacement");
            if(serie != null)
            {
        		System.out.println("Attente");
        		scanner = new Scanner(System.in);
    		    scanner.nextLine();
            }
  
            Capteur capteur = new Capteur(serie);
  
//            TestCoordinationPattesSimulation test = new TestCoordinationPattesSimulation(deplacement, 1000000, 5000, 0, true, serie != null);
//            TestEngine testEngine = new TestEngine(test);
//            testEngine.start();

          TestDeuxCoups test = new TestDeuxCoups(deplacement, 500000, 5000, 0, true, true);
          TestEngine testEngine = new TestEngine(test);
          testEngine.start();
          
//            deplacement.setMode(Mode.BIPHASE, Marche.BASIQUE);
//            hexa.avancer(100);
    //        hexa.va_au_point(new Vec2(-100, 600), true);
		}
		catch(Exception e)
		{
		    e.printStackTrace();
		}
		if(serie != null)
		{
	        scanner.close();
		    serie.close();
		}
	}

	public static void test_validation(Deplacement deplacement)
	{
        TestCoordinationPattesSimulation test = new TestCoordinationPattesSimulation(deplacement, 10000, 50, 1, true, true);
        TestEngine testEngine = new TestEngine(test);
        testEngine.start();
	}

	
	public static void script(Hexapode hexa)
	{
        Vec2[] itineraire1 = {   new Vec2(1100, 1400),
                new Vec2(1100, 600),
                new Vec2(700, 400),
                new Vec2(0, 600),
                new Vec2(0, 1500)};

        hexa.initialiser();
	    // TODO: pouvoir se recaler en cours de match
	    hexa.poser_fresques();
	}
	
}
