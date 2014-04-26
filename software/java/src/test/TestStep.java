package test;
import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

public class TestStep implements Serializable {

	/**
	 * @author : Stud
	 */
	private static final long serialVersionUID = 350894876388230279L;
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
	
	@Override
	public String toString()
	{
		String out = new String();

		for(int i=1; i < resultats.size(); ++i)
		{
			out += etats.get(i-1).toString() + resultats.get(i).toString() + "\n";
		}
		
		return out;
	}
}
