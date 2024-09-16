#include "vendeg.h"
#include "cellovolde.h"
#include "ajandek.h"
#include <string>

using namespace std;

void Vendeg::Nyer(Ajandek* a) {
    nyeremenyek.push_back(a);
}

int Vendeg::Eredmeny(Cellovolde* c) {
    int o = 0;
    for (Ajandek* a : nyeremenyek) {
        if (a->cellovolde == c) {
            o += a->Ertek();
        }
    }
    return o;
}