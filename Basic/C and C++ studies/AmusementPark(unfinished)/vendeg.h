#pragma once
#include <string>
#include <vector>
#include "cellovolde.h"
#include "ajandek.h"

class Vendeg {
    protected:
        std::string nev;
        std::vector<Ajandek* a> nyeremenyek;
    public:
        Vendeg(const std::string nev) : nev(nev){};
        void Nyer(Ajandek* a);
        int Eredmeny(Cellovolde* c);
        ~Vendeg(){}
        std::string getNev() const {return nev;}
};