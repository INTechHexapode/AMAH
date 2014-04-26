#pragma once
#include "StdAfx.h"
#include <vector>
#include <iostream>
#include <cassert>
#include "Neuron.hpp"
#include "NeuronTanh.hpp"
#include "NeuronRectifiedLinear.hpp"
#include "NeuronLinear.hpp"
#include "NeuronStep.hpp"
#include "NeuronSigmoid.hpp"

typedef std::vector<Neuron*> Layer;

class Network
{
public:
    Network(const std::vector<unsigned> &topology, bool load);
	~Network();
    void feedForward(const std::vector<double> &inputVals);
    void backProp(const std::vector<double> &targetVals);
    void getResults(std::vector<double> &resultVals) const;
    double getRecentAverageError(void) const 
	{ 
		return m_recentAverageError; 
	}

	void saveDatas();

private:
    std::vector<Layer> m_layers; // m_layers[layerNum][neuronNum]
    double m_error;
    double m_recentAverageError;
    static double m_recentAverageSmoothingFactor;
};

double Network::m_recentAverageSmoothingFactor = 100.0; // Nombre de passage sur lequel on fait la moyenne

Network::Network(const std::vector<unsigned> &topology, bool load)
{
    unsigned numLayers = topology.size();
    for (unsigned l = 0; l < numLayers; l++)
	{
        m_layers.push_back(Layer());
        unsigned numOutputs = l == topology.size() - 1 ? 0 : topology[l + 1];

        // On a créé notre layer, il faut le remplir de neurones (on ajoute le bias à chaque layer)
        for (unsigned n = 0; n <= topology[l]; n++) 
		{
			NeuronTanh *neur = new NeuronTanh(numOutputs, n, l);
			if(load)
				neur->loadDatas();
            m_layers.back().push_back(neur);
        }

        // On force le bias à 1
        m_layers.back().back()->setOutputVal(1.0);
    }
}

Network::~Network()
{
	for(int l(0); l < m_layers.size(); ++l)
		for(int n(0); n < m_layers[l].size(); ++n)
			delete m_layers[l][n];
}

void Network::getResults(std::vector<double> &resultVals) const
{
    resultVals.clear();

    for (unsigned n = 0; n < m_layers.back().size() - 1; ++n) {
        resultVals.push_back(m_layers.back()[n]->getOutputVal());
    }
}

void Network::backProp(const std::vector<double> &targetVals)
{
    // On calcule l'erreur totale en sortie

    Layer &outputLayer = m_layers.back();
    m_error = 0.0;

    for (unsigned n = 0; n < outputLayer.size() - 1; ++n) {
        double delta = targetVals[n] - outputLayer[n]->getOutputVal();
        m_error += delta * delta;
    }
    m_error /= outputLayer.size() - 1; // On fait la moyenne des erreurs
    m_error = sqrt(m_error); // On prend la racine carée

    // On calcule la moyenne des RMS

    m_recentAverageError =
            (m_recentAverageError * m_recentAverageSmoothingFactor + m_error)
            / (m_recentAverageSmoothingFactor + 1.0);

    // On calcule le gradient du output layer

    for (unsigned n = 0; n < outputLayer.size() - 1; ++n) {
        outputLayer[n]->calcOutputGradients(targetVals[n]);
    }

    // On calcule le gradient des hidden layers

    for (unsigned layerNum = m_layers.size() - 2; layerNum > 0; --layerNum) {
        Layer &hiddenLayer = m_layers[layerNum];
        Layer &nextLayer = m_layers[layerNum + 1];

        for (unsigned n = 0; n < hiddenLayer.size(); ++n) {
            hiddenLayer[n]->calcHiddenGradients(nextLayer);
        }
    }

    // On met à jours le weight du output layers jusqu'au premier hidden layer

    for (unsigned layerNum = m_layers.size() - 1; layerNum > 0; --layerNum) {
        Layer &layer = m_layers[layerNum];
        Layer &prevLayer = m_layers[layerNum - 1];

        for (unsigned n = 0; n < layer.size() - 1; ++n) {
            layer[n]->updateInputWeights(prevLayer);
        }
    }
}

void Network::feedForward(const std::vector<double> &inputVals)
{
    assert(inputVals.size() == m_layers[0].size() - 1);

    // On associe les inputs aux neurones de l'input layer
    for (unsigned i = 0; i < inputVals.size(); ++i) {
        m_layers[0][i]->setOutputVal(inputVals[i]);
    }

    // On propage les données dans le réseau
    for (unsigned layerNum = 1; layerNum < m_layers.size(); layerNum++) {
        Layer &prevLayer = m_layers[layerNum - 1];
        for (unsigned n = 0; n < m_layers[layerNum].size() - 1; ++n) {
            m_layers[layerNum][n]->feedForward(prevLayer);
        }
    }
}

void Network::saveDatas()
{
	for (unsigned layerNum = 0; layerNum < m_layers.size(); ++layerNum)
        for (unsigned n = 0; n < m_layers[layerNum].size(); ++n)
			m_layers[layerNum][n]->saveDatas();
}