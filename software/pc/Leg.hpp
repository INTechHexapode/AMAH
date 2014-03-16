#ifndef LEG_H
#define LEG_H

#include <vector>
#include <stdio.h>

#include "Servo.hpp"
#include "serialib.h"
#include "utility.hpp"

class Leg
{
	public:
		Leg(serialib& serial, std::string idAlpha, std::string idBeta, std::string idEta);
		~Leg();
		//Arrête les servomoteurs de la jambe
		void disable();
		//Déplace les trois servos aux angles alpha, beta et eta
		void moveAngle(int alpha, int beta, int eta);
		//Met à jour la position des trois servos à leurs m_currentAngle respectifs
		void update();
		//Active les trois servos
		void setReady();
		//Met jour les m_currentAngle respectifs des servos pour atteinre la position (x,y,z)
		void goTo(int x, int y, int z);
		void move(legPosAlpha alpha, legPosHigh high, legPosGap gap);
	
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

void Leg::moveAngle(int alpha, int beta, int eta)
{
	m_alpha.move(alpha);
	m_beta.move(beta);
	m_eta.move(eta);
}

void Leg::update()
{
	moveAngle(m_alpha.m_currentPule, m_beta.m_currentPule, m_eta.m_currentPule);
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

void Leg::move(legPosAlpha alpha, legPosHigh high, legPosGap gap)
{
	int angleAlpha(0);
	int angleBeta(0);
	int angleEta(0);
	switch(alpha)
	{
		case LEFT:
			angleAlpha = 1850;
			break;
		case RIGHT:
			angleAlpha = 1300;
			break;
		case MIDDLE:
			angleAlpha = 1500;
			break;
	}
	
	if(high == TOP)
	{
		if(gap == FAR)
			angleEta = 1600;
		else if(gap == CLOSE)
			angleEta = 1800;
		angleBeta = 1900;
	}
	else if(high == BOTTOM)
	{
		if(gap == FAR)
			angleEta = 1400;
		else if(gap == CLOSE)
			angleEta = 1500;
		angleBeta = 1400;
	}
		
	
		
	m_alpha.move(angleAlpha);
	m_beta.move(angleBeta);
	m_eta.move(angleEta);
}

#endif
