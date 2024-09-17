const art = document.querySelector("#art");
let table = document.createElement('table');
let draw = false;
for (let i = 0; i < 20; i++) {
    let row = document.createElement('tr');
    for (let j = 0; j < 20; j++) {
        let td = document.createElement('td')
        row.appendChild(td)
  }
  table.appendChild(row);
}

art.appendChild(table)  
let colour = document.createElement('input')
colour.type = "color"
art.appendChild(colour)

function delegal(szulo, gyerek, mikor, mit){
    function esemenyKezelo(esemeny){
        let esemenyCelja    = esemeny.target;
        let esemenyKezeloje = this;
        let legkozelebbiKeresettElem = esemenyCelja.closest(gyerek);

        if(esemenyKezeloje.contains(legkozelebbiKeresettElem)){
            mit(esemeny, legkozelebbiKeresettElem);
        }
    }
    szulo.addEventListener(mikor, esemenyKezelo);
}

delegal(art,"#art td","click", (e) => {
    e.target.style.backgroundColor = colour.value;
})

/*table.addEventListener('click', (e) => {
    if(e.target.nodeName == "TD") {
        e.target.style.backgroundColor = colour.value;
    }
})*/
