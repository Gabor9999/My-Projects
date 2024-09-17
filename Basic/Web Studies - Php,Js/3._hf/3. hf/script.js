let tomb = ["alma", "körte", "banán", "cseresznye", "dió"];
let random_max = tomb.length;
let random = Math.floor(Math.random() * random_max);
let szo = tomb[random];

let allas = document.querySelector("#allas");
let tippelt = document.querySelector("#tippelt");
let hibak = document.querySelector("#hibak");
let hibak_num = 0;
let aktualis = document.querySelector("#aktualis");
let gomb = document.querySelector("button");

for (let i = 0; i < szo.length; i++) {
    allas.innerHTML += "_ ";
}

let tippek = [];

gomb.addEventListener("click", ()=>{
    let aktualis_tipp = aktualis.value;
    if(isNaN(aktualis_tipp)) {
        if(!tippek.includes(aktualis_tipp)) {
            tippelt.innerHTML += aktualis_tipp+" ";
            tippek.push(aktualis_tipp);

            if(szo.includes(aktualis_tipp)) {
                allas.innerHTML = "";
                for (let j = 0; j < szo.length; j++) {
                    if(tippek.includes(szo[j])) {
                        allas.innerHTML += szo[j]+" ";
                    }
                    else {
                        allas.innerHTML += "_ ";
                    }
                }
            }
            else {
                hibak_num++;
                hibak.innerHTML = hibak_num;
            }
        }
        aktualis.value = "";
    }
    else {
        aktualis.value = "";
    }
})