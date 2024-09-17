const numbers = [5, 2, 15, -3, 6, -8, -2];

const matrix = [
  [1, 0, 3],
  [0, 2, 0],
  [4, 5, 6],
  [0, 0, 0],
]

const searchResults = {
  "Search": [
    {
      "Title": "The Hobbit: An Unexpected Journey",
      "Year": "2012",
      "imdbID": "tt0903624",
      "Type": "movie"
    },
    {
      "Title": "The Hobbit: The Desolation of Smaug",
      "Year": "2013",
      "imdbID": "tt1170358",
      "Type": "movie"
    },
    {
      "Title": "The Hobbit: The Battle of the Five Armies",
      "Year": "2014",
      "imdbID": "tt2310332",
      "Type": "movie"
    },
    {
      "Title": "The Hobbit",
      "Year": "1977",
      "imdbID": "tt0077687",
      "Type": "movie"
    },
    {
      "Title": "Lego the Hobbit: The Video Game",
      "Year": "2014",
      "imdbID": "tt3584562",
      "Type": "game"
    },
    {
      "Title": "The Hobbit",
      "Year": "1966",
      "imdbID": "tt1686804",
      "Type": "movie"
    },
    {
      "Title": "The Hobbit",
      "Year": "2003",
      "imdbID": "tt0395578",
      "Type": "game"
    },
    {
      "Title": "A Day in the Life of a Hobbit",
      "Year": "2002",
      "imdbID": "tt0473467",
      "Type": "movie"
    },
    {
      "Title": "The Hobbit: An Unexpected Journey - The Company of Thorin",
      "Year": "2013",
      "imdbID": "tt3345514",
      "Type": "movie"
    },
    {
      "Title": "The Hobbit: The Swedolation of Smaug",
      "Year": "2014",
      "imdbID": "tt4171362",
      "Type": "movie"
    }
  ],
  "totalResults": "51",
  "Response": "True",
}

//a
let numbers2 = [];
/*for (let i = 0; i < numbers.length; i++) {
    numbers2[i] = numbers[i] * numbers[i]
}*/

numbers.forEach(element => numbers2.push(element*element))
console.log(numbers2)

//b
let min = numbers[0];
for (let i = 1; i < numbers.length; i++) {
    if (numbers[i] < min) {
        min = numbers[i]
    }
}
console.log(min)

//c
let b = 0;
let result = -1;
for (let i = 0; i < matrix.length; i++) {
    for (let j = 0; j < matrix[i].length; j++) {
        if (matrix[i][j] != 0) {
            break
        }
        else if (j == matrix[i].length-1) {
            result = b;
        }
    }
    b++;
}
console.log(result)

//d
let results = []
for (let k = 0; k < searchResults.Search.length; k++) {
    if (searchResults.Search[k].Type == "movie" && searchResults.Search[k].Year > 2010) {
        results.push(searchResults.Search[k].imdbID)
    }
}
console.log(results)

//2a
let code = document.querySelector("#valami")
let gombocska = document.querySelector("#butt")
let hue = document.querySelector("#h")
let sat = document.querySelector("#s")
let light = document.querySelector("#l")

gombocska.addEventListener("click", () => {
  code.value = hue.value + " " + sat.value + " " + light.value
  //2b
  document.body.style.backgroundColor = `hsl(${hue.value}, ${sat.value}% , ${light.value}%)`
})

//2c
sat.addEventListener('input', () => {
  document.body.style.backgroundColor = `hsl(${hue.value}, ${sat.value}% , ${light.value}%)`
})

hue.addEventListener('input', () => {
  document.body.style.backgroundColor = `hsl(${hue.value}, ${sat.value}% , ${light.value}%)`
})

light.addEventListener('input', () => {
  document.body.style.backgroundColor = `hsl(${hue.value}, ${sat.value}% , ${light.value}%)`
})

//3a

let contact = document.querySelector("#contacts")
contact.addEventListener("click", (e) => {
  if (e.target.matches(".button")){
    console.log(e.target.innerText)
    document.querySelectorAll(".email").classlist.toggle
  }
})