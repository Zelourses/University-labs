<?php
$time_start = microtime();
$Y = $_GET["Y"];
$X = $_GET['X'];
$R = $_GET['R'];
if(($X == null) || ($Y == null) || ($R == null)){
    goBack();
}
foreach ($X as $Xs){
    if ((!checkInt($Xs)) || ($Xs < -3) || ($Xs >5) || (strlen($Xs)>=7)){
        goBack();
    }
    if ((!checkNumber($Y)) || ($Y < -3) || ($Y > 5) || (strlen($Y)>=7)){
        goBack();
    }
    if ((!checkNumber($R)) || ($R < -3) || ($R > 5) || (strlen($R)>=7)){
        goBack();
    }
}
session_start();
if (!isset($_SESSION['result']))
    $_SESSION['result'] = array();

$time_execute = number_format((float)(microtime() - $time_start), 6);
foreach ($X as $Xs){
    $b = "промах";
    if (($Xs * $Xs + $Y * $Y <= $R *$R) && ($Y <= 0) && ($X >= 0)){ //круг
        $b = "Попадание";
    }
    if (($Y <= $R) && ($Y >=0) && ($Xs >= $R/2) && ($Xs <= 0 )){ //прямоугольник, вроде даже правильно написал
        $b = "Попадание";
    }
    if (($Y <= 0) && ($Xs <=0) && ($Y  >= (-$Xs-$R)/2)){
        $b = "Попадание";
    }
    $arrayOfValues = array('X' => $Xs, 'Y' => $Y, 'R' => $R, 'Время запроса' => date('H:i:s'), 'Время выполнения' => $time_execute, 'Результат' => $b);
    array_push($_SESSION['result'], $arrayOfValues);
}
header("Location: ./result.php");
function goBack(){
    header("Location: ../badData.html");
    exit();
}
function checkInt($float){
    return (checkNumber($float) && substr_count($float,",")=== 0);
}
function checkNumber($num){
    return (is_numeric(str_replace(",",".", $num)));
}