<?php
    include_once("connect.php");
    $id=$_POST['ID'];//APP post过来的宿管号
    $phone=$_POST['phone'];//APP post过来的手机号
    $result=mysqli_query($conn,"UPDATE userinfoh SET phone = '$phone' WHERE id = '$id'");
    if($result){
        $back['status']="12";
        $back['info']="alter success for houseparent";
        echo (json_encode($back));
    }else{
        $back['status']="-15";
        $back['info']="alter fail for houseparent";
        echo (json_encode($back));
    }
    mysqli_close($conn);
 ?>   