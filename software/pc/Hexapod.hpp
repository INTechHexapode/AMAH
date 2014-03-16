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
		void moveListLegs(std::vector<std::shared_ptr<Leg> > legs, legPosAlpha alpha, legPosHigh high, legPosGap gap);
		void stand();
		void setReady();
		void test();
		void walk();
		void rotate();
		
	private:
		std::vector<std::shared_ptr<Leg> > m_legs;
};

Hexapod::Hexapod(serialib& serial)
{
	m_legs.push_back(std::shared_ptr<Leg>(new Leg(serial, "0", "1", "2")));
	m_legs.push_back(std::shared_ptr<Leg>(new Leg(serial, "8", "9", "10")));
	m_legs.push_back(std::shared_ptr<Leg>(new Leg(serial, "13", "14", "15")));
	m_legs.push_back(std::shared_ptr<Leg>(new Leg(serial, "29", "30", "31")));
	m_legs.push_back(std::shared_ptr<Leg>(new Leg(serial, "25", "26", "27")));
	m_legs.push_back(std::shared_ptr<Leg>(new Leg(serial, "17", "18", "19")));
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
	//stand();
	walk();
	//setReady();
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

void Hexapod::moveListLegs(std::vector<std::shared_ptr<Leg> > legs, legPosAlpha alpha, legPosHigh high, legPosGap gap)
{
	for(unsigned int l(0); l < legs.size(); ++l)
		legs[l]->move(alpha, high, gap);
	milliSleep(100);
}

void Hexapod::rotate()
{
	for(int i(0); i< 4; ++i)
		for(unsigned int l(0); l < m_legs.size(); ++l)
		{
			m_legs[l]->move(LEFT, TOP, CLOSE);
			milliSleep(100);
			m_legs[l]->move(LEFT, BOTTOM, CLOSE);
			milliSleep(100);
		}
}

void Hexapod::stand()
{
	std::vector<std::shared_ptr<Leg> > tri1;
	tri1.push_back(m_legs[1]);
	tri1.push_back(m_legs[3]);
	tri1.push_back(m_legs[5]);
	
	std::vector<std::shared_ptr<Leg> > tri2;
	tri2.push_back(m_legs[0]);
	tri2.push_back(m_legs[2]);
	tri2.push_back(m_legs[4]);
	
	setReady();
	
	for(int i(0); i < 6; ++i)
	{
		moveListLegs(tri1, MIDDLE, TOP, CLOSE);
		moveListLegs(tri1, MIDDLE, BOTTOM, CLOSE);
		moveListLegs(tri2, MIDDLE, TOP, CLOSE);
		moveListLegs(tri2, MIDDLE, BOTTOM, CLOSE);
	}
	for (unsigned int l(0); l < m_legs.size(); ++l)
	{
		m_legs[l]->move(MIDDLE, BOTTOM, CLOSE);
	}
}

void Hexapod::setReady()
{
	for(unsigned int l(0); l < m_legs.size(); ++l)
	{
		m_legs[l]->move(MIDDLE, BOTTOM, CLOSE);
		milliSleep(100);
	}
}

void Hexapod::walk()
{
	/*m_legs[3]->move(LEFT, BOTTOM, FAR);
	milliSleep(1000);
	m_legs[3]->move(MIDDLE, BOTTOM, CLOSE);
	milliSleep(1000);*/
	setReady();
	std::vector<std::shared_ptr<Leg> > trilegsEven;
	std::vector<std::shared_ptr<Leg> > trilegsOdd;
	trilegsEven.push_back(m_legs[0]);
	trilegsEven.push_back(m_legs[2]);
	
	trilegsOdd.push_back(m_legs[1]);
	trilegsOdd.push_back(m_legs[5]);
	
	m_legs[4]->move(LEFT, TOP, CLOSE);
	moveListLegs(trilegsEven, RIGHT, TOP, CLOSE);
	for(int i(0); i < 10; ++i)
	{
		m_legs[4]->move(LEFT, BOTTOM, CLOSE);
		moveListLegs(trilegsEven, RIGHT, BOTTOM, CLOSE);
		
		m_legs[3]->move(LEFT, TOP, CLOSE);
		moveListLegs(trilegsOdd, RIGHT, TOP, CLOSE);
		m_legs[4]->move(MIDDLE, BOTTOM, CLOSE);
		moveListLegs(trilegsEven, MIDDLE, BOTTOM, CLOSE);
		m_legs[3]->move(LEFT, BOTTOM, CLOSE);
		moveListLegs(trilegsOdd, RIGHT, BOTTOM, CLOSE);
		m_legs[4]->move(LEFT, TOP, CLOSE);
		moveListLegs(trilegsEven, RIGHT, TOP, CLOSE);
		m_legs[3]->move(MIDDLE, BOTTOM, CLOSE);
		moveListLegs(trilegsOdd, MIDDLE, BOTTOM, CLOSE);
	}
		m_legs[4]->move(MIDDLE, BOTTOM, CLOSE);
		moveListLegs(trilegsEven, MIDDLE, BOTTOM, CLOSE);
		m_legs[3]->move(MIDDLE, BOTTOM, CLOSE);
		moveListLegs(trilegsOdd, MIDDLE, BOTTOM, CLOSE);
}


#endif
