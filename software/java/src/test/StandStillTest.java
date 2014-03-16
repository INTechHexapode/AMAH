package test;

import hexapode.Hexapode;
import hexapode.markov.EtatHexa;
import hexapode.markov.Markov;

public class StandStillTest extends Test {

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
		
		
	}

	@Override
	public void proceedTest()
	{
		EtatHexa etat = hexapode.getEtat_actuel();
		hexapode.goto_etat((markov.next(etat)));
		try
		{
			Thread.sleep(2000);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
		int result = Integer.parseInt(System.console().readLine());
		markov.updateMatrix(result);
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
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onBreak() {
		hexapode.desasserv();
	}

}
