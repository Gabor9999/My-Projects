<?php
$students = [
    ['name' => 'Student1', 'age' => 20],
    ['name' => 'Student2', 'age' => 10],
    ['name' => 'Student3', 'age' => 30],
    ['name' => 'Student4', 'age' => 20],
    ['name' => 'Student5', 'age' => 10],
    ];
    var_dump($_GET);
    $tomb = [];
    foreach($students as $student){
        if(isset($_GET['age']) && is_numeric($_GET['age'])) {
            if($_GET['age'] == $student["age"]){
                array_push($tomb,$student);
            }
        }
        else {
            array_push($tomb,$student);
        }
    }
?>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Gyakorlas</title>
</head>
<body>
    <form action="" method="GET">
        <input name="age" type="hidden" id="age" value="">
    </form>
    <ul>
        <?php foreach($students as $student): ?>
            <li><?= $student["name"] ?> (<?= $student["age"] ?>)</li>
        <?php endforeach ?>
        <?php foreach($tomb as $student): ?>
            <li><?= $student["name"] ?> (<?= $student["age"] ?>)</li>
        <?php endforeach ?>
    </ul>
    
</body>
</html>