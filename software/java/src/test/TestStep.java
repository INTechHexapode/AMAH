package test;
import java.util.LinkedList;
import java.util.List;

import hexapode.markov.EtatHexa;

public class TestStep {

	public List<EtatHexa> etats;
	public List<Integer> resultats;
	
	public TestStep()
	{
		resultats = new LinkedList<Integer>();
		etats = new LinkedList<EtatHexa>();
	}
	
	public void addStep(EtatHexa etat, int resultat)
	{
		resultats.add(resultat);
		etats.add(etat);
	}
	
}
