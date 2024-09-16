#include <string>
#include <vector>
#include <iostream>
#include "ajandek.h"
#include "meret.h"
#include "cellovolde.h"
#include "vidampark.h"

int main() {
    Vidampark *park = new Vidampark();
    Cellovolde *vizagyu = new Cellovolde("Vizagyu");
    park->Add(vizagyu);
    

}