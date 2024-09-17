<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=from, initial-scale=1.0">
    <title>Hf</title>
    <style>
        @import url("https://cdn.jsdelivr.net/gh/elte-fi/www-assets@19.10.16/styles/mdss.min.css");
    </style>
</head>
<body>
    <h1>Film véleménygyűjtés</h1>
    <form action="isvalid.php" method="get">
        <label for="nev">Neved</label></br>
        <input type="text" id="name" name="name" value=""></br>
        <label for="email">E-mail cím</label></br>
        <input type="email" id="email" name="email" value=""></br>
        <label for="film">Film címe</label></br>
        <input type="text" id="film" name="film" value=""></br>
        <label for="filmV">Film értékelése:</label></br>
        <input type="radio" id="filmV" name="filmV" value="1">1
        <input type="radio" id="filmV" name="filmV" value="2">2
        <input type="radio" id="filmV" name="filmV" value="3">3
        <input type="radio" id="filmV" name="filmV" value="4">4
        <input type="radio" id="filmV" name="filmV" value="5">5</br>
        <button type="submit">Küldés</button>


    </form>
    <br><button onclick="window.location.href= 'index.php'">Visszalépés a főoldalra</button>
</body>
</html>