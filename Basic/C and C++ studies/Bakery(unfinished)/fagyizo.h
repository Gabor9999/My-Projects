#pragma once
#include <string>
#include <vector>
#include "fagyizo.h"
#include "meret.h"
#include "rendeles.h"
#include "kivansag.h"

class Fagyizo {
    protected:
        std::string nev;
        std::string cim;
        int ar;
        std::vector<Rendeles*> napi;
    public:
        Fagyizo(const std::string nev,const std::string cim, int ar) : nev(nev), cim(cim), ar(ar) {}
        void Felvesz(std::vector<Kivansag*>,std::string t);
        int Hany(Meret* m);
        int Bevetel();
};