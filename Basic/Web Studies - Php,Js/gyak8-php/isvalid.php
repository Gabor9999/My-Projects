<?php
    $errors = [];

    $i = $_GET;
    
    if(!isset($i["name"]) || strlen(trim($i["name"])) == 0) {
        $errors["name"] = "Add meg a neved!";
    }
    else if (strlen($i["name"]) < 3) {
        $errors["name"] = "A neved túl rövid";
    }

    if(!isset($i["email"]) || strlen(trim($i["email"])) == 0) {
        $errors["email"] = "Add meg az email címed!";
    }

    if(!isset($i["film"]) || strlen(trim($i["film"])) == 0) {
        $errors["film"] = "Add meg a film nevét!";
    }
    else if (strlen($i["film"]) < 3) {
        $errors["film"] = "A film neve túl rövid";
    }

    if(!isset($i["filmV"]) || strlen(trim($i["filmV"])) == 0) {
        $errors["filmV"] = "Nem értékelted a filmet!";
    }
    else if(intval($i["filmV"]) < 1 || intval($i["filmV"]) > 5) {
        $errors["filmV"] = "Rosszul értékelted a filmet";
    }

    if(!$errors) {
        if(file_exists("velemenyek.json")) {
            $file = file_get_contents("velemenyek.json");
            $tomb = json_decode($file, true);
        }
        else {
            $tomb = [];
        }
        
        
        $new = [
            "name" => $i["name"],
            "email" => $i["email"],
            "film" => $i["film"],
            "filmV" => $i["filmV"],
        ];

        array_push($tomb, $new);
        
        file_put_contents("velemenyek.json", json_encode($tomb, JSON_PRETTY_PRINT));
    }
?>
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
    <?php if($errors): ?>
        <h2>Hiba történt az elküldés közben!</h2>
        <ul>
            <?php foreach($errors as $elem): ?>
                <li><?= $elem; ?></li>
            <?php endforeach; ?>
        </ul>
        <button onclick="window.location.href= 'velemeny.php'">Visszalépés az űrlaphoz</button>
    <?php else: ?>
        <h2>Sikeresen elküldted a véleményedet!</h2>
        <button onclick="window.location.href= 'index.php'">Visszalépés a főoldalra</button>
    <?php endif; ?>
</body>
</html>