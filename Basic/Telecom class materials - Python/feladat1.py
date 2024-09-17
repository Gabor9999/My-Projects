f = open("feladat1.txt")
def szokoev(szam):
    if szam%4 == 0:
        if szam%100 == 0:
            if szam%400 == 0:
                print(str(szam) + " Szökőév")
            else:
                print(str(szam) + " Nem szökőév")
        else:
           print(str(szam) + " Szökőév") 
    else:
        print(str(szam) + " Nem szökőév")

for x in f:
    szokoev(int(x))
f.close()