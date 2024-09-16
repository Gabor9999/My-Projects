#pragma once
#include <string>
#include <vector>
#include "ajandek.h"
#include "vendeg.h"

class Cellovolde {
    protected:
        std::string helyszin;
        std::vector<std::string> vendegek;
    public:
        Cellovolde(const std::string helyszin): helyszin(helyszin){}
        std::string getHely() {return helyszin;}
        std::string Legjobb();
        void Lo(Vendeg* v);
        void Kiallit(Ajandek* a) {a->cellovolde = this}
        ~Cellovolde(){}
};