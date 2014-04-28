#include "Neuron.hpp"
#include <math.h> 

class NeuronSigmoid : public Neuron
{
public:
	NeuronSigmoid(unsigned numOutputs, unsigned index, unsigned layerNum);
private:
	double transfer(double x)
	{
		return 1 / (1 + exp(-x));
	}

    double transferDerivative(double x)
	{
		// aproximation tanh dérivée
		return (1 / (1 + exp(-x)))*(1 - 1 / (1 + exp(-x)));
	}
};

NeuronSigmoid::NeuronSigmoid(unsigned numOutputs, unsigned index, unsigned layerNum):
				Neuron(numOutputs, index, layerNum)
{

}