const container = document.querySelector('#container')
let maxV = 0
let name1 = document.querySelector('#name')
let timer = document.querySelector("#time")
let timetex = document.querySelector("#timetext")
let save = document.querySelector('#Ment')
let s
let timeOn = false
let modify = true
let timechange;
let menu = document.querySelector(".menu")
let jatek = document.querySelector("#startgame")
let leiras = document.querySelector("#description")
let szoveg = document.querySelector("#destext")
let buttons = document.querySelector("#buttons")
let back = document.querySelector("#back")
let backbutton = document.querySelector("#backbutton")
let pname = document.querySelector("#playername")
let resetB = document.querySelector("#reset")
let megoldas = document.querySelector("#megoldas")
let level1 = document.querySelector("#Level1")
let level2 = document.querySelector("#Level2")
let level3 = document.querySelector("#Level3")
let results = document.querySelector("#results")
let table = document.createElement('table')
let SaveB = document.querySelector("#savegame")
let folytGomb = document.querySelector("#continue")
let folytValue = 0

let prevGame
if (localStorage.key("Playername") != null) {
    name1.value = window.localStorage.getItem("Playername")
}
save.addEventListener("click", (e) => {
    window.localStorage.setItem("Playername", name1.value)
})
function getPrevResults() {
    for(let i = 0;i < window.localStorage.getItem("Results_num");i++) {
        let newLi = document.createElement("li")
        newLi.innerText = window.localStorage.getItem("Results_" + i)
        results.appendChild(newLi)
    }
}
getPrevResults()
function time() {
    s = 0
    timechange = setInterval(() => {
        if (timeOn) {
        s++
        if (s%60 < 10) { timetex.value = Math.floor(s/60) + ":" + "0" + s%60;}
        else {timetex.value = Math.floor(s/60) + ":" + s%60;}}
    },1000);
}
function fg_displays() {
    buttons.style.display = "none"
    pname.style.display = "none"
    megoldas.style.display= "block"
    timer.style.display = "block"
    backbutton.style.display = "block"
    resetB.style.display = "block"
    results.style.display = "none"
    SaveB.style.display = "block"
    time()
}
function init_displays() {
    timer.style.display = "none"
    backbutton.style.display = "none"
    megoldas.style.display= "none"
    buttons.style.display = "none"
    szoveg.style.display = "none"
    resetB.style.display = "none"
    SaveB.style.display = "none"
    menu.style.height = '200px'
    if (window.localStorage.getItem("Continue") != null) {
        folytValue = window.localStorage.getItem("Continue")
    }
    if (folytValue == 0) {
        folytGomb.style.display = "none"
    }
    else {
        folytGomb.style.display = "block"
    }
}
init_displays()

function Level1(){
    maxV = 7
    for (let i=0; i<maxV; i++){
        let newRow = document.createElement('tr');
        newRow.value = i
        table.appendChild(newRow)
        for(let j=0; j<maxV; j++){
        let newCell = document.createElement('td');
        newCell.value = j
        if ((j == 1 && i == 5) || (i == 3 && (j == 0 || j == 3 || j == 6))) {
            newCell.style.backgroundColor = 'black'
            newCell.innerText = ""
            newCell.value = "-100"
        }
        else if (i == 1 && j == 1) {
            newCell.style.backgroundColor = 'black'
            newCell.innerText = "0"
            newCell.value = "0"
            newCell.style.color = 'lime'
        }
        else if (i == 0 && j == 3) {
            newCell.style.backgroundColor = 'black'
            newCell.innerText = "1"
            newCell.value = "1"
            newCell.style.color = 'white'
        }
        else if (j == 5 && (i == 1 || i == 5)) {
            newCell.style.backgroundColor = 'black'
            newCell.innerText = "2"
            newCell.value = "2"
            newCell.style.color = 'white'
        }
        else if (i == 6 && j == 3) {
            newCell.style.backgroundColor = 'black'
            newCell.innerText = "3"
            newCell.value = "3"
            newCell.style.color = 'white'
        }
        else {newCell.style.backgroundColor = 'white'}
        newRow.appendChild(newCell);
        }
    }
container.appendChild(table);
timeOn = true
}

