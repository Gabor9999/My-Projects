var c = document.querySelector("#canvas");
var ctx = c.getContext("2d");
var slider = document.querySelector("#myRange");
let val = slider.value;
c.height = 500
c.width = 500
ctx.font ="30px Arial"
ctx.fillText(val, (c.width/2)-20, (c.height/2)+10);
ctx.lineWidth = 50
slider.oninput = function() {
    val = this.value;
    ctx.clearRect(0, 0, c.width, c.height);
    ctx.fillText(val, (c.width/2)-20, (c.height/2)+10);
    ctx.beginPath();
    ctx.arc(250, 250, 200, 0,(Math.PI/180)*val);
    ctx.stroke();
  }