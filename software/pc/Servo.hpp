#ifndef SERVO_H
#define SERVO_H

#include <stdio.h>

#include "serialib.h"
#include "utility.hpp"

class Servo
{
	public:
		Servo(std::string ID, serialib& serial);
		~Servo();
		void disable();
		void move(int pulse);
		void setReady();
	
		int m_currentPule;
		
	private:
		const int m_maxPulse;
		const int m_minPulse;
		const std::string m_ID;
		int m_speed;
		serialib& m_serial;
};

Servo::Servo(std::string ID, serialib& serial):
	m_currentPule(1500),
	m_maxPulse(2000),
	m_minPulse(1100),
	m_ID(ID),
	m_speed(500),
	m_serial(serial)
{
	
}

Servo::~Servo()
{
	
}

void Servo::disable()
{
	std::string message = "#" + m_ID + " L\r";
	m_serial.WriteString((const char*)message.c_str());
}

void Servo::move(int pulse)
{
	if(pulse > m_maxPulse)
		pulse = m_maxPulse;
	if(pulse < m_minPulse)
		pulse = m_minPulse;
	std::string message = "#" + m_ID + " P" + toString(pulse) + "\r";
	std::cout << message << std::endl;
	m_serial.WriteString((const char*)message.c_str());
}

void Servo::setReady()
{
	std::string message = "#" + m_ID + " P" + toString(m_currentPule) + "\r";
	m_serial.WriteString((const char*)message.c_str());
}

#endif
