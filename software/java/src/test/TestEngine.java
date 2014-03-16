package test;

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
		for(int index = 0; index < test.getNbIteration(); ++index)
		{
			long currentTime = System.currentTimeMillis();
			if(!((currentTime - lastPauseTime) / 1000 > test.getConsecutiveLearnTime()))
			{
				test.onStart();
				test.proceedTest();
				test.onExit();
			}
			else
			{
				test.onBreak();
				try 
				{
					Thread.sleep((long)(test.getPauseTime() * 1000));
				} 
				catch (InterruptedException e) 
				{
					e.printStackTrace();
				}
				lastPauseTime = System.currentTimeMillis();
			}
		}
		test.terminate();
	}

}
