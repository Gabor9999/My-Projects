let ember = {
    name: "Gaga",
    age: 69,
};

console.log(ember.name + " is " + ember.age + " yo")

let mybutton = document.querySelector("#mybutton")
let spanom = document.querySelector("#spanom")
let kaga = () => {spanom.innerText++ };
mybutton.addEventListener("click", kaga)

let mybutton2 = document.querySelector("#mybutton2")
let myinput = document.querySelector("#mynumber")

mybutton2.addEventListener("click", () => {
    let darab = myinput.value;
    console.log(darab);
});

let mybutton3 = document.querySelector("#calc")
let radiusInput = document.querySelector("#radius")
let outputDiv = document.querySelector("#output")

mybutton3.addEventListener("click", () => {
    let r = radiusInput.value;
    let kerulet = 2 * r * Math.PI;
    outputDiv.innerText = kerulet;
})

let minus = document.querySelector("#minus")
let plus = document.querySelector("#plus")
let count = document.querySelector("#counter")
plus.addEventListener("click", () => {
    if (count.value == -10) {
        minus.disabled = false
    }
    count.value++;
    if (count.value == 10) {
        plus.disabled = true
    }
})
minus.addEventListener("click", () => {
    if (count.value == 10) {
        plus.disabled = false
    }
    count.value--;
    if (count.value == -10) {
        minus.disabled = true
    }
})