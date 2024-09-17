let table = document.querySelector("table");

// 2. feladat
table.addEventListener("click", (e)=>{
    if (e.target.matches("td")) {
        e.target.parentElement.classList.toggle("kivalasztott");
    }
});

// 1. feladat
let tbody = table.querySelector("tbody");
document.querySelector("button#hozzaad").addEventListener("click", (e)=>{
    let row = document.createElement("tr");
    let cellaszam = 1;

    let ures = false;

    document.querySelectorAll("input").forEach(adat => {
        if(adat.value == "") {
            ures = true
        }

        let cell = document.createElement("td");
        cell.innerHTML = adat.value;
        adat.value = "";
        if (cellaszam == 1) {
            cell.classList.add("nev");
        }
        else if(cellaszam == 2) {
            cell.classList.add("sikkosszeg");
        }
        else if(cellaszam == 3) {
            cell.classList.add("sikkszama");
        }
        row.append(cell);
        cellaszam++;
    });

    if(!ures) {
        tbody.append(row);
    }
});

// 3. feladat
let sum = 0;
let sumSpan = document.querySelector("#sum");
document.querySelector("button#lopas").addEventListener("click", (e)=>{
    document.querySelectorAll(".kivalasztott").forEach(adat => {
        let sikkosszeg = parseInt(adat.querySelector(".sikkosszeg").innerHTML);
        sum += sikkosszeg;

        // 4. feladat
        adat.querySelector(".sikkszama").innerHTML = parseInt(adat.querySelector(".sikkszama").innerHTML)+1;
    });

    sumSpan.innerHTML = sum;
    document.querySelectorAll("td").forEach(adat => adat.parentElement.classList.remove("kivalasztott")); // levesszük az összes kiválasztst miután a LOPÁS gombot megnyomtuk
});

// 5. feladat
let sikkasztok = table.querySelectorAll(".sikkosszeg");
let tizmilliofelett = 0;
sikkasztok.forEach(e => {
    let sikkosszeg = parseInt(e.innerHTML);

    if(sikkosszeg > 10000000) {
        tizmilliofelett++;
    }
});
document.querySelector("#tizmillio").innerHTML = tizmilliofelett;

// 6. feladat
let max = 0;
let maxid = 0;
let i = 0;
table.querySelectorAll(".sikkosszeg").forEach(e => {
    let osszeg = parseInt(e.innerHTML);
    if(max < osszeg) {
        max = osszeg;
        maxid = i;
    }
    i++;
});

let nevek = [];
table.querySelectorAll(".nev").forEach(e => {
    nevek.push(e.innerHTML);
});
document.querySelector("#leggazdagabb").innerHTML = nevek[maxid];