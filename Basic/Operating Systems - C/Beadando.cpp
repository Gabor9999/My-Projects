#include <iostream>
#include <fstream>
#include <string>
#include <vector>

using namespace std;

// Adatok struktúrája
struct Applicant {
    string name;
    vector<string> daysAvailable;
};

// Fájlból olvasás
void readDataFromFile(vector<Applicant>& applicants) {
    ifstream infile("applicants.txt");
    string name, days;
    while (infile >> name >> days) {
        Applicant applicant;
        applicant.name = name;
        string day = "";
        for (int i = 0; i < days.length(); i++) {
            if (days[i] == ' ') {
                applicant.daysAvailable.push_back(day);
                day = "";
            } else {
                day += days[i];
            }
        }
        if (day != "") {
            applicant.daysAvailable.push_back(day);
        }
        applicants.push_back(applicant);
    }
}

// Jelentkezők adatainak kiírása
void printApplicants(vector<Applicant>& applicants) {
    cout << "Jelentkezők adatai:" << endl;
    for (int i = 0; i < applicants.size(); i++) {
        cout << "Név: " << applicants[i].name << endl;
        cout << "Elérhető napok: ";
        for (int j = 0; j < applicants[i].daysAvailable.size(); j++) {
            cout << applicants[i].daysAvailable[j] << " ";
        }
        cout << endl;
    }
}

// Jelentkezők hozzáadása
void addApplicant(vector<Applicant>& applicants) {
    Applicant applicant;
    cout << "Add meg a neved: ";
    cin >> applicant.name;
    cout << "Add meg az elérhető napjaidat (pl.: hetfo szerda csutortok): ";
    string days;
    getline(cin >> ws, days);
    string day = "";
    for (int i = 0; i < days.length(); i++) {
        if (days[i] == ' ') {
            applicant.daysAvailable.push_back(day);
            day = "";
        } else {
            day += days[i];
        }
    }
    if (day != "") {
        applicant.daysAvailable.push_back(day);
    }
    applicants.push_back(applicant);
}

// Jelentkezők törlése
void deleteApplicant(vector<Applicant>& applicants) {
    string name;
    cout << "Add meg a jelentkező nevét, akinek az adatait törölni szeretnéd: ";
    cin >> name;
    bool found = false;
    for (int i = 0; i < applicants.size(); i++) {
        if (applicants[i].name == name) {
            applicants.erase(applicants.begin() + i);
            found = true;
            break;
        }
    }
    if (found) {
        cout << "A jelentkező adatai sikeresen törölve lettek." << endl;
    } else {
        cout << "A megadott névvel nem található jelentkező." << endl;
    }
}