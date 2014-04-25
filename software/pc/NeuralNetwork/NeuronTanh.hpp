#include "Neuron.hpp"

class NeuronTanh : public Neuron
{
public:
	NeuronTanh(unsigned numOutputs, unsigned index, unsigned layerNum);
private:
	double transfer(double x)
	{
		return tanh(x);
	}

    double transferDerivative(double x)
	{
		// aproximation tanh dérivée
		return 1.0 - x * x;
	}
};

NeuronTanh::NeuronTanh(unsigned numOutputs, unsigned index, unsigned layerNum):
				Neuron(numOutputs, index, layerNum)
{

}