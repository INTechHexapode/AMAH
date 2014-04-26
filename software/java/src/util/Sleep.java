package util;

public class Sleep {

    public static int temps_defaut = 300;
    
    public static void sleep()
    {
        sleep(temps_defaut);
    }
    
	public static void sleep(long millis)
	{
		try {
			Thread.sleep(millis);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
}
