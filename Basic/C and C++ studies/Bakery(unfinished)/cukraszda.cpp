#include <vector>
#include "cukraszda.h"
#include "fagyizo.h"

void Cukraszda::Nyit(Fagyizo* u) {
    bool l = false;
    for(Fagyizo* f : uzletek){
        if (f == u) {
            l = true;
        }
    }
    if (l == false) {
    uzletek.push_back(u);}
}