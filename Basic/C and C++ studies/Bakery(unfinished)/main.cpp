#include <iostream>
#include "fagyizo.h"
#include "cukraszda.h"
#include "meret.h"
#include "iz.h"
#include "rendeles.h"
#include "kehely.h"
#include "kivansag.h"

using namespace std;

int main()
{
    Cukraszda* kft = new Cukraszda();
    Fagyizo* b1=new Fagyizo("a1","c1",200);
    Fagyizo* b2=new Fagyizo("a2","c2",240);
    Fagyizo* b3=new Fagyizo("a3","c3",280);
    Fagyizo* ptr;
    kft->Nyit(b1);
    kft->Nyit(b2);
    kft->Nyit(b3);
    try
    {
        Fagyizo* b4=new Fagyizo("a3","c4",220);
        ptr=b4;
        kft->Nyit(b4);
    }
    catch (...)
    {
        cout << "Letezo uzlet - Helyesen kezelve" << endl;
        delete ptr;
    }
    try
    {
        Fagyizo* b4=new Fagyizo("a4","c3",210);
        ptr=b4;
        kft->Nyit(b4);
    }
    catch (...)
    {
        cout << "Letezo uzlet - Helyesen kezelve" << endl;
        delete ptr;
    }

    /*cout << kft->MindenholVolt() << endl;
    cout << b1->Mennyit(v1) << endl;
    cout << b1->Hany() << endl;*/


    Kicsi::destroy();
    Kozepes::destroy();
    Nagy::destroy();

    return 0;
}
