package hexapode.markov;

import hexapode.Hexapode;


/**
 * Gère le déroulement des tests.
 * @author Stud
 *
 */


public class TestEngine {
	
	private Hexapode hexapode;
	private Test test;
	
	public TestEngine(Hexapode hexapode, Test test)
	{
		this.hexapode = hexapode;
		this.test = test;
	}
	
	public void start()
	{
		long lastPauseTime = System.currentTimeMillis();
		for(int index = 0; index < test.getNbIteration(); ++index)
		{
			long currentTime = System.currentTimeMillis();
			if(!((currentTime - lastPauseTime) / 1000 > test.getConsecutiveLearnTime()))
			{
				test.onStart();
				test.proceedTest();
				test.onExit();
			}
			else if((currentTime - lastPauseTime) / 1000 > test.getPauseTime())
			{
				lastPauseTime = System.currentTimeMillis();
			}
		}
	}

}
