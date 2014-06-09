package test;

import hexapode.Deplacement;
import hexapode.enums.Mode;

public class TestCoordinationPattes extends Test {

	public TestCoordinationPattes(Deplacement deplacement, int nbIteration, double consecutiveLearnTime, double pauseTime, boolean restartMarkov) 
	{
		super(deplacement, nbIteration, consecutiveLearnTime, pauseTime, restartMarkov);
	}

    public TestCoordinationPattes(Deplacement deplacement)
    {
        super(deplacement);
    }
	
	@Override
	public void onStart() {
		etat_actuel = etat_suivant;
	}
		
	@Override
	public void onBreak() {
	    deplacement.desasserv();
	}

	@Override
	public void proceedTest() {
		//On r�cup�re l'�tat suivant � tester
		etat_suivant = markov.next();
		
		//On demande � l'hexapode de se mettre en position
		try
        {
		    deplacement.goto_etat(etat_suivant);
        } catch (Exception e)
        {
            e.printStackTrace();
        }
		
		//On calcule la note en fonction de la transition 
	}

	@Override
	public void validTest() {
		// TODO Auto-generated method stub

	}

	@Override
	public void terminate() {
		super.terminate();	// sauvegarde
		deplacement.desasserv();
	}

	@Override
	public void init() 
	{
		markov = new MarkovNCoups(1, 2);
		deplacement.setMode(Mode.BIPHASE);
	}
	
    @Override
	public void updateNote()
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
