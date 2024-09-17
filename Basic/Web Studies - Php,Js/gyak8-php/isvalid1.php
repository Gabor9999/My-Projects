<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Új kutyus érkezett!</title>
    <link rel="stylesheet" href="style.css">
    <title>Mentés</title>
</head>
<body>
<?php
    $errors = [];
    $var = $_GET;
if(!isset($var["szin"]) || !isset($var["szulido"]) || !isset($var["nem"]) || !isset($var["nev"])) {
    array_push($errors, "Nincs megadva minden adat");}
if(!(strlen(trim($var["szin"]," ")) > 0 && strlen(trim($var["szulido"]," ")) > 0 && strlen(trim($var["nem"]," ")) > 0 && strlen(trim($var["nev"]," ")) > 2)) {
    array_push($errors, "Nem minden adat elég hosszú");}
if($var["nem"] !== "kan" && $var["nem"] !== "szuka") {
    array_push($errors, "A nem nincs megfelelően megadva");
}
if(!$errors) {
    if(file_exists("kutyuk.json")) {
        $file = file_get_contents("kutyuk.json");
        $tomb = json_decode($file, true);
    }
    else {
        $tomb = [];
    }
    
    
    $new = [
        "nev" => $var["nev"],
        "szulido" => $var["szulido"],
        "nem" => $var["nem"],
        "szin" => $var["szin"],
    ];
    $l = false;
    $szoveg = "";
    foreach($tomb as $elem) {
        if($elem["nev"] == $new["nev"] && $elem["nem"] == $new["nem"] && $elem["szulido"] == $new["szulido"] && $elem["szin"] == $new["szin"]) {
            $l = true;
        }
    }
    if($l == false){
        $szoveg = "Siker 😍";
        array_push($tomb, $new);
        file_put_contents("kutyuk.json", json_encode($tomb, JSON_PRETTY_PRINT));}
    else {
        $szoveg = "Nem sikerült 😿";
    }
}
?>
<div id="main">
        <h1><?= $szoveg ?></h1>
</div>

<?php if ($errors) : ?>
        <ul style="font-size: 25px;color: red;">
        <?php foreach($errors as $error) : ?>
            <li><?= $error ?></li>
            <?php endforeach; ?>
        </ul>
<?php endif; ?>


</body>
</html>