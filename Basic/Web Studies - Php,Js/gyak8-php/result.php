<?php
$kerdesek = [
	"1. Kedvenc ételed?" => array(
		'valaszok' => array("Pizza","Chips"),
		'helyes' => "Pizza"),
	"2. Fanta vagy Cola?" => array(
		'valaszok' => array("Fanta","Cola"),
		'helyes' => "Fanta"),
	"3. Mennyi 1+1?" => array(
		'valaszok' => array("2","3","5"),
		'helyes' => "3"),
	"4. Szereted a tejet?" => array(
		'valaszok' => array("Igen","Nem","Kakaó"),
		'helyes' => "Kakaó"),
	];
$c = 1;
foreach ($kerdesek as $id => $arr) {
    for ($i=0;$i < count($arr['valaszok']);$i++) {
        if(isset($_POST['submit'])){ 	
            echo $_POST['submit'];
            $answer = $_POST["radio"];  
            if ($answer == $id) {         
                echo $c;
                echo '.kérdés: Helyes';
                $c++;     
            }
            else {
                echo $c;
                echo '.kérdés: Helytelen';
                $c++; 
            }
        }
    }
}          
?>