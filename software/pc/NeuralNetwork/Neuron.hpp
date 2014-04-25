#pragma once
#include "StdAfx.h"

#include <vector>
#include <cmath>
#include <iostream>
#include <fstream>
#include <iostream>
#include <sstream>

struct Connection
{
    double weight;
    double deltaWeight;
};


class Neuron;

typedef std::vector<Neuron*> Layer;

class Neuron
{
public:
    Neuron(unsigned numOutputs, unsigned index, unsigned layerNum);
	~Neuron();

    void setOutputVal(double val) 
	{ 
		m_outputVal = val; 
	}
    double getOutputVal(void) const 
	{ 
		return m_outputVal; 
	}

    void feedForward(const Layer &prevLayer);
    void calcOutputGradients(double targetVal);
    void calcHiddenGradients(const Layer &nextLayer);
    void updateInputWeights(Layer &prevLayer);
	void saveDatas();
	void loadDatas();
	void updateLearningRate(double prevGradient, double newGradient);

protected:
    virtual double transfer(double x)=0;

    virtual double transferDerivative(double x)=0;

    static double randomWeight(void) 
	{ 
		return rand() / double(RAND_MAX); 
	}
	
    double sumDOW(const Layer &nextLayer) const;

    float eta;   //vitesse d'aprentissage
    static double alpha; //multiplicateur affectant les modifs précédente des weight (momentum)
    double m_outputVal;
    std::vector<Connection> m_outputWeights;
    unsigned m_index;
	unsigned m_layerNum;
    double m_gradient;
};

double Neuron::alpha = 0.5;

Neuron::Neuron(unsigned numOutputs, unsigned index, unsigned layerNum)
{
    for (unsigned c = 0; c < numOutputs; ++c) {
        m_outputWeights.push_back(Connection());
        m_outputWeights.back().weight = randomWeight();
    }
	eta = 0.15;
    m_index = index;
	m_layerNum = layerNum;
}

Neuron::~Neuron()
{
	
}

//Mise à jour des weight de la couche précédente
void Neuron::updateInputWeights(Layer &prevLayer)
{
    // On édite les weights des Connections de la couche précédente

    for (unsigned n = 0; n < prevLayer.size(); ++n) {
        Neuron &neuron = *prevLayer[n];
        double oldDeltaWeight = neuron.m_outputWeights[m_index].deltaWeight;

        double newDeltaWeight =
                // L'output modifié par le gradient et le coef d'apprentissage
                eta
                * neuron.getOutputVal()
                * m_gradient
                // On ajoute le momentum
                + alpha
                * oldDeltaWeight;

        neuron.m_outputWeights[m_index].deltaWeight = newDeltaWeight;
        neuron.m_outputWeights[m_index].weight += newDeltaWeight;
    }
}

//On sauvegarde les valeurs de chaque neurone dans un fichier
void Neuron::saveDatas()
{
	std::stringstream ss;
	std::string path("datas/neuron ");	
	ss << path << m_layerNum << " " << m_index << ".txt";
	std::string filename = ss.str();

	std::ofstream  fichier(filename, std::ios::out | std::ios::trunc);
  
        if(fichier)
        {       
			fichier << eta << std::endl;
			fichier << alpha << std::endl;
			fichier << m_outputVal << std::endl;
			fichier << m_gradient << std::endl;
			for(int i(0); i < m_outputWeights.size(); ++i)
			{
				fichier << m_outputWeights[i].weight << std::endl;
				fichier << m_outputWeights[i].deltaWeight << std::endl;
			}
            fichier.close();
        }
        else
			std::cerr << "Impossible d'ouvrir le fichier !" << std::endl;
  
}

void Neuron::loadDatas()
{
	std::stringstream ss;
	std::string path("datas/neuron ");	
	ss << path << m_layerNum << " " << m_index << ".txt";
	std::string filename = ss.str();

	std::ifstream  fichier(filename, std::ios::in);
  
        if(fichier)
        {       
			std::string contenu;

            getline(fichier, contenu);
			std::istringstream newEta(contenu);
			newEta >> eta;

			getline(fichier, contenu);
			std::istringstream newAlpha(contenu);
			newAlpha >> alpha;

			getline(fichier, contenu);
			std::istringstream outputVal(contenu);
			outputVal >> m_outputVal;

			getline(fichier, contenu);
			std::istringstream gradient(contenu);
			gradient >> m_gradient;

			m_outputWeights.clear();
			while(getline(fichier, contenu))
			 {
				m_outputWeights.push_back(Connection());
				std::istringstream weight(contenu);
				weight >> m_outputWeights.back().weight;

				getline(fichier, contenu);
				std::istringstream deltaWeight(contenu);
				deltaWeight >> m_outputWeights.back().deltaWeight;
			 }
            fichier.close();
        }
        else
			std::cerr << "Impossible d'ouvrir le fichier !" << std::endl;
  
}

//private
double Neuron::sumDOW(const Layer &nextLayer) const
{
    double sum = 0.0;

    for (unsigned n = 0; n < nextLayer.size() - 1; ++n) {
        sum += m_outputWeights[n].weight * nextLayer[n]->m_gradient;
    }

    return sum;
}

//Gradient pour les couches intermédiaires de neurones
void Neuron::calcHiddenGradients(const Layer &nextLayer)
{
    double dow = sumDOW(nextLayer);
    m_gradient = dow * transferDerivative(m_outputVal);
}

//Gradient pour la couche de fin
void Neuron::calcOutputGradients(double targetVal)
{
    double delta = targetVal - m_outputVal;
	double tempGradient = delta * transferDerivative(m_outputVal);
	//updateLearningRate(m_gradient, tempGradient);
    m_gradient = delta * transferDerivative(m_outputVal);
}

//Calcul de la valeur en sortie
void Neuron::feedForward(const Layer &prevLayer)
{
    double sum = 0.0;

    // On fait la somme des input en incluant le bias
    for (unsigned n = 0; n < prevLayer.size(); ++n) {
        sum += prevLayer[n]->getOutputVal() *
                prevLayer[n]->m_outputWeights[m_index].weight;
    }

    m_outputVal = transfer(sum);
}

void Neuron::updateLearningRate(double prevGradient, double newGradient)
{
	if(((prevGradient > 0) - (prevGradient < 0)) == ((newGradient > 0) - (newGradient < 0)))
		eta += 0.05;
	else
		eta *= 0.95;
}
