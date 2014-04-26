#include "Neuron.hpp"

class NeuronLinear : public Neuron
{
public:
	NeuronLinear(unsigned numOutputs, unsigned index, unsigned layerNum);
private:
	double transfer(double x)
	{
		return x;
	}

    double transferDerivative(double x)
	{
		return 1.0;
	}
};

NeuronLinear::NeuronLinear(unsigned numOutputs, unsigned index, unsigned layerNum):
				Neuron(numOutputs, index, layerNum)
{

}