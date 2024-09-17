// 1. feladat - AZ OLDALON MINDEN OLYAN HIVATKOZÁST TILTSUNK LE, AMELYIK NEM ELTÉS CÍMRE MUTAT!

const links = document.querySelectorAll("a");

links.forEach(e => e.href.includes("elte.hu") ? "" : e.classList.add("inaktiv"));

// 2. feladat - OLDJUK MEG, HOGY A CSÚSZKÁK SEGÍTSÉGÉVEL VÁLTOZZON A DIV MÉRETE!
const t2div = document.querySelector("#t2div");

const hinput = document.querySelector("#height");
const winput = document.querySelector("#width");

t2div.style.height = hinput.value + "px";
t2div.style.width = winput.value + "px";

/* hinput.addEventListener("input", ()=> { // change nem jó eventre, mert csak elengedés után változik majd, ezért inputot használuk, ami húzás közben is követi
    t2div.style.height = hinput.value + "px";
}); */

hinput.addEventListener("input", ()=> { // inputot használuk, ami húzás közben is folyamatosan változtatja a méretet
    t2div.style.height = hinput.value + "px";
});

winput.addEventListener("input", ()=> {
    t2div.style.width = winput.value + "px";
});

// 3. feladat - TO-DO-LIST

//includes - stringben keresünk szöveget
//contains - tömbszerű dologban keresünk elemet

document.querySelector("ul").addEventListener("click", (e)=> {
    if (e.target.matches("li")) {
        /*if(e.target.classList.contains("done")) {
            e.target.classList.remove("done");
        }
        else {
            e.target.classList.add("done");
        }*/

        e.target.classList.toggle("done");
    }
});

// 4. feladat - COLORPIXEL LEBUTÍTVA

// a) Javascripttel készítsünk egy 10x10-es táblázatot 10x10-es cellákkal a "#table" div-be.
let rows = 10;
let cols = 10;
let div = document.querySelector("#table");
let table = document.createElement("table");

for (let i = 0; i < rows; i++) {
    let row = document.createElement("tr");
    for (let j = 0; j < cols; j++) {
        let cell = document.createElement("td");
        cell.style.height = "10px";
        cell.style.width = "10px";
        row.append(cell);
    }
    table.append(row);
}
div.append(table);

// b) A cellákra kattintáskor a color inputból bekért színnel töltse ki a cellát.
// c is benne van
document.querySelector("#table").addEventListener("click", (e)=>{
    if (e.target.matches("td")) {   
        let color = document.querySelector("#cinput").value;

        if(e.altKey) {
            e.target.parentElement.parentElement.querySelectorAll("td").forEach(e => e.style.backgroundColor = color);
        }
        else {
            e.target.style.backgroundColor = color;
        }
    }
});

// Tömbből 1 random kiválasztása
let tomb = ["a", "b", "c", "d", "e"];
let random_max = tomb.length;
let random = Math.floor(Math.random() * random_max);
let random_elem = tomb[random];

// Akasztófa

// Gyűjtsétek azokat, amire már tippeltetek
// Vizuálisan megjelenítve a hibák száma képpel, svgvel, bármivel
// Hibák száma szöveggel is látszódjon
// 5 random szó közül válasszon az oldal betöltésekor 1 szót