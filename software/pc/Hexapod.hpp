#ifndef HEXAPOD_H
#define HEXAPOD_H

#include <vector>
#include <memory>

#include "Leg.hpp"
#include "serialib.h"

class Hexapod
{
	public:
		Hexapod(serialib& serial);
		~Hexapod();
	private:
		std::vector<std::shared_ptr<Leg> > m_legs;
		Leg m_leg0;
		Leg m_leg1;
		Leg m_leg2;
		Leg m_leg3;
		Leg m_leg4;
		Leg m_leg5;
};

Hexapod::Hexapod(serialib& serial):
	m_leg0(serial, "29", "30", "31"),
	m_leg1(serial, "13", "14", "15"),
	m_leg2(serial, "25", "26", "27"),
	m_leg3(serial, "8", "9", "10"),
	m_leg4(serial, "17", "18", "19"),
	m_leg5(serial, "0", "1", "2")
{
	m_legs.push_back(std::shared_ptr<Leg>(new Leg(m_leg0)));
	m_legs.push_back(std::shared_ptr<Leg>(new Leg(m_leg1)));
	m_legs.push_back(std::shared_ptr<Leg>(new Leg(m_leg2)));
	m_legs.push_back(std::shared_ptr<Leg>(new Leg(m_leg3)));
	m_legs.push_back(std::shared_ptr<Leg>(new Leg(m_leg4)));
	m_legs.push_back(std::shared_ptr<Leg>(new Leg(m_leg5)));
}

Hexapod::~Hexapod()
{
	
}

#endif