function Level2(){
    maxV = 7;
    for (let i=0; i<maxV; i++){
        let newRow = document.createElement('tr');
        newRow.value = i
        table.appendChild(newRow)
        for(let j=0; j<maxV; j++){
        let newCell = document.createElement('td');
        newCell.value = j
        if ((j == 4 && i == 0) || (i == 2 && (j == 0 || j == 2 || j == 6)) || (i == 4 && (j == 4 || j == 2 || j == 6)) || (j == 2 && i == 6)) {
            newCell.style.backgroundColor = 'black'
            newCell.innerText = ""
            newCell.value = "-100"
        }
        else if (i == 0 && j == 2) {
            newCell.style.backgroundColor = 'black'
            newCell.innerText = "0"
            newCell.value = "0"
            newCell.style.color = 'lime'
        }
        else if (i == 3 && j == 3) {
            newCell.style.backgroundColor = 'black'
            newCell.innerText = "1"
            newCell.value = "1"
            newCell.style.color = 'white'
        }
        else if ((j == 0 && i == 4) || (j == 4 && i == 6)) {
            newCell.style.backgroundColor = 'black'
            newCell.innerText = "2"
            newCell.value = "2"
            newCell.style.color = 'white'
        }
        else if (i == 2 && j == 4) {
            newCell.style.backgroundColor = 'black'
            newCell.innerText = "3"
            newCell.value = "3"
            newCell.style.color = 'white'
        }
        else {newCell.style.backgroundColor = 'white'}
        newRow.appendChild(newCell);
        }
    }
container.appendChild(table);
timeOn = true
}

function Level3(){
    maxV = 10
    for (let i=0; i<maxV; i++){
        let newRow = document.createElement('tr');
        newRow.value = i
        table.appendChild(newRow)
        for(let j=0; j<maxV; j++){
        let newCell = document.createElement('td');
        newCell.value = j
        if ((j == 1 && i == 0) || (i == 2 && (j == 2 || j == 7)) || (j == 9 && i == 1) || (j == 4 && i == 3) || (j == 2 && i == 8) || (j == 8 && i == 7) || (i == 4 && (j == 4 || j == 6)) || 
        (i == 5 && (j == 3 || j == 4 || j == 5)) || (j == 5 && i == 6)) {
            newCell.style.backgroundColor = 'black'
            newCell.innerText = ""
            newCell.value = "-100"
        }
        else if ((i == 9 && j == 8) || (i == 8 && j == 4) || (i == 7 && j == 7) || (i == 2 && j == 1)) {
            newCell.style.backgroundColor = 'black'
            newCell.innerText = "0"
            newCell.value = "0"
            newCell.style.color = 'lime'
        }
        else if ((i == 7 && j == 2) || (i == 4 && (j == 1 || j == 5))) {
            newCell.style.backgroundColor = 'black'
            newCell.innerText = "1"
            newCell.value = "1"
            newCell.style.color = 'white'
        }
        else if (j == 7 && i == 1) {
            newCell.style.backgroundColor = 'black'
            newCell.innerText = "2"
            newCell.value = "2"
            newCell.style.color = 'white'
        }
        else if ((i == 8 && j == 0) || (i == 1 && j == 5) || (i == 5 && j == 8)) {
            newCell.style.backgroundColor = 'black'
            newCell.innerText = "3"
            newCell.value = "3"
            newCell.style.color = 'white'
        }
        else {newCell.style.backgroundColor = 'white'}
        newRow.appendChild(newCell);
        }
    }
container.appendChild(table);
timeOn = true
}

