#ifndef HEXAPOD_H
#define HEXAPOD_H

#include <vector>
#include <memory>
#include <unistd.h>

#include "Leg.hpp"
#include "serialib.h"

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
	std::vector<std::shared_ptr<Leg> > tri1;
	tri1.push_back(m_legs[1]);
	tri1.push_back(m_legs[2]);
	tri1.push_back(m_legs[4]);
	moveListLegs(tri1, 1500, 1500, 1500);
}

void Hexapod::update()
{
	for(unsigned int l(0); l < m_legs.size(); ++l)
		m_legs[l]->update();
}

void Hexapod::moveListLegs(std::vector<std::shared_ptr<Leg> > legs, int alpha, int beta, int eta)
{
	std::cout << legs.size() << std::endl;
	for(unsigned int l(0); l < legs.size(); ++l)
		m_legs[l]->move(alpha, beta, eta);
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
	
	for(int i(0); i < 5; ++i)
	{
		moveListLegs(tri1, 1400, 1900, 1500);
		sleep(1);
		moveListLegs(tri1, 1400, 1400, 1500);
		sleep(1);
		moveListLegs(tri2, 1400, 1900, 1500);
		sleep(1);
		moveListLegs(tri2, 1400, 1400, 1500);
		sleep(1);
	}
}


#endif
