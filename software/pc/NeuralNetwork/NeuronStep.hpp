#include "Neuron.hpp"

class NeuronStep : public Neuron
{
public:
	NeuronStep(unsigned numOutputs, unsigned index, unsigned layerNum, unsigned threshold);

private:
	double transfer(double x)
	{
		return (x > m_threshold) ? 1 : 0;
	}

    double transferDerivative(double x)
	{
		return (x == 0) ? 1 : 0;
	}

	double m_threshold;
};

NeuronStep::NeuronStep(unsigned numOutputs, unsigned index, unsigned layerNum, unsigned threshold):
			Neuron(numOutputs, index, layerNum)
{
	m_threshold = threshold;
}