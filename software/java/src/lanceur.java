import hexapode.Direction;
import hexapode.EnumPatte;
import hexapode.Hexapode;
import hexapode.Vec2;
import hexapode.exceptions.EnnemiException;
import hexapode.exceptions.GoToException;

import java.util.Scanner;

import serial.Serial;
import serial.SerialManager;
import test.TestCoordinationPattesSimulation;
import test.TestEngine;
import util.Sleep;

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
		try {
		    serialmanager = new SerialManager();
			Serial serie = serialmanager.getSerial("serieAsservissement");
			Hexapode hexa = new Hexapode(serie, false);
 			System.out.println("Attente");
			Scanner scanner = new Scanner(System.in);
			scanner.nextLine();
	
	        Sleep.temps_defaut = 200;
	        hexa.avancer(200);
	        //	        hexa.setMarche(Hexapode.marche_basique);
//	        hexa.setProfil(0);
//            hexa.avancer(2000);
//			hexa.poser_fresques();
//            autre_marche_scriptee(hexa);
//			test_validation(hexa);
//			cercle(hexa);
//			hexa.va_au_point(new Vec2(-300, 1500), true);
//			marche_scriptee(hexa);
//			hexa.avancer_tomber_feu(1000, EnumPatte.DROITE);
//			hexa.recaler();
			hexa.arret();
			hexa.desasserv();
			scanner.close();
			serie.close();
		} catch (Exception e) {
			e.printStackTrace();
		}				
	}

	public static void cercle(Hexapode hexa) throws GoToException, EnnemiException
	{
        Sleep.temps_defaut = 300;
        for(int i = 0; i < 18; i++)
        {
            hexa.goto_etat("101010");
            hexa.goto_etat("010101");
            if(i % 3 == 2)
                hexa.setDirectionRelatif(1);
        }	    
	}

	// Tests de marche triphasée... mais ça ne marche pas.
	// Peut-être à réessayer avec des patins anti-dérapants.
	   public static void marche_triphase_1(Hexapode hexa) throws GoToException, EnnemiException
	   {
	        for(int i = 0; i < 10; i++)
	        {
	            hexa.goto_etat_triphase("ACBACB");
	            hexa.goto_etat_triphase("BACBAC");
                hexa.goto_etat_triphase("CBACBA");
	        }
	        hexa.arret();
	    }

       public static void marche_triphase_4(Hexapode hexa) throws GoToException, EnnemiException
       {
            for(int i = 0; i < 10; i++)
            {
                hexa.goto_etat_triphase("ABCABC");
                hexa.goto_etat_triphase("BCABCA");
                hexa.goto_etat_triphase("CABCAB");
            }
            hexa.arret();
        }

       public static void marche_triphase_2(Hexapode hexa) throws GoToException, EnnemiException
       {
            for(int i = 0; i < 10; i++)
            {
                hexa.goto_etat_triphase("ACBCBA");
                hexa.goto_etat_triphase("BACACB");
                hexa.goto_etat_triphase("CBABAC");
            }
            hexa.arret();
        }

       public static void marche_triphase_5(Hexapode hexa) throws GoToException, EnnemiException
       {
            for(int i = 0; i < 10; i++)
            {
                hexa.goto_etat_triphase("ABCCAB");
                hexa.goto_etat_triphase("BCAABC");
                hexa.goto_etat_triphase("CABBCA");
            }
            hexa.arret();
        }

       public static void marche_triphase_3(Hexapode hexa) throws GoToException, EnnemiException
       {
            for(int i = 0; i < 10; i++)
            {
                hexa.goto_etat_triphase("ACBBAC");
                hexa.goto_etat_triphase("BACCBA");
                hexa.goto_etat_triphase("CBAACB");
            }
            hexa.arret();
        }

       public static void marche_triphase_6(Hexapode hexa) throws GoToException, EnnemiException
       {
            for(int i = 0; i < 10; i++)
            {
                hexa.goto_etat_triphase("ABCBCA");
                hexa.goto_etat_triphase("BCACAB");
                hexa.goto_etat_triphase("CABABC");
            }
            hexa.arret();
        }

	public static void marche_scriptee(Hexapode hexa) throws GoToException, EnnemiException
	{
        Sleep.temps_defaut = 600;
        for(int i = 0; i < 15; i++)
        {
            hexa.goto_etat("101010");
            hexa.goto_etat("010101");
        }
        hexa.arret();
	}

	// TODO: comparer la précision avec la première marche scriptée
	// par contre, celle-là est 3 fois plus lente
	   public static void autre_marche_scriptee(Hexapode hexa) throws GoToException, EnnemiException
	    {
	        Sleep.temps_defaut = 300;
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

	public static void acceleration(Hexapode hexa) throws GoToException, EnnemiException
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
