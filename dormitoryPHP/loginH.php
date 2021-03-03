<?php
    include_once("connect.php");
    $id=$_POST['ID'];//APP post过来的宿管名
    $pwd=$_POST['password'];//APP post过来的密码
    $sql=mysqli_query($conn,"SELECT * FROM userinfoh WHERE ID ='$id'");
    $result=mysqli_fetch_assoc($sql);
    if(!empty($result)){
        if($pwd==$result['password']){
            $back['status']="4";
            $back['info']="login success";
            echo(json_encode($back));
        }else{/*密码错误*/
            $back['status']="-6";
            $back['info']="password wrong";
            echo(json_encode($back));
        } 
    }else{
        //不存在该用户
        $back['status']="-7";
        $back['info']="user not exist";
        echo(json_encode($back));
    }
    mysqli_close($conn);
 ?>   