#ifndef UTILITY_H
#define UTILITY_H
 
#include <string>
#include <sstream>
#include <unistd.h>

template<typename T>
std::string toString(T x)
{
    std::ostringstream oss;
    oss << x;
    return oss.str();
}

void milliSleep(int sec)
{
	usleep(sec * 1000);
}

#endif
