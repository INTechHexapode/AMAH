package test;

import java.util.Scanner;

import util.Sleep;
import hexapode.Hexapode;
import hexapode.markov.EtatHexa;
import hexapode.markov.Markov;

public class StandStillTest extends Test {

	private Scanner scanner = new Scanner(System.in);
	private boolean last_good = false;
	
	public StandStillTest(Hexapode hexapode, int nbIteration,
			double consecutiveLearnTime, double pauseTime, boolean restartMarkov, boolean validation)
	{
		super(hexapode, nbIteration, consecutiveLearnTime, pauseTime, restartMarkov, validation);
	}

	@Override
	public void onStart()
	{
		if(last_good)
		{
			for(int i = 0; i < 6; i++)
				hexapode.baisserPatte(i);
			Sleep.sleep(500);
		}
		else
		hexapode.stand_up();
	}

	@Override
	public void onExit()
	{
		super.onExit();
		if(!last_good)
		{
			hexapode.lay_down();
			Sleep.sleep(100);
		}
	}
	
	@Override
	public void validTest()
	{
		/*etat_suivant = markov.next();
		do {
		etat_suivant = markov.next();
		} while(markov.getMat(etat_suivant) < 2.);
		
		hexapode.goto_etat(etat_suivant);
		last_good = true;
		Sleep.sleep(500);		*/
	}

	@Override
	public void proceedTest()
	{
		/*EtatHexa etat_suivant = markov.next();
		
		hexapode.goto_etat(etat_suivant);
		Sleep.sleep(1000);
		System.out.println("Veuillez entrer le résultat: 0 (tombé) ou 1 (debout)");
		try {
			result = Integer.parseInt(scanner.nextLine());		
			last_good = (result == 1);
			markov.updateMatrix(result, etat_suivant);
		}
		catch(Exception e)
		{
			System.out.println("Erreur: itération ignorée");
		}*/
	}

	@Override
	public void init() {
		if(restartMarkov)
			markov = new Markov(2);
		else
			markov = chargement_matrice();
	}

	@Override
	public void terminate() {
		super.terminate();	// sauvegarde
		hexapode.desasserv();
	}

	@Override
	public void onBreak() {
		hexapode.desasserv();
		last_good = false;		// afin qu'il se relève après la pause
	}

}
