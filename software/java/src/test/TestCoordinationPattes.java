package test;

import hexapode.Hexapode;
import hexapode.exceptions.EnnemiException;
import hexapode.markov.Markov;

public class TestCoordinationPattes extends Test {

	public TestCoordinationPattes(Hexapode hexapode, int nbIteration, double consecutiveLearnTime, double pauseTime, boolean restartMarkov, boolean validation) 
	{
		super(hexapode, nbIteration, consecutiveLearnTime, pauseTime, restartMarkov, validation);
	}

	@Override
	public void onStart() {
		etat_actuel = etat_suivant;
	}
	
	@Override
	public void onExit()
	{
		super.onExit();
		markov.updateMatrix(note, etat_actuel, etat_suivant);
	}
	

	@Override
	public void onBreak() {
		hexapode.desasserv();
	}

	@Override
	public void proceedTest() {
		//On r�cup�re l'�tat suivant � tester
		etat_suivant = markov.next();
		
		//On demande � l'hexapode de se mettre en position
		try
        {
            hexapode.goto_etat(etat_suivant);
        } catch (EnnemiException e)
        {
            e.printStackTrace();
        }
		
		//On calcule la note en fonction de la transition 
		calcNote();
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
	
	private void calcNote()
	{
		int nbRetourArriere = 0;

		for(int i = 0; i < 6; i++)
		{
			if(etat_actuel.charAt(i) == '0')
			{
				if(etat_suivant.charAt(i) == '1')
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
				if(etat_suivant.charAt(i) == '1')
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