function colorYellow(val,val2,val3,val4,par5,e) {
    var row = e.target.parentNode  
    var ttable = e.target.parentNode.parentNode
    modify = false
    let over = [false,false,false,false]
    let i = val
    let r1 = setInterval(() => {
        if (i < maxV && row.cells[i].style.backgroundColor != 'black') {
            if (row.cells[i].innerText == "💡") {
                row.cells[i].style.backgroundColor = 'red'
                e.target.style.backgroundColor = 'red'
            }
            else {
                row.cells[i].style.backgroundColor = 'yellow'
            }
            i++;
        }
        else {
            clearInterval(r1)
            over[0] = true
        }
    },200)
    let j = val2
    let r2 = setInterval(() => {
        if (j >= 0 && row.cells[j].style.backgroundColor != 'black') {
            if (row.cells[j].innerText == "💡") {
                row.cells[j].style.backgroundColor = 'red'
                e.target.style.backgroundColor = 'red'
            }
            else {
                row.cells[j].style.backgroundColor = 'yellow'
            }
            j--;
        }
        else {
            clearInterval(r2)
            over[1] = true
        }
    },200)
    let k = val3
    let r3 = setInterval(() => {
        if (k < maxV && ttable.rows[k].cells[par5].style.backgroundColor != 'black') {
            if (ttable.rows[k].cells[par5].innerText == "💡") {
                ttable.rows[k].cells[par5].style.backgroundColor = 'red'
                e.target.style.backgroundColor = 'red'
            }
            else {
            ttable.rows[k].cells[par5].style.backgroundColor = 'yellow'
            }
            k++;
        }
        else {
            clearInterval(r3)
            over[2] = true
        }
    },200)
    let l = val4
    let r4 = setInterval(() => {
        if (l >= 0 && ttable.rows[l].cells[par5].style.backgroundColor != 'black') {
            if (ttable.rows[l].cells[par5].innerText == "💡") {
                ttable.rows[l].cells[par5].style.backgroundColor = 'red'
                e.target.style.backgroundColor = 'red'
            }
            else {
                ttable.rows[l].cells[par5].style.backgroundColor = 'yellow'
            }
            l--;
        }
        else {
            clearInterval(r4)
            over[3] = true
        }
    },200)
    let overTimer = setInterval(() => {
        if(over[0] && over[1] && over[2] && over[3]) {
            modify = true
            clearInterval(overTimer)
        }
    },200)
}
function decolor(val,val2,val3,val4,par5,e) {
    var row = e.target.parentNode  
    var ttable = e.target.parentNode.parentNode
    for (let i = val; i < maxV; i++) {
        if (row.cells[i].style.backgroundColor != 'black') {
            row.cells[i].style.backgroundColor = 'white'
        }
        else {
            break;
        }
    }
    for (let j = val2; j >= 0; j--) {
        if (row.cells[j].style.backgroundColor != 'black') {
            row.cells[j].style.backgroundColor = 'white'
        }
        else {
            break;
        }
    }
    for (let i = val3; i < maxV; i++) {
        if (ttable.rows[i].cells[par5].style.backgroundColor != 'black') {
            ttable.rows[i].cells[par5].style.backgroundColor = 'white'
        }
        else {
            break;
        }
    }
    for (let j = val4; j >= 0 ; j--) {
        if (ttable.rows[j].cells[par5].style.backgroundColor != 'black') {
            ttable.rows[j].cells[par5].style.backgroundColor = 'white'
        }
        else {
            break;
        }
    }
}
function redraw(l1,l2,k1,k2,k,l,table) {
    let maybeRed = false
    for (let i = l1; i < maxV; i++) {
        if (table.rows[k].cells[i].style.backgroundColor != 'black') {
            if (table.rows[k].cells[i].innerText == "💡") {
                table.rows[k].cells[i].style.backgroundColor = 'red'
                maybeRed = true
            }
            else {
                table.rows[k].cells[i].style.backgroundColor = 'yellow'
            }
        }
        else {
            break;
        }
    }
    for (let j = l2; j >= 0; j--) {
        if (table.rows[k].cells[j].style.backgroundColor != 'black') {
            if (table.rows[k].cells[j].innerText == "💡") {
                table.rows[k].cells[j].style.backgroundColor = 'red'
                maybeRed = true
            }
            else {
                table.rows[k].cells[j].style.backgroundColor = 'yellow'
            }
        }
        else {
            break;
        }
    }
    for (let i = k1; i < maxV; i++) {
        if (table.rows[i].cells[l].style.backgroundColor != 'black') {
            if (table.rows[i].cells[l].innerText == "💡") {
                table.rows[i].cells[l].style.backgroundColor = 'red'
                maybeRed = true
            }
            else {
            table.rows[i].cells[l].style.backgroundColor = 'yellow'
            }
        }
        else {
            break;
        }
    }
    for (let j = k2; j >= 0 ; j--) {
        if (table.rows[j].cells[l].style.backgroundColor != 'black') {
            if (table.rows[j].cells[l].innerText == "💡") {
                table.rows[j].cells[l].style.backgroundColor = 'red'
                maybeRed = true
            }
            else {
                table.rows[j].cells[l].style.backgroundColor = 'yellow'
            }
        }
        else {
            break;
        }
    }
    return maybeRed
}
function reset() {
    timeOn = false
    clearInterval(timechange)
    table.innerHTML = ""
    time()
    if (resetB.value == 1) {
        Level1()
    }
    if (resetB.value == 2) {
        Level2()
    }
    if (resetB.value == 3) {
        Level3()
    }
}
function solution() {
    let l = true
    for(let i = 0;i<maxV;i++) {
        for(let j = 0;j<maxV;j++) {
            if(table.rows[i].cells[j].style.backgroundColor == 'white' || table.rows[i].cells[j].style.backgroundColor == 'red' || (table.rows[i].cells[j].style.backgroundColor == 'black' && (table.rows[i].cells[j].style.color != "lime" && table.rows[i].cells[j].innerText != ""))) {
                alert("A megoldás helytelen");
                l = false
                break;
            }
        }
        if(l == false) {
            break; 
        }
    }
    if(l){
        alert("A megoldás helyes :)")
        let newLi = document.createElement("li")
        newLi.innerText = "Játékos: " + window.localStorage.getItem("Playername") + ", Szint: Level " + resetB.value + ", Idő: " + timetex.value
        let numres
        if (window.localStorage.getItem("Results_num") != null) {
            numres = window.localStorage.getItem("Results_num")
        }
        else {
            numres = 0
        }
        window.localStorage.setItem("Results_" + numres, newLi.innerText)
        numres++
        window.localStorage.setItem("Results_num", numres)
        results.appendChild(newLi)
        reset()
    }
}

