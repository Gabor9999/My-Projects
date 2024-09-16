#pragma once
#include <vector>
#include "iz.h"

class Kehely{
    protected:
        std::vector<Iz*> gomboc;
        Meret* meret;
    public:
        Kehely(Meret* m, std::vector<Iz*> i) {if (i.size()>2 && i.size()<6){gomboc=i;meret=m;}}
        int Suly();
        bool Epres();

};