const container = document.querySelector('#container')
const color = document.querySelector('.color')

function delegal(szulo, gyerek, mikor, mit) {
    function esemenyKezelo(esemeny) {
      let esemenyCelja = esemeny.target;
      let esemenyKezeloje = this;
      let legkozelebbiKeresettElem = esemenyCelja.closest(gyerek);
  
      if (esemenyKezeloje.contains(legkozelebbiKeresettElem)) {
        mit(esemeny, legkozelebbiKeresettElem);
      }
    }
  
    szulo.addEventListener(mikor, esemenyKezelo);
  }

function generateTable(){   

    for(let i=0; i<20; i++){
        let newRow = document.createElement('tr');
        container.appendChild(newRow);

        for(let j=0; j<20; j++){
            let newCell = document.createElement('td');
            newCell.classList.add("cell");
            newRow.appendChild(newCell);
        }
    }
}

generateTable();

delegal(container, "#container td", "click", (e) => {
   e.target.style.backgroundColor = color.value;
})


