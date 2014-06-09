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
	    long debut_test = System.currentTimeMillis();
		long lastPauseTime = System.currentTimeMillis();
        int pourcentage = 0;
        System.out.println("0%");
		for(long index = 0; index < test.getNbIteration(); ++index)
		{
			long currentTime = System.currentTimeMillis();
			if((currentTime - lastPauseTime) / 1000 <= test.getConsecutiveLearnTime())
			{
			    if(100*index/(test.getNbIteration()-1) > pourcentage)
			    {
			        pourcentage = (int)(100*index/(test.getNbIteration()-1));
                    System.out.println(pourcentage+"%");
                    if((pourcentage%5)==0 && test.sometimes())
                    {
                        System.out.println("Test terminé car la convergence a été atteinte");
                        index = test.getNbIteration();
                    }
                    
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
				try
                {
                    Thread.sleep((long)(test.getPauseTime() * 1000));
                } catch (InterruptedException e)
                {
                    e.printStackTrace();
                }
				System.out.println("Fin de pause.");
				lastPauseTime = System.currentTimeMillis();
			}
		}
		test.terminate();
		long temps_total = System.currentTimeMillis() - debut_test; 
		System.out.println("Temps écoulé: "+temps_total/3600000+"h "+(temps_total/60000)%60+"m "+(temps_total/1000)%60+"s.");
	}

}
