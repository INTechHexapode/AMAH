#ifndef HEXAPOD_H
#define HEXAPOD_H

#include <vector>
#include <memory>

#include "Leg.hpp"
#include "serialib.h"
#include "utility.hpp"

class Hexapod
{
	public:
		Hexapod(serialib& serial);
		~Hexapod();
		void disable();
		void update();
		void moveListLegs(std::vector<std::shared_ptr<Leg> > legs, int alpha, int beta, int eta);
		void stand();
		void setReady();
		void test();
		void walk();
		
	private:
		std::vector<std::shared_ptr<Leg> > m_legs;
};

Hexapod::Hexapod(serialib& serial)
{
	m_legs.push_back(std::shared_ptr<Leg>(new Leg(serial, "29", "30", "31")));
	m_legs.push_back(std::shared_ptr<Leg>(new Leg(serial, "13", "14", "15")));
	m_legs.push_back(std::shared_ptr<Leg>(new Leg(serial, "25", "26", "27")));
	m_legs.push_back(std::shared_ptr<Leg>(new Leg(serial, "8", "9", "10")));
	m_legs.push_back(std::shared_ptr<Leg>(new Leg(serial, "17", "18", "19")));
	m_legs.push_back(std::shared_ptr<Leg>(new Leg(serial, "0", "1", "2")));
}

Hexapod::~Hexapod()
{
	
}

void Hexapod::disable()
{
	for(unsigned int l(0); l < m_legs.size(); ++l)
		m_legs[l]->disable();
}

void Hexapod::test()
{
	m_legs[0]->move(MIDDLE, BOTTOM, FAR);
}

void Hexapod::update()
{
	for(unsigned int l(0); l < m_legs.size(); ++l)
		m_legs[l]->update();
}

void Hexapod::moveListLegs(std::vector<std::shared_ptr<Leg> > legs, int alpha, int beta, int eta)
{
	for(unsigned int l(0); l < legs.size(); ++l)
		legs[l]->moveAngle(alpha, beta, eta);
	milliSleep(100);
}

void Hexapod::stand()
{
	std::vector<std::shared_ptr<Leg> > tri1;
	tri1.push_back(m_legs[1]);
	tri1.push_back(m_legs[2]);
	tri1.push_back(m_legs[5]);
	
	std::vector<std::shared_ptr<Leg> > tri2;
	tri2.push_back(m_legs[0]);
	tri2.push_back(m_legs[3]);
	tri2.push_back(m_legs[4]);
	
	setReady();
	
	for(int i(0); i < 6; ++i)
	{
		moveListLegs(tri1, 1400, 1900, 1500);
		moveListLegs(tri1, 1400, 1400, 1500);
		moveListLegs(tri2, 1400, 1900, 1500);
		moveListLegs(tri2, 1400, 1400, 1500);
	}
}

void Hexapod::setReady()
{
	for(unsigned int l(0); l < m_legs.size(); ++l)
		m_legs[l]->setReady();
}

void Hexapod::walk()
{
	/*while (true)
	{
		setReady();
		int pulseMin = 1200;
		int pulseMax = 1500;
		std::vector<std::shared_ptr<Leg> > trilegs1;
		std::vector<std::shared_ptr<Leg> > trilegs2;
		trilegs1.push_back(m_legs[0]);
		trilegs1.push_back(m_legs[3]);
		trilegs1.push_back(m_legs[4]);
		
		trilegs2.push_back(m_legs[1]);
		trilegs2.push_back(m_legs[2]);
		trilegs2.push_back(m_legs[5]);
		
		moveListLegs(trilegs1, pulsemin, 1900, 1500)
		moveListLegs([legs[3]], pulsemax, 1900, 1500)
		
		moveListLegs([legs[0], legs[4]], pulsemin, 1400, 1500)
		moveListLegs([legs[3]], pulsemax, 1400, 1500)
		
		moveListLegs([legs[1], legs[5]], pulsemax, 1900, 1500)
		moveListLegs([legs[2]], pulsemin, 1900, 1500)
		
		
		moveListLegs([legs[0], legs[4]], pulsemax, 1400, 1500)
		moveListLegs([legs[3]], pulsemin, 1400, 1500)
		
		moveListLegs([legs[1], legs[5]], pulsemax, 1400, 1500)
		moveListLegs([legs[2]], pulsemin, 1400, 1500)
		
		moveListLegs([legs[1], legs[5]], pulsemin, 1400, 1500)
		moveListLegs([legs[2]], pulsemax, 1400, 1500)
	}*/	
}


#endif
