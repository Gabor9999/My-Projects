#include "meret.h"

S *S::_instance = nullptr;
S *S::instance() {
    if(nullptr == _instance){
        _instance = new S();
    }
    return _instance;
}

M *M::_instance = nullptr;
M *M::instance() {
    if(nullptr == _instance){
        _instance = new M();
    }
    return _instance;
}

L *L::_instance = nullptr;
L *L::instance() {
    if(nullptr == _instance){
        _instance = new L();
    }
    return _instance;
}

XL *XL::_instance = nullptr;
XL *XL::instance() {
    if(nullptr == _instance){
        _instance = new XL();
    }
    return _instance;
}