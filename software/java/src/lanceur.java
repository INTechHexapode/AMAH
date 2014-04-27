import hexapode.GoToException;
import hexapode.Hexapode;
import hexapode.Vec2;

import java.util.Scanner;

import serial.Serial;
import serial.SerialManager;
import test.TestCoordinationPattesSimulation;
import test.TestEngine;
import util.Sleep;

/*
 * TODO list (méca/élec)
 * Cartes électroniques (alim/capteurs)
 * Capteur ultrason ou infrarouge + servo (on tourne le capteur dans la direction où on va)
 * Jumper
 * Bouton d'arrêt d'urgence
 * Raspberry
 * Un ou deux boutons de manière à pouvoir le configurer sans pc?...
 * (un commutateur pour la couleur, un bouton poussoir pour le recalage)
 */

public class lanceur {

	public static void main(String[] args) {
		SerialManager serialmanager;
		try {
//		    serialmanager = new SerialManager();
//			Serial serie = serialmanager.getSerial("serieAsservissement");
			Hexapode hexa = new Hexapode(null, false);
 			System.out.println("Attente");
			Scanner scanner = new Scanner(System.in);
			scanner.nextLine();

            hexa.va_au_point(new Vec2(0, 100), true);
            hexa.va_au_point(new Vec2(-10, 110), true);

//			hexa.recaler();

            Hexapode hexaJaune = new Hexapode(null, false);
            Hexapode hexaRouge = new Hexapode(null, true);

            cercle(hexaJaune);
            cercle(hexaRouge);

            scanner.close();
//			serie.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
				
	}
	public static void cercle(Hexapode hexa) throws GoToException
	{
        Sleep.temps_defaut = 300;
        for(int i = 0; i < 18; i++)
        {
            hexa.goto_etat("101010");
            hexa.goto_etat("010101");
            if(i % 3 == 2)
                hexa.setDirectioneRelatif(1);
        }	    
	}
	
	public static void marche_scriptee(Hexapode hexa) throws GoToException
	{
        Sleep.temps_defaut = 200;
        for(int i = 0; i < 30; i++)
        {
            hexa.goto_etat("101010");
            hexa.goto_etat("010101");
        }
        hexa.arret();
	}

	// TODO: comparer la précision avec la première marche scriptée
	// par contre, celle-là est 3 fois plus lente
	   public static void autre_marche_scriptee(Hexapode hexa) throws GoToException
	    {
	        Sleep.temps_defaut = 200;
	        for(int i = 0; i < 30; i++)
	        {
	            if(i % 2 == 0)
	                hexa.goto_etat("101010");
	            else
                    hexa.goto_etat("010101");
	            hexa.goto_etat("111111");
                hexa.goto_etat("000000");
	        }
	        hexa.arret();
	    }

	public static void acceleration(Hexapode hexa) throws GoToException
	{
        Sleep.temps_defaut = 500;
	    for(int i = 0; i < 10; i++)
        {
            hexa.goto_etat("101010");
            Sleep.temps_defaut -= 20;
            hexa.goto_etat("010101");
            Sleep.temps_defaut -= 20;
        }
        for(int i = 0; i < 5; i++)
        {
            hexa.goto_etat("101010");
            hexa.goto_etat("010101");
        }	    
	}

	public static void test_validation(Hexapode hexa)
	{
        TestCoordinationPattesSimulation test = new TestCoordinationPattesSimulation(hexa, 10000, 50, 1, true, true);
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
	    hexa.suit_chemin(itineraire1);
	    hexa.poser_fresques();
	}
	
}
