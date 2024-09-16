#include "meret.h"

Kicsi *Kicsi::_instance=nullptr;
Kicsi *Kicsi::instance(){
    if(_instance==nullptr) {
        _instance = new Kicsi(55);
    }
    return _instance;
}
Kozepes *Kozepes::_instance=nullptr;
Kozepes *Kozepes::instance(){
    if(_instance==nullptr) {
        _instance = new Kozepes(70);
    }
    return _instance;
}
Nagy *Nagy::_instance=nullptr;
Nagy *Nagy::instance(){
    if(_instance==nullptr) {
        _instance = new Nagy(90);
    }
    return _instance;
}