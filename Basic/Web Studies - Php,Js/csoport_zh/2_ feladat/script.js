// Delegálás segítségével érd el, hogy egy betűre kattintva azon alkalmazva legyen a 'neonText' stílusosztály
// majd újabb kattintásra lekerüljön róla. - 2 pont

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

let divCon = document.querySelector("#container")
delegal(divCon,"span","click", (e) => {
    e.target.classList.toggle("neonText")
})