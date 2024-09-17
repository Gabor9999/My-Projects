#ifndef INDAPPER_H
#define INDAPPER_H

#include <vector>
#include <iterator>
#include <iostream>

template<class T, typename type = typename T::value_type >
class index_appender_view {
    private:
        std::vector<T*> data;
    public:
        void add(T& cont){
            data.push_back(&cont);
        }

        int size() const {
            int size = 0;
            for (long unsigned int i = 0; i < data.size();i++) {
                size += data[i]->size();
            }
            return size;
        }

        type& at(const int a){
            int pos = a;
            for (long unsigned int i = 0; i < data.size();i++) {
                for (typename T::iterator it = data[i]->begin(); it != data[i]->end();++it){
                    if (pos == 0) {
                        return *it;
                    }
                    else {
                        pos--;
                    }
                }
            }
            data[data.size()-1]->resize(data[data.size()-1]->size()+pos);
            //return data[data.size()-1]->at((data[data.size()-1]->size())-1);
            return data[data.size()-1]->back();
        }

        type& at(const int a) const{
            int pos = a;
            for (long unsigned int i = 0; i < data.size();i++) {
                for (typename T::iterator it = data[i]->begin(); it != data[i]->end();++it){
                    if (pos == 0) {
                        return *it;
                    }
                    else {
                        pos--;
                    }
                }
            }
            data[data.size()-1]->resize(data[data.size()-1]->size()+pos);
            //return data[data.size()-1]->at((data[data.size()-1]->size())-1);
            return data[data.size()-1]->back();
        }

        type& operator[](const int a) {
            int pos = a;
            for (long unsigned int i = 0; i < data.size();i++) {
                for (typename T::iterator it = data[i]->begin(); it != data[i]->end();++it){
                    if (pos == 0) {
                        return *it;
                    }
                    else {
                        pos--;
                    }
                }
            }
            data[data.size()-1]->resize(data[data.size()-1]->size()+pos);
            //return data[data.size()-1]->at((data[data.size()-1]->size())-1);
            return data[data.size()-1]->back();
        }

        type& operator[](const int a) const{
            int pos = a;
            for (long unsigned int i = 0; i < data.size();i++) {
                for (typename T::iterator it = data[i]->begin(); it != data[i]->end();++it){
                    if (pos == 0) {
                        return *it;
                    }
                    else {
                        pos--;
                    }
                }
            }
            data[data.size()-1]->resize(data[data.size()-1]->size()+pos);
            //return data[data.size()-1]->at((data[data.size()-1]->size())-1);
            return data[data.size()-1]->back();
        }
};

template<>
class index_appender_view<std::vector<bool>,bool> {
    private:
        std::vector<std::vector<bool>*> data;
    public:
        void add(std::vector<bool>& cont){
            data.push_back(&cont);
        }

        int size() const {
            int size = 0;
            for (long unsigned int i = 0; i < data.size();i++) {
                size += data[i]->size();
            }
            return size;
        }

        std::vector<bool>::reference at(const int a) {
            int pos = a;
            for (long unsigned int i = 0; i < data.size();i++) {
                for (typename std::vector<bool>::iterator it = data[i]->begin(); it != data[i]->end();++it){
                    if (pos == 0) {
                        return *it;
                    }
                    else {
                        pos--;
                    }
                }
            }
            data[data.size()-1]->resize(data[data.size()-1]->size()+pos);
            return data[data.size()-1]->back();
        }
        //Amúgy soha nem használjuk a mainben
        std::vector<bool>::reference at(const int a) const {
            int pos = a;
            for (long unsigned int i = 0; i < data.size();i++) {
                for (typename std::vector<bool>::iterator it = data[i]->begin(); it != data[i]->end();++it){
                    if (pos == 0) {
                        return *it;
                    }
                    else {
                        pos--;
                    }
                }
            }
            data[data.size()-1]->resize(data[data.size()-1]->size()+pos);
            return data[data.size()-1]->back();
        }

        std::vector<bool>::reference operator[](const int a) {
            int pos = a;
            for (long unsigned int i = 0; i < data.size();i++) {
                for (typename std::vector<bool>::iterator it = data[i]->begin(); it != data[i]->end();++it){
                    if (pos == 0) {
                        return *it;
                    }
                    else {
                        pos--;
                    }
                }
            }
            data[data.size()-1]->resize(data[data.size()-1]->size()+pos);
            return data[data.size()-1]->back();
        }
        //Szintén
        std::vector<bool>::reference operator[](const int a) const{
            int pos = a;
            for (long unsigned int i = 0; i < data.size();i++) {
                for (typename std::vector<bool>::iterator it = data[i]->begin(); it != data[i]->end();++it){
                    if (pos == 0) {
                        return *it;
                    }
                    else {
                        pos--;
                    }
                }
            }
            data[data.size()-1]->resize(data[data.size()-1]->size()+pos);
            return data[data.size()-1]->back();
        }
};

#endif