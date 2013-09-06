#ifndef LEG_H
#define LEG_H

#include <vector>
#include <stdio.h>

#include "Servo.hpp"
#include "serialib.h"

class Leg
{
	public:
		Leg(serialib& serial, std::string idAlpha, std::string idBeta, std::string idEta);
		~Leg();
		//Arrête les servomoteurs de la jambe
		void disable();
		//Déplace les trois servos aux angles alpha, beta et eta
		void move(int alpha, int beta, int eta);
		//Met à jour la position des trois servos à leurs m_currentAngle respectifs
		void update();
		//Active les trois servos
		void setReady();
		//Met jour les m_currentAngle respectifs des servos pour atteinre la position (x,y,z)
		void goTo(int x, int y, int z);
	
	private:
		Servo m_alpha;
		Servo m_beta;
		Servo m_eta;
		//Distance entre alpha et beta
		int m_coxa;
		//Distance entre beta et eta
		int m_femur;
		//Distance entre eta et le sol
		int m_tibia;
};

Leg::Leg(serialib& serial, std::string idAlpha, std::string idBeta, std::string idEta):
	m_alpha(idAlpha, serial),
	m_beta(idBeta, serial),
	m_eta(idEta, serial),
	m_coxa(30),
	m_femur(60),
	m_tibia(150)
{
	
}

Leg::~Leg()
{
	
}

void Leg::disable()
{
	m_alpha.disable();
	m_beta.disable();
	m_eta.disable();
}

void Leg::move(int alpha, int beta, int eta)
{
	m_alpha.move(alpha);
	m_beta.move(beta);
	m_eta.move(eta);
}

void Leg::update()
{
	move(m_alpha.m_currentPule, m_beta.m_currentPule, m_eta.m_currentPule);
}

void Leg::setReady()
{
	m_alpha.setReady();
	m_beta.setReady();
	m_eta.setReady();
}

void Leg::goTo(int x, int y, int z)
{
	
}

#endif
