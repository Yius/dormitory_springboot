<?php
    include_once("connect.php");
    $recordID=(integer)$_POST['recordID'];//APP post过来的表编号
    $govern=$_POST['govern'];//APP post过来的宿管管理的楼号
    $sql=mysqli_query($conn,"SELECT name,dormID,phone FROM userinfo WHERE belong LIKE '$govern' AND ID NOT IN (SELECT SID FROM signed WHERE recordID = '$recordID') ORDER BY dormID");
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