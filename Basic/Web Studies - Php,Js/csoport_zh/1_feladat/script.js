// Egy adott időszakban minden nap megmértük a Balaton vizének hőmérsékletét,
// és egy sorozatban tároltuk el.

// A válaszokat írd ki a konzolra!

let homersekletek = [18, 20, 23, 26, 30, 28, 25, 20, 20, 23, 24];

// a. Hányszor mértünk 25 Celsius fok feletti hőmérsékletet? - 0,5 pont

let hom2 = homersekletek.filter(element => element > 25)
console.log(hom2.length)

// b. Hanyadik napon volt a víz először pontosan 23 fokos? - 0,5 pont

console.log((homersekletek.findIndex(element => element == 23)) + 1 + ".napon volt")

// c. Melyik volt a legnagyobb mért hőmérséklet-érték? - 1 pont
let max = homersekletek[0]
for (let i = 1; i < homersekletek.length; i++) {
    if (homersekletek[i] > max) {
        max = homersekletek[i]
    }
}
console.log(max)

// d. Hányadik napon volt a leghidegebb a víz? - 1 pont

let minInd = 0
let minVal = homersekletek[0]
for (let i = 1; i < homersekletek.length; i++) {
    if (homersekletek[i] < minVal) {
        minVal = homersekletek[i]
        minInd = i
    }
}
console.log(minInd + 1 + ".napon volt a leghidegebb a víz")