function checkBlackBoxMinus(a){
    if(a.style.backgroundColor == 'black' && a.value != -100) {
        a.value--;
        if(a.value == 0) {
            a.style.color = "lime"
        }
        if(a.value != 0) {
            a.style.color = "white"
        }}
}
function checkBlackBoxPlus(a){
    if(a.style.backgroundColor == 'black' && a.value != -100) {
        a.value++;
        if(a.value == 0) {
            a.style.color = "lime"
        }
        if(a.value > 0) {
            a.style.color = "white"
        }}
}

jatek.addEventListener("click",() => {
    buttons.style.display = "block"
    jatek.style.display = "none"
    leiras.style.display = "none"
    folytGomb.style.display = "none"
})
leiras.addEventListener("click",() => {
    jatek.style.display = "none"
    leiras.style.display = "none"
    pname.style.display = "none"
    szoveg.style.display = "inline"
    backbutton.style.display = "block"
    menu.style.height = '500px'
    results.style.display = "none"
    folytGomb.style.display = "none"
    back.value = 1
})

level1.addEventListener("click", () => {
    fg_displays()
    menu.style.display = "none"
    container.style.display = "block"
    resetB.value = 1
    back.value = 2
    Level1()
    prevGame = new Array(maxV)
    for (let i = 0; i < prevGame.length; i++) {
        prevGame[i] = new Array(maxV).fill(0)
    }
})
level2.addEventListener("click", () => {
    fg_displays()
    menu.style.display = "none"
    container.style.display = "block"
    resetB.value = 2
    back.value = 2
    Level2()
    prevGame = new Array(maxV)
    for (let i = 0; i < prevGame.length; i++) {
        prevGame[i] = new Array(maxV).fill(0)
    }
})
level3.addEventListener("click", () => {
    fg_displays()
    menu.style.display = "none"
    container.style.display = "block"
    resetB.value = 3
    back.value = 2
    Level3()
    prevGame = new Array(maxV)
    for (let i = 0; i < prevGame.length; i++) {
        prevGame[i] = new Array(maxV).fill(0)
    }
})

container.addEventListener("click", (e) => {
    const val = e.target.value + 1
    const val2 = e.target.value - 1
    const val3 = e.target.parentNode.value + 1
    const val4 = e.target.parentNode.value - 1
    var row = e.target.parentNode  
    var ttable = e.target.parentNode.parentNode
    if (e.target.matches("td") && e.target.style.backgroundColor != "black" && e.target.innerText == "💡" && modify) {
        e.target.style.backgroundColor = "white"
        e.target.innerText = ""
        prevGame[e.target.parentNode.value][e.target.value] = 0
        if (val < maxV) {
            checkBlackBoxPlus(row.cells[val])  
        }
        if (val2 > -1) {
            checkBlackBoxPlus(row.cells[val2])
        }
        if (val3 < maxV) {
            checkBlackBoxPlus(ttable.rows[val3].cells[e.target.value])
        }
        if (val4 > -1) {
            checkBlackBoxPlus(ttable.rows[val4].cells[e.target.value])
        }
        decolor(val,val2,val3,val4,e.target.value,e);
        for(let k = 0; k < maxV;k++) {
            let logical = false
            for(let l = 0; l < maxV;l++) {
                if(ttable.rows[k].cells[l].innerText == "💡") {
                    logical = redraw(l+1,l-1,k+1,k-1,k,l,table,e)
                    if (logical){
                        ttable.rows[k].cells[l].style.backgroundColor = "red"
                    }
                    else {
                        ttable.rows[k].cells[l].style.backgroundColor = "yellow" 
                    }
                }
            }
        }
    }
    else if (e.target.matches("td") && e.target.style.backgroundColor != "black" && modify) {
        e.target.style.backgroundColor = "yellow"
        e.target.innerText = "💡"
        prevGame[e.target.parentNode.value][e.target.value] = 1
        if (val < maxV) {
            checkBlackBoxMinus(row.cells[val])  
        }
        if (val2 > -1) {
            checkBlackBoxMinus(row.cells[val2])
        }
        if (val3 < maxV) {
            checkBlackBoxMinus(ttable.rows[val3].cells[e.target.value])
        }
        if (val4 > -1) {
            checkBlackBoxMinus(ttable.rows[val4].cells[e.target.value])
        }
        colorYellow(val,val2,val3,val4,e.target.value,e)
    }
})
megoldas.addEventListener("click", () => {
    solution();
})
resetB.addEventListener("click", () => {
    reset()
})
back.addEventListener("click", () => {
    init_displays()
    menu.style.display = "block"
    results.style.display = "block"
    jatek.style.display = "inline"
    pname.style.display = "block"
    leiras.style.display = "inline"
    if (back.value == 2) {
        table.innerHTML = ""
        container.removeChild(container.firstElementChild)
        container.style.display = "none"
        timeOn = false
        clearInterval(timechange)
    }
})

SaveB.addEventListener("click",() => {
    for(let i = 0;i < prevGame.length;i++) {
        for(let j = 0;j < prevGame[i].length;j++) {
            window.localStorage.setItem("prevGame_" + i + "_" + j,prevGame[i][j])
        }
    }
    window.localStorage.setItem("Continue",resetB.value)
    window.localStorage.setItem("S",s)
})
folytGomb.addEventListener("click", () => {
    if(window.localStorage.getItem("Continue") == 1) {
        Level1()
    }
    if(window.localStorage.getItem("Continue") == 2) {
        Level2()
    }
    if(window.localStorage.getItem("Continue") == 3) {
        Level3()
    }
    resetB.value = window.localStorage.getItem("Continue")
    prevGame = new Array(maxV)
    for (let i = 0; i < prevGame.length; i++) {
        prevGame[i] = new Array(maxV)
        for (let j = 0; j < prevGame[i].length; j++) {
            prevGame[i][j] = window.localStorage.getItem("prevGame_"+ i + "_" + j)
        }
    }
    for(let i = 0;i < prevGame.length;i++) {
        for(let j = 0;j < prevGame[i].length;j++){
            if(prevGame[i][j] == 1) {
                table.rows[i].cells[j].innerText = "💡"
                table.rows[i].cells[j].style.backgroundColor = "yellow"
                if (j+1 < maxV) {
                    checkBlackBoxMinus(table.rows[i].cells[j+1])  
                }
                if (j-1 > -1) {
                    checkBlackBoxMinus(table.rows[i].cells[j-1])
                }
                if (i+1 < maxV) {
                    checkBlackBoxMinus(table.rows[i+1].cells[j])
                }
                if (i-1 > -1) {
                    checkBlackBoxMinus(table.rows[i-1].cells[j])
                }
                for(let k = j+1;k < maxV;k++){
                    if (table.rows[i].cells[k].style.backgroundColor != 'black') {
                        if (table.rows[i].cells[k].innerText == "💡") {
                            table.rows[i].cells[k].style.backgroundColor = 'red'
                            table.rows[i].cells[j].style.backgroundColor = 'red'
                        }
                        else {
                            table.rows[i].cells[k].style.backgroundColor = 'yellow'
                        }
                    }
                    else {
                        break
                    }
                }
                for(let k = j-1;k >= 0;k--){
                    if (table.rows[i].cells[k].style.backgroundColor != 'black') {
                        if (table.rows[i].cells[k].innerText == "💡") {
                            table.rows[i].cells[k].style.backgroundColor = 'red'
                            table.rows[i].cells[j].style.backgroundColor = 'red'
                        }
                        else {
                            table.rows[i].cells[k].style.backgroundColor = 'yellow'
                        }
                    }
                    else {
                        break
                    }
                }
                for(let k = i+1;k < maxV;k++){
                    if (table.rows[k].cells[j].style.backgroundColor != 'black') {
                        if (table.rows[k].cells[j].innerText == "💡") {
                            table.rows[k].cells[j].style.backgroundColor = 'red'
                            table.rows[k].cells[j].style.backgroundColor = 'red'
                        }
                        else {
                            table.rows[k].cells[j].style.backgroundColor = 'yellow'
                        }
                    }
                    else {
                        break
                    }
                }
                for(let k = i-1;k >= 0;k--){
                    if (table.rows[k].cells[j].style.backgroundColor != 'black') {
                        if (table.rows[k].cells[j].innerText == "💡") {
                            table.rows[k].cells[j].style.backgroundColor = 'red'
                            table.rows[i].cells[j].style.backgroundColor = 'red'
                        }
                        else {
                            table.rows[k].cells[j].style.backgroundColor = 'yellow'
                        }
                    }
                    else {
                        break
                    }
                }
            }
        }
    }
    fg_displays()
    s = window.localStorage.getItem("S")
    menu.style.display = "none"
    container.style.display = "block"
    back.value = 2
})