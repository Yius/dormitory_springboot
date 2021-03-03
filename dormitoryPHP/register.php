<?php
    include_once("connect.php");
    $id=$_POST['ID'];//APP post过来的学号
    $pwd=$_POST['password'];//APP post过来的密码
    $name=$_POST['name'];//APP post过来的姓名
    $dorm=$_POST['dormID'];//APP post过来的宿舍号
    $phone=$_POST['phone'];//APP post过来的手机号
    $nickname=$_POST['nickname'];//APP post过来的昵称
    $belong=$_POST['belong'];//APP post过来的所属宿舍楼
    $sql=mysqli_query($conn,"SELECT * FROM userinfo WHERE ID ='$id'");
    $result=mysqli_fetch_assoc($sql);
    if(!empty($result)){
        $back['status']="-3";
        $back['info']="user has been existed";
        echo(json_encode($back));
    }else{
        $result=mysqli_query($conn,"INSERT INTO userinfo(ID,name,dormID,phone,password,nickname,belong) VALUES('$id','$name','$dorm','$phone','$pwd','$nickname','$belong')");
        if($result){
            $back['status']="2";
            $back['info']="register success";
            echo (json_encode($back));
        }else{
            $back['status']="-4";
            $back['info']="register fail";
            echo (json_encode($back));
        }
    }
    mysqli_close($conn);
 ?>   