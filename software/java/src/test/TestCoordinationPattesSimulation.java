package test;

import hexapode.Deplacement;
import hexapode.enums.Mode;

public class TestCoordinationPattesSimulation extends Test {

		public TestCoordinationPattesSimulation(Deplacement deplacement, int nbIteration, double consecutiveLearnTime, double pauseTime, boolean restartMarkov) 
		{
			super(deplacement, nbIteration, consecutiveLearnTime, pauseTime, restartMarkov);
		}

	    public TestCoordinationPattesSimulation(Deplacement deplacement)
	    {
	        super(deplacement);
	    }

		@Override
		public void onStart() {
			note = 0;
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

		}

		@Override
		public void validTest() {
			etat_suivant = markov.nextValidation(markov.string2index(etat_actuel));

			//On demande � l'hexapode de se mettre en position
			try
            {
			    System.out.println(etat_suivant);
			    deplacement.goto_etat(etat_suivant);
            } catch (Exception e)
            {
                e.printStackTrace();
            }
		}

		@Override
		public void terminate() {
			super.terminate();	// sauvegarde
			deplacement.desasserv();
		}

		@Override
		public void init() 
		{
			if(validation)
			{
				markov=chargement_matrice();
			}
			else
			{
				markov = new MarkovNCoups(1,2);
			}
			
			deplacement.setMode(Mode.BIPHASE);
			
			etat_actuel = new String("000000");
			etat_suivant = new String("000000");
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
