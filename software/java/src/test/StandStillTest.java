package test;

import hexapode.Deplacement;

public class StandStillTest extends Test {

//	private Scanner scanner = new Scanner(System.in);
	private boolean last_good = false;
	
	public StandStillTest(Deplacement deplacement, int nbIteration,
			double consecutiveLearnTime, double pauseTime, boolean restartMarkov)
	{
		super(deplacement, nbIteration, consecutiveLearnTime, pauseTime, restartMarkov);
	}
	
	public StandStillTest(Deplacement deplacement)
	{
	    super(deplacement);
	}

	@Override
	public void onStart()
	{
	    // Passage commenté car génère une erreur

/*		if(last_good)
		{
			for(int i = 0; i < 6; i++)
                try
                {
                    hexapode.pattes[0][i].baisser();
                } catch (GoToException e)
                {
                    e.printStackTrace();
                }
			Sleep.sleep(500);
		}
		else
		hexapode.stand_up();*/
	}

	@Override
	public void onExit()
	{
		super.onExit();
		if(!last_good)
		{
			//hexapode.lay_down();
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
		Sleep.sleep(500);*/
	}

	@Override
	public void proceedTest()
	{
		/*EtatHexa etat_suivant = markov.next();
		
		hexapode.goto_etat(etat_suivant);*/
	}
	
	@Override
	public void updateNote()
	{
       /*Sleep.sleep(1000);
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
			markov = new MarkovNCoups(1, 2);
		else
			markov = chargement_matrice();
	}

	@Override
	public void terminate() {
		super.terminate();	// sauvegarde
		deplacement.desasserv();
	}

	@Override
	public void onBreak() {
	    deplacement.desasserv();
		last_good = false;		// afin qu'il se relève après la pause
	}

}
