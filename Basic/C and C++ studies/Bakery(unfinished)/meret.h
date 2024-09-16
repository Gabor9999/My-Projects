#pragma once
#include <string>

class Meret{
    protected:
        int suly;
    public:
        Meret Alakit(std::string s){}
        Meret(int suly) : suly(suly){}
};

class Kicsi : public Meret {
    private:
        static Kicsi* _instance;
    public:
        Kicsi(int suly=55) : Meret(suly) {}
        static Kicsi* instance();
        static Kicsi* destroy() {if(nullptr!=_instance) delete _instance;_instance=nullptr;}
};

class Kozepes : public Meret {
    private:
        static Kozepes* _instance;
    public:
        Kozepes(int suly=70) : Meret(suly) {}
        static Kozepes* instance();
        static Kozepes* destroy() {if(nullptr!=_instance) delete _instance;_instance=nullptr;}
};

class Nagy : public Meret {
    private:
        static Nagy* _instance;
    public:
        Nagy(int suly = 90) : Meret(suly) {}
        static Nagy* instance();
        static Nagy* destroy() {if(nullptr!=_instance) delete _instance;_instance=nullptr;}
};