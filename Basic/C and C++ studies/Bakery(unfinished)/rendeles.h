#pragma once
#include <string>
#include <vector>
#include "kehely.h"
#include "meret.h"

class Rendeles {
    protected:
        std::string idopont;
        std::vector<Kehely*> kelyhek;
    public:
        Rendeles(const std::string idopont) : idopont(idopont){}
        void Hozzavesz(Kehely* k) {kelyhek.push_back(k);}
        int Suly();
        int Darab(Meret* m);

};