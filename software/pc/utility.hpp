#ifndef UTILITY_H
#define UTILITY_H
 
#include <string>
#include <sstream>

template<typename T>
std::string toString(T x)
{
    std::ostringstream oss;
    oss << x;
    return oss.str();
}

#endif
