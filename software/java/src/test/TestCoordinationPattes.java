package test;

import hexapode.Hexapode;
import hexapode.markov.EtatHexa;
import hexapode.markov.EtatPatteTest2;
import hexapode.markov.Markov;

public class TestCoordinationPattes extends Test {

	public TestCoordinationPattes(Hexapode hexapode, int nbIteration, double consecutiveLearnTime, double pauseTime, boolean restartMarkov, boolean validation) 
	{
		super(hexapode, nbIteration, consecutiveLearnTime, pauseTime, restartMarkov, validation);
	}

	@Override
	public void onStart() {
		note = 0;

	}

	@Override
	public void onBreak() {
		hexapode.desasserv();
	}

	@Override
	public void proceedTest() {
		EtatHexa nEtatSuivant = markov.next();
		
		calcNote(nEtatSuivant);

	}

	@Override
	public void validTest() {
		// TODO Auto-generated method stub

	}

	@Override
	public void terminate() {
		super.terminate();	// sauvegarde
		hexapode.desasserv();
	}

	@Override
	public void init() 
	{
		markov = new Markov(2);
	}
	
	private void calcNote(EtatHexa nEtatSuivant)
	{
		int nbRetourArriere = 0;
		for(int i = 0; i < 6; i++)
		{
			if(etat_suivant.epattes[i].etat == EtatPatteTest2.AVANT)
			{
				if(nEtatSuivant.epattes[i].etat == EtatPatteTest2.AVANT)
				{
					note += 5;
				}
				else
				{
					nbRetourArriere++;
				}
			}
			else
			{
				if(nEtatSuivant.epattes[i].etat == EtatPatteTest2.AVANT)
				{
					note += 5;
				}
			}
		}
		
		if(nbRetourArriere >= 3)
		{
			note += nbRetourArriere * 10;
		}
		else
		{
			note -=  -40;
		}
	}

}
