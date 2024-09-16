#pragma once
#include <string>
#include "meret.h"
#include "cellovolde.h"

class Ajandek {
    protected:
        Meret* meret;
        Cellovolde* cellovolde;
    public:
        Ajandek(Meret* m) : meret(m){}
        virtual ~Ajandek(){}
        virtual int Pont() {return 0;}
        virtual int Ertek() {return Pont()* meret->Szorzo();}

};

class Labda : public Ajandek {
    public:
        Labda(Meret* m) : Ajandek(m){}
        int Pont() override {return 1;}
};

class Figura : public Ajandek {
    public:
        Figura(Meret* m) : Ajandek(m){}
        int Pont() override {return 2;}
};

class Pluss : public Ajandek {
    public:
        Pluss(Meret* m) : Ajandek(m){}
        int Pont() override {return 3;}
};