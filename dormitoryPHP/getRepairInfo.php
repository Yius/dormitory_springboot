<?php
    include_once("connect.php");
    $belong=$_POST['govern'];//APP post过来的宿管所管理的楼号
    $which=(integer)$_POST['which'];//APP post过来的决定变量，1代表查询已处理的，0代表查询未处理的，2代表查询全部
    if($which!=2){
        $sql=mysqli_query($conn,"SELECT * FROM repairinfo WHERE Status = $which AND belong LIKE '$belong' ORDER BY Status , ApplyDate DESC");
    }else{
        $sql=mysqli_query($conn,"SELECT * FROM repairinfo WHERE belong LIKE '$belong' ORDER BY ApplyDate DESC");
    }
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