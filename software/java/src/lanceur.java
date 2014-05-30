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
            //lanceur_coupe(hexa, deplacement);
            danse(deplacement, hexa);

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
        boolean symetrie = hexa.initialiser_match();
        
        try {
            if(!symetrie)
            {
                hexa.setAngle(-Math.PI*5/6);
                hexa.avancer(700);
                hexa.va_au_point_relatif((new Vec2(-250, 500)));
                Thread.sleep(2000);
                Thread.sleep(2000);
                
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
	
	public static void danse(Deplacement deplacement, Hexapode hexa)
	{
		
		hexa.initialiser();

        try {
    		Thread.sleep(2000);
    		
        	hexa.lever_milieu();
   	
        	try {
        		//bruit de moteur
    			deplacement.tourner_fixe(300);
    			Thread.sleep(2000);
    			
    			deplacement.tourner_fixe(-300);
    			Thread.sleep(1300);
    			deplacement.lever(1);
    			Thread.sleep(500);
    			
    			//début : pattes sur le côté
    			deplacement.go_to_angle(1, 1200, 2000, 1800);
    			deplacement.lever(4);
    			Thread.sleep(200);
    			hexa.lever_except("haut", new int[]{1,4});
    			Thread.sleep(300);
    			deplacement.go_to_angle(1, 1200, 2000, 2000);
    			deplacement.go_to_angle(4, 1200, 2000, 1800);
    			Thread.sleep(500);
    			deplacement.go_to_angle(4, 1200, 2000, 2000);
    			deplacement.go_to_angle(1, 1200, 2000, 1800);
    			
    			//demi temps, on monte et on continu à bouger les pattes sur le côté, rapidement
    			Thread.sleep(500);
    			deplacement.go_to_angle(1, 1200, 2000, 2000);
    			deplacement.go_to_angle(4, 1200, 2000, 1800);
    			Thread.sleep(200);
    			deplacement.go_to_angle(4, 1200, 2000, 2000);
    			deplacement.go_to_angle(1, 1200, 2000, 1800);
    			Thread.sleep(200);
    			deplacement.go_to_angle(1, 1200, 2000, 2000);
    			deplacement.go_to_angle(4, 1200, 2000, 1800);
    			Thread.sleep(500);
    			deplacement.go_to_angle(4, 1200, 2000, 2000);
    			deplacement.go_to_angle(1, 1200, 2000, 2000);
    			Thread.sleep(550);
    			deplacement.tourner_fixe(300);
    			Thread.sleep(950);
    			
    			//On tourne en haut, on suit le rythme au moment où le rythme change
    			deplacement.go_to_angle(1, 1200, 2000, 1800);
    			deplacement.go_to_angle(4, 1200, 2000, 1800);
    			deplacement.tourner_fixe(-300);
    			Thread.sleep(250);
    			deplacement.tourner_fixe(300);
    			Thread.sleep(1150);
    			deplacement.tourner_fixe(-300);
    			Thread.sleep(700);
    			deplacement.go_to_angle(1, 1500, 2000, 1800);
    			deplacement.go_to_angle(4, 1500, 2000, 1800);
    			Thread.sleep(500);
    			hexa.lever_except("milieu", new int[]{1,4});
    			Thread.sleep(650);
    			hexa.lever_bas();
    			Thread.sleep(550);
    			
    			//On ramène les pattes du côté à la normale
            	hexa.lever_milieu();
        		Thread.sleep(300);
        		
    			//On pointe le doigt
    			//doigt 1
    			deplacement.go_to_angle(0, 1500, 2000, 1200);
        		Thread.sleep(400);
    			deplacement.go_to_angle(0, 1500, 2000, 1500);
        		Thread.sleep(400);
    			deplacement.go_to_angle(0, 1350, 2000, 1200);
        		Thread.sleep(400);
        		//doigt 2
    			deplacement.go_to_angle(0, 1350, 2000, 1200);
        		Thread.sleep(400);
    			deplacement.go_to_angle(0, 1350, 2000, 1500);
        		Thread.sleep(300);
    			deplacement.go_to_angle(0, 1200, 2000, 1200);
        		//doigt 3
    			deplacement.go_to_angle(0, 1200, 2000, 1200);
        		Thread.sleep(300);
    			deplacement.go_to_angle(0, 1200, 2000, 1500);
        		Thread.sleep(300);
    			deplacement.go_to_angle(0, 1050, 2000, 1200);
        		//doigt 4
    			deplacement.go_to_angle(0, 1050, 2000, 1200);
        		Thread.sleep(300);
    			deplacement.go_to_angle(0, 1050, 2000, 1500);
        		Thread.sleep(300);
    			deplacement.go_to_angle(0, 900, 2000, 1200);
    			
    			//On range le doigt
        		Thread.sleep(500);
    			deplacement.go_to_angle(0, 1200, 1700, 1800);
    			
    			//Et on rebouge les pattes sur le côté !
    			Thread.sleep(500);
    			deplacement.go_to_angle(1, 1200, 2000, 1800);
    			deplacement.lever(4);
    			Thread.sleep(300);
    			deplacement.go_to_angle(1, 1200, 2000, 2000);
    			deplacement.go_to_angle(4, 1200, 2000, 1800);
    			Thread.sleep(300);
    			deplacement.go_to_angle(4, 1200, 2000, 2000);
    			deplacement.go_to_angle(1, 1200, 2000, 1800);
    			Thread.sleep(500);
    			deplacement.go_to_angle(1, 1200, 2000, 2000);
    			deplacement.go_to_angle(4, 1200, 2000, 1800);
    			Thread.sleep(500);
    			deplacement.go_to_angle(4, 1200, 2000, 2000);
    			deplacement.go_to_angle(1, 1200, 2000, 1800);

                Sleep.temps_defaut = 1000;
            	hexa.lever_bas();
                Sleep.temps_defaut = 200;
                Thread.sleep(3400);
            	hexa.lever_haut();
                Thread.sleep(200);
    			deplacement.go_to_angle(1, 1200, 2000, 2000);
    			deplacement.go_to_angle(4, 1200, 2000, 1800);
                Thread.sleep(200);
                deplacement.tendre(1);
                deplacement.tendre(4);
                
                //fait l'oiseaux
                Sleep.temps_defaut = 850;
    			hexa.lever_except("milieu", new int[]{1,4});
    			Thread.sleep(Sleep.temps_defaut/2);
    			deplacement.go_to_angle(1, 1200, 1700, 1400);
    			deplacement.go_to_angle(4, 1200, 1700, 1400);
    			Thread.sleep(500);
    			hexa.lever_except("haut", new int[]{1,4});
    			Thread.sleep(Sleep.temps_defaut/2);
    			deplacement.go_to_angle(1, 1200, 2000, 1400);
    			deplacement.go_to_angle(4, 1200, 2000, 1400);
    			Thread.sleep(500);
    			hexa.lever_except("milieu", new int[]{1,4});
    			Thread.sleep(Sleep.temps_defaut/2);
    			deplacement.go_to_angle(1, 1200, 1700, 1400);
    			deplacement.go_to_angle(4, 1200, 1700, 1400);
    			Thread.sleep(500);
    			hexa.lever_except("haut", new int[]{1,4});
    			Thread.sleep(Sleep.temps_defaut/2);
    			deplacement.go_to_angle(1, 1200, 2000, 1400);
    			deplacement.go_to_angle(4, 1200, 2000, 1400);
    			Sleep.temps_defaut = 200;
    			Thread.sleep(500);
    			deplacement.tourner_fixe(300);
    			Thread.sleep(500);
    			hexa.lever_except("milieu gauche", new int[]{1,4});
    			Thread.sleep(500);
    			hexa.lever_except("haut gauche", new int[]{1,4});
    			Thread.sleep(500);
    			deplacement.tourner_fixe(-300);
    			Thread.sleep(500);
    			hexa.lever_except("milieu", new int[]{1,4});
    			Thread.sleep(500);
    			hexa.lever_except("haut", new int[]{1,4});
    			
    			
    			//Changement de pattes
                Sleep.temps_defaut = 100;
            	hexa.lever_haut();
            	Thread.sleep(500);
            	deplacement.tendre(1);
            	deplacement.tendre(4);
            	
            	Thread.sleep(500);
            	hexa.lever_haut();
            	Thread.sleep(100);
            	deplacement.tendre(0);
            	deplacement.tendre(5);

            	Thread.sleep(500);
            	hexa.lever_haut();
            	Thread.sleep(100);
            	deplacement.tendre(2);
            	deplacement.tendre(3);
            	
            	//oiseaux
            	Sleep.temps_defaut = 850;
    			hexa.lever_except("milieu", new int[]{1,4});
    			Thread.sleep(Sleep.temps_defaut/2);
    			deplacement.go_to_angle(1, 1200, 1700, 1400);
    			deplacement.go_to_angle(4, 1200, 1700, 1400);
    			Thread.sleep(500);
    			hexa.lever_except("haut", new int[]{1,4});
    			Thread.sleep(Sleep.temps_defaut/2);
    			deplacement.go_to_angle(1, 1200, 2000, 1400);
    			deplacement.go_to_angle(4, 1200, 2000, 1400);
    			Thread.sleep(500);
    			hexa.lever_except("milieu", new int[]{1,4});
    			Thread.sleep(Sleep.temps_defaut/2);
    			deplacement.go_to_angle(1, 1200, 1700, 1400);
    			deplacement.go_to_angle(4, 1200, 1700, 1400);
    			Thread.sleep(500);
    			hexa.lever_except("haut", new int[]{1,4});
    			Thread.sleep(Sleep.temps_defaut/2);
    			deplacement.go_to_angle(1, 1200, 2000, 1400);
    			deplacement.go_to_angle(4, 1200, 2000, 1400);
                

    		} catch (GoToException e) {
    			// TODO Auto-generated catch block
    			e.printStackTrace();
    		} catch (InterruptedException e) {
    			// TODO Auto-generated catch block
    			e.printStackTrace();
    		} 
        	
			/*hexa.lever_haut();
			deplacement.tendre(3);
			deplacement.tendre(2);
			Thread.sleep(500);
			Sleep.temps_defaut = 600;
			hexa.lever_except("milieu", new int[]{2,3});
			Thread.sleep(Sleep.temps_defaut/2);
			deplacement.go_to_angle(2, 1200, 1700, 1400);
			deplacement.go_to_angle(3, 1200, 1700, 1400);
			Thread.sleep(500);
			hexa.lever_except("haut", new int[]{2,3});
			Thread.sleep(Sleep.temps_defaut/2);
			deplacement.go_to_angle(2, 1200, 2000, 1400);
			deplacement.go_to_angle(3, 1200, 2000, 1400);
	        Sleep.temps_defaut = 200;
	        ent.go_to_angle(3, 1200, 2000, 1400);
			Thread.sleep(500);
			hexa.lever_except("milieu", new int[]{2,3});
			Thread.sleep(Sleep.temps_defaut/2);
			deplacement.go_to_angle(2, 1200, 1700, 1400);
			deplacement.go_to_angle(3, 1200, 1700, 1400);
			Thread.sleep(500);
			hexa.lever_except("haut", new int[]{2,3});
			Thread.sleep(Sleep.temps_defaut/2);
			deplacement.go_to_angle(2, 1200, 2000, 1400);
			deplacement.go_to_angle(3, 1200, 2000, 1400);
			Sleep.temps_defaut = 200;*/

    		Thread.sleep(2500);
		} catch (GoToException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}/*
		try {
			deplacement.tourner_fixe(300);
			Thread.sleep(100);
			
			deplacement.tourner_fixe(-300);
			Thread.sleep(100);
			deplacement.tourner_fixe(-300);
			Thread.sleep(100);
			deplacement.tourner_fixe(300);
			Thread.sleep(100);
		} catch (GoToException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
	    
	    /*int[] tab = {0,1,2,5,4,3};
	    for(int i = 0; i < 8; i++)
	    {
	        for(int j = 0; j < 6; j++)
	        {
                int k = tab[j];
	            deplacement.lever(k);
	            sleep.sleep();
                deplacement.baisser((k+5)%6);
	        }
	    }*/
	}
	
}

