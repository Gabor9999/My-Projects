#pragma once
#include <string>
#include <vector>
#include "cellovolde.h"

class Vidampark {
    protected:
        std::vector<Cellovolde*> cellovoldek;
    public:
        void Add(Cellovolde* c);
        Vidampark(){}
        ~Vidampark(){}
};