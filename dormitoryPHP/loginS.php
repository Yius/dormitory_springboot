<?php
    include_once("connect.php");
    $id=$_POST['ID'];//APP post过来的用户名
    $pwd=$_POST['password'];//APP post过来的密码
    $sql=mysqli_query($conn,"SELECT * FROM userinfo WHERE ID ='$id'");
    $result=mysqli_fetch_assoc($sql);
    if(!empty($result)){
        if($pwd==$result['password']){
            $back['status']="1";
            $back['info']="login success";
            echo(json_encode($back));
        }else{/*密码错误*/
            $back['status']="-2";
            $back['info']="password wrong";
            echo(json_encode($back));
        } 
    }else{
        //不存在该用户
        $back['status']="-1";
        $back['info']="user not exist";
        echo(json_encode($back));
    }
    mysqli_close($conn);
 ?>   