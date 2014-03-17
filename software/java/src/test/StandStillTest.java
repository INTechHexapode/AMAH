package test;

import java.util.Scanner;

import util.Sleep;
import hexapode.Hexapode;
import hexapode.markov.EtatHexa;
import hexapode.markov.Markov;

public class StandStillTest extends Test {

	Scanner scanner = new Scanner(System.in);
	
	public StandStillTest(Hexapode hexapode, int nbIteration,
			double consecutiveLearnTime, double pauseTime, boolean restartMarkov)
	{
		super(hexapode, nbIteration, consecutiveLearnTime, pauseTime, restartMarkov);
	}

	@Override
	public void onStart()
	{
		hexapode.stand_up();
	}

	@Override
	public void onExit()
	{
		hexapode.lay_down();
		Sleep.sleep(1000);
		sauvegarde_matrice();
	}

	@Override
	public void proceedTest()
	{
		EtatHexa etat_suivant = markov.next();
		hexapode.goto_etat(etat_suivant);
		Sleep.sleep(500);
		System.out.println("Veuillez entrer le résultat: 0 (tombé) ou 1 (debout)");
		int result = Integer.parseInt(scanner.nextLine());		
		markov.updateMatrix(result, etat_suivant);
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
	}

	@Override
	public void onBreak() {
		hexapode.desasserv();
	}

}
