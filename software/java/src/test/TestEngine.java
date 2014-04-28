package test;

import util.Sleep;

/**
 * G�re le d�roulement des tests.
 * @author Stud
 *
 */


public class TestEngine {
	
	private Test test;
	
	public TestEngine(Test test)
	{
		this.test = test;
		test.init();
	}
	
	public void start()
	{
		long lastPauseTime = System.currentTimeMillis();
        int pourcentage = 0;
		for(int index = 0; index < test.getNbIteration(); ++index)
		{
			long currentTime = System.currentTimeMillis();
			if(!((currentTime - lastPauseTime) / 1000 > test.getConsecutiveLearnTime()))
			{
			    if(100*(index+1)/test.getNbIteration() > pourcentage)
			    {
			        pourcentage = 100*(index+1)/test.getNbIteration();
	                System.out.println(pourcentage+"%");
			    }
				test.onStart();
				if(test.isValidation())
					test.validTest();
				else
					test.proceedTest();
				test.onExit();
			}
			else
			{
				System.out.println("Début de pause. Durée: "+test.getPauseTime()+"s");
				test.onBreak();
				Sleep.sleep((long)(test.getPauseTime() * 1000));
				System.out.println("Fin de pause.");
				lastPauseTime = System.currentTimeMillis();
			}
		}
		test.terminate();
	}

}
