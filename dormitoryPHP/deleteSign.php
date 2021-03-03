<?php
    include_once("connect.php");
    $ID=(int)$_POST['ID'];//APP post过来的签到的ID
    $result=mysqli_query($conn,"DELETE FROM signrecord WHERE ID = '$ID'");
    if($result){
        $back['status']="20";
        $back['info']="delete sign success";
        echo (json_encode($back));
    }else{
        $back['status']="-27";
        $back['info']="delete sign fail";
        echo (json_encode($back));
    }
    mysqli_close($conn);
 ?>