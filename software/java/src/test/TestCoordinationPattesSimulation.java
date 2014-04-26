package test;

import hexapode.Hexapode;
import hexapode.markov.EtatHexa;
import hexapode.markov.Markov;

public class TestCoordinationPattesSimulation extends Test {

		public TestCoordinationPattesSimulation(Hexapode hexapode, int nbIteration, double consecutiveLearnTime, double pauseTime, boolean restartMarkov, boolean validation) 
		{
			super(hexapode, nbIteration, consecutiveLearnTime, pauseTime, restartMarkov, validation);
		}

		@Override
		public void onStart() {
			note = 0;
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
			//On récupère l'état suivant à tester
			char[] nEtatSuivant = markov.next();

			etat_suivant = new EtatHexa(String.valueOf(nEtatSuivant));
			
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
			etat_actuel = new EtatHexa("000000");
		}
		
		private void calcNote()
		{
			int nbRetourArriere = 0;
			char[] cEtatSuivant = etat_suivant.etatString();
			char[] cEtatActuel = etat_actuel.etatString();
			for(int i = 0; i < 6; i++)
			{
				if(cEtatActuel[i] == '0')
				{
					if(cEtatSuivant[i] == '1')
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
					if(cEtatSuivant[i] == '1')
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
