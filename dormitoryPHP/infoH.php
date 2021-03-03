<?php
    include_once("connect.php");
    $id=$_POST['ID'];//APP post过来的宿管ID
    $sql=mysqli_query($conn,"SELECT * FROM userinfoh WHERE ID ='$id'");
    $result=mysqli_fetch_assoc($sql);
    if(!empty($result)){
        echo(json_encode($result));
    }
    mysqli_close($conn);
 ?>   