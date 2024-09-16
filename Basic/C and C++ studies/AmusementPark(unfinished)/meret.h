#pragma once

class Meret{
    public:
        virtual int Szorzo() {return 0;}
        Meret(){}
        virtual ~Meret(){}
};

class S : public Meret {
    private:
        static S* _instance;
    public:
        static S* instance();
        S() : Meret(){}
        int Szorzo() override {return 1;}
};

class M : public Meret {
    private:
        static M* _instance;
    public:
        static M* instance();
        M(): Meret(){}
        int Szorzo() override {return 2;}
};

class L : public Meret {
    private:
        static L* _instance;
    public:
        static L* instance();
        L(): Meret(){}
        int Szorzo() override {return 3;}
};

class XL : public Meret {
    private:
        static XL* _instance;
    public:
        static XL* instance();
        XL(): Meret(){}
        int Szorzo() override {return 4;}
};