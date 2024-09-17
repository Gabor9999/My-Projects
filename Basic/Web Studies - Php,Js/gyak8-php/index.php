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
    <h1>Film véleménygyűjtések</h1>
    <button onclick="window.location.href= 'velemeny.php'">Vélemény beküldése</button>
    <h2>Eddig beküldött:</h2>
    <?php 
    if(file_exists("velemenyek.json")) {
        $file = file_get_contents("velemenyek.json");
        $tomb = json_decode($file, true);
    }
    else {
        $tomb = [];
    }
    ?>
    <table>
        <th>Név:</th>
        <th>Email-cím:</th>
        <th>Film neve:</th>
        <th>Értékelés:</th>
        <?php foreach($tomb as $elem): ?>
            <tr>
                <td><?= $elem["name"]?></td>
                <td><?= $elem["email"]?></td>
                <td><?= $elem["film"]?></td>
                <td><?= $elem["filmV"]?></td>
            </tr>
        <?php endforeach; ?>
    </table>
</html>