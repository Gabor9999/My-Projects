let tableGenerated = false;

let t1 = document.querySelector("#task1");

t1.querySelector("button").addEventListener("click", ()=>{
    if(!tableGenerated) {
        let rows = t1.querySelector("input#row").value;
        let cols = t1.querySelector("input#col").value;
    
        if(rows > 0 && cols > 0) {
            let counter = 1;
            for (let i = 0; i < rows; i++) {
                let row = document.createElement("tr");
                for (let j = 0; j < cols; j++) {
                    let cell = document.createElement("td");
                    cell.innerHTML = counter;
                    row.append(cell);
                    counter++;
                }
                document.querySelector("table").append(row);
            }
        
            tableGenerated = true;
        }
    }
});

let selected = 0;
let num1 = 0;
let num2 = 0;

t1.addEventListener("click", (e) => {
    if(e.target.nodeName == "TD") {
        if(selected == 0) {
            e.target.style.backgroundColor = "blue";
            num1 = e.target.innerHTML;
            selected++;
        }
        else if(selected == 1) {
            e.target.style.backgroundColor = "blue";
            num2 = e.target.innerHTML;
            selected++;
            t1.querySelector("#kiiras").innerHTML = num1+"*"+num2+" = "+num1*num2;
        }
        else if(selected == 2) {
            t1.querySelectorAll("td").forEach(elem => {elem.style.backgroundColor = "transparent";});
            e.target.style.backgroundColor = "blue";
            num1 = e.target.innerHTML;
            selected = 1;
        }
    }
});