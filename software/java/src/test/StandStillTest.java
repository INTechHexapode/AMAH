package test;

import hexapode.Hexapode;
import hexapode.markov.Markov;

public class StandStillTest extends Test {

	public StandStillTest(Hexapode hexapode, int nbIteration,
			double consecutiveLearnTime, double pauseTime)
	{
		super(hexapode, nbIteration, consecutiveLearnTime, pauseTime);
		markov = new Markov(6);
	}

	@Override
	public void onStart()
	{
		hexapode.stand_up();
	}

	@Override
	public void onExit()
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void proceedTest()
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void init() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void terminate() {
		// TODO Auto-generated method stub
		
	}

}
