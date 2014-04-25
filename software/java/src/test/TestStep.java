package test;
import java.util.LinkedList;
import java.util.List;

public class TestStep {

	public List<String> etats;
	public List<Integer> resultats;
	
	public TestStep()
	{
		resultats = new LinkedList<Integer>();
		etats = new LinkedList<String>();
	}
	
	public void addStep(String etat, int resultat)
	{
		resultats.add(resultat);
		etats.add(etat);
	}
	
}
