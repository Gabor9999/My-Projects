const kepek = [
  {
    cim: "balatoni_kikapcsolodas",
    rovidLeiras: "Kikapcsolódás a Balatonon",
    url: "http://vikorbogdan.hu/csaladi_kepeink/balatoni_kikapcsolodas.jpg",
  },
  {
    cim: "eloadas",
    rovidLeiras: "Pantomim előadás - Lőrinci Városi Könyvtár és Művelődési Ház",
    url: "http://vikorbogdan.hu/csaladi_kepeink/eloadas.jpg",
  },
  {
    cim: "portre",
    rovidLeiras: "Óvodai fotózás",
    url: "http://vikorbogdan.hu/csaladi_kepeink/portre.jpg",
  },
  {
    cim: "tarsasozas",
    rovidLeiras: "Társasozás pünkösd estéjén",
    url: "http://vikorbogdan.hu/csaladi_kepeink/tarsasozas.jpg",
  },
  {
    cim: "tengerparti_nyaralas",
    rovidLeiras: "Kiruccanás a tengerpartra - Gdansk, Lengyelország",
    url: "http://vikorbogdan.hu/csaladi_kepeink/tengerparti_nyaralas.jpeg",
  },
];

  
// a. Készíts a megadott adatstruktúra alapján (kepek) egy legördülő menüt ahol az opciókban megjelenő szöveg
// az adatok 'rovidLeiras' adattagjai, az opciók értéke pedig az adatok 'cim' adattagjai - 2 pont
// A létrehozott inputot add hozzá az gyerekként az "input-container" ID-jű divhez.
const inputContainer = document.querySelector("#input-container");
const imageContainer = document.querySelector("#image-container");
let item = document.createElement("select");
inputContainer.appendChild(item);

for (let i = 0; i < kepek.length; i++) {
  let elemek = document.createElement("option");

  elemek.text = kepek[i].rovidLeiras;
  elemek.value = kepek[i].cim;

  item.appendChild(elemek);
}

// b. Eseménykezelés segítségével oldd meg, hogy amikor *megváltozik* a <select> értéke,
// a legördülő menüben kiválasztott fotó jelenjen meg az imageContainer divben.
// Ehhez használd az 'url' adattagot a kiindulási adatokból.(kepek) - 2 pont

inputContainer.addEventListener("change", (e) => {
  for (let i = 0; i < kepek.length; i++) {
    if (e.target.item.elemek.text == kepek[i].rovidLeiras) {
      let image = document.createElement("img")
      image.src = kepek[i]
      imageContainer.appendChild(image)
    }
  }
})