<?php
    include_once("connect.php");
    $id=$_POST['ID'];//APP post过来的学号
    $phone=$_POST['phone'];//APP post过来的手机号
    $nickname=$_POST['nickname'];//APP post过来的昵称
    $result=mysqli_query($conn,"UPDATE userinfo SET phone = '$phone', nickname = '$nickname' WHERE id = '$id'");
    if($result){
        $back['status']="3";
        $back['info']="alter success";
        echo (json_encode($back));
    }else{
        $back['status']="-5";
        $back['info']="alter fail";
        echo (json_encode($back));
    }
    mysqli_close($conn);
 ?>   