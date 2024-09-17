let bod = document.body
let table = document.createElement('table');
for (let i=0; i<10; i++){
    let newRow = document.createElement('tr');
    table.appendChild(newRow)
    for(let j=0; j<10; j++){
    var randomColor = Math.floor(Math.random()*16787265).toString(16);
    let newCell = document.createElement('td');
    newCell.style.backgroundColor = "#" + randomColor
    var characters = 'ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789';
    var charactersLength = characters.length;
    newCell.innerText = characters.charAt(Math.floor(Math.random() * charactersLength));
    newRow.appendChild(newCell);
    }
}
bod.appendChild(table);