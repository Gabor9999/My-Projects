#pragma once
#include <vector>
#include "fagyizo.h"

class Cukraszda {
    protected:
        std::vector<Fagyizo*> uzletek;
    public:
        void Nyit(Fagyizo* u);
        Cukraszda(){}
        ~Cukraszda(){}
};