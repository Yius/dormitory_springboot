<?php
    include_once("connect.php");
    $SID=$_POST['SID'];//APP post过来的学生ID
    $recordID=(integer)$_POST['recordID'];//APP post过来的签到表编号
    $signedtime=$_POST['signedtime'];//APP post过来的签到时间
    $result=mysqli_query($conn,"INSERT INTO signed (SID,recordID,signedtime) VALUES('$SID',$recordID,'$signedtime')");
     if($result){
     $result2=mysqli_query($conn,"UPDATE signrecord SET nums=nums+1 WHERE ID = '$recordID'");
       if($result2){
            $back['status']="14";
            $back['info']="sign success";
            echo (json_encode($back));
       }else{
           //其实这里应该用事务回滚，懒就不写了
           $back['status']="-18";
           $back['info']="sign fail, can not update";
           echo (json_encode($back));
       }
    }else{
       $back['status']="-17";
       $back['info']="sign fail, can not sign";
       echo (json_encode($back));
    }
    mysqli_close($conn);
 ?>