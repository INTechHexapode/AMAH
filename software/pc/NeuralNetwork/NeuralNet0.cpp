#include "StdAfx.h"

#include <vector>
#include <iostream>

#include "Neuron.hpp"
#include "Network.hpp"
#include "Trainer.hpp"

void test(Network &network, const std::vector<double> &inputVals, const std::vector<double> &targetVals, const int pass, const int MAX)
{
	if(pass <= MAX)
	{
		std::vector<double> resultVals;
		network.feedForward(inputVals);
		network.backProp(targetVals);
		network.getResults(resultVals);
		if(pass == MAX)
		{
			std::cout << "input : " << inputVals[0] << " " << inputVals[1] << std::endl;
			std::cout << "result : " << resultVals[0] << " " << "error : " << network.getRecentAverageError() << std::endl;
			std::cout <<  std::endl;
		}
	}
}

std::vector<int> getAngle(std::vector<int> position)
{
	int x = position[0];
	int y = position[1];
	int z = position[2];



}

int _tmain(int argc, _TCHAR* argv[])
{
	std::vector<unsigned> topology;
	topology.push_back(2);
	topology.push_back(4);
	topology.push_back(1);
	Network network(topology, true);

	//Trainer train(network);
	//train.loadDatas();
	
	std::vector<double> inputVals;
	std::vector<double> targetVals;
	std::vector<double> resultVals;
	inputVals.push_back(0);
	inputVals.push_back(1);
	targetVals.push_back(0);
	const int MAX(5000);
	/*for(int pass(0); pass <= MAX; pass++)
	{
		inputVals[0] = 3;
		inputVals[1] = 1;
		targetVals[0] = 1;
		test(network, inputVals, targetVals, pass, MAX);
		
		inputVals[0] = 1;
		inputVals[1] = 3;
		targetVals[0] = 1;
		test(network, inputVals, targetVals, pass, MAX);

		inputVals[0] = 2;
		inputVals[1] = 2;
		targetVals[0] = 1;
		test(network, inputVals, targetVals, pass, MAX);
		
		inputVals[0] = 4;
		inputVals[1] = 0;
		targetVals[0] = 1;
		test(network, inputVals, targetVals, pass, MAX);

		inputVals[0] = 1;
		inputVals[1] = 1;
		targetVals[0] = 0;
		test(network, inputVals, targetVals, pass, MAX);
		
		inputVals[0] = 0;
		inputVals[1] = 1;
		targetVals[0] = 0;
		test(network, inputVals, targetVals, pass, MAX);

		inputVals[0] = 0;
		inputVals[1] = 0;
		targetVals[0] = 0;
		test(network, inputVals, targetVals, pass, MAX);
		
		inputVals[0] = 15;
		inputVals[1] = 10;
		targetVals[0] = 0;
		test(network, inputVals, targetVals, pass, MAX);
		
		inputVals[0] = 5;
		inputVals[1] = 3;
		targetVals[0] = 0;
		test(network, inputVals, targetVals, pass, MAX);
		
		inputVals[0] = 4;
		inputVals[1] = 1;
		targetVals[0] = 0;
		test(network, inputVals, targetVals, pass, MAX);
		
		inputVals[0] = 3;
		inputVals[1] = 0;
		targetVals[0] = 0;
		test(network, inputVals, targetVals, pass, MAX);
		
		inputVals[0] = 1000;
		inputVals[1] = 1000;
		targetVals[0] = 0;
		test(network, inputVals, targetVals, pass, MAX);
	}
	inputVals[0] = 3;
	inputVals[1] = 0;
	test(network, inputVals, targetVals, 0, 0);
	network.saveDatas();*/
	while(42){}
	return 0;
}