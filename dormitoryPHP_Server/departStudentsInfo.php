<?php
    include_once("connect.php");
    $belong=$_POST['govern'];//APP post过来的宿管所管理的楼号
    $sql=mysqli_query($conn,"SELECT * FROM departinfo WHERE belong LIKE '$belong' ORDER BY registerDate DESC");
    $res="[";
    while($result=mysqli_fetch_assoc($sql)){
        $res.=json_encode($result);
        $res.=",";
    }
    $res=rtrim($res,",");
    $res.="]";
    echo $res;
    mysqli_close($conn);
 ?>   