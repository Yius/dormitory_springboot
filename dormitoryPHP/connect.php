<?php
    $host='localhost';
    $userName='root';
    $password='demaxiya08';
    $dbName='dormitory';
    $conn = mysqli_connect($host,$userName,$password,$dbName)
    or die("数据库连接失败!".mysqli_error());
    mysqli_query($conn, "set names utf8");
?>
    