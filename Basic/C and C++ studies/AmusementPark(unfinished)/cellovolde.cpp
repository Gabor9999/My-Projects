#include "vendeg.h"
#include "cellovolde.h"
#include <string>

void Cellovolde::Lo(Vendeg* v) {
    vendegek.push_back(v);
}

std::string Cellovolde::Legjobb() {
    std::string leg;
    int b = 0;
    if(vendegek.size() > 0){
        for(unsigned int i = 0; i < vendegek.size(); i++) {
            if(vendegek[i]->Eredmeny(this) > b) {
                b = vendegek[i]->Eredmeny(this);
                leg = vendegek[i]->getNev();
            }
    }
    }
    return leg;
}