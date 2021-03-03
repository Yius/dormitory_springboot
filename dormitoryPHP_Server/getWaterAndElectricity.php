<?php
    include_once("connect.php");
    $dorm=$_POST['dorm'];//APP post过来的dorm
    $sql=mysqli_query($conn,"SELECT * FROM waterandelectricity WHERE dorm ='$dorm'");
    $result=mysqli_fetch_assoc($sql);
    if(!empty($result)){
        echo(json_encode($result));
    }
    mysqli_close($conn);
 ?>   