<?php
    include_once("connect.php");
    $SID=$_POST['SID'];//APP post������ѧ��ID
    $recordID=(integer)$_POST['recordID'];//APP post������ǩ������
    $signedtime=$_POST['signedtime'];//APP post������ǩ��ʱ��
    $result=mysqli_query($conn,"INSERT INTO signed (SID,recordID,signedtime) VALUES('$SID',$recordID,'$signedtime')");
     if($result){
     $result2=mysqli_query($conn,"UPDATE signrecord SET nums=nums+1 WHERE ID = '$recordID'");
       if($result2){
            $back['status']="14";
            $back['info']="sign success";
            echo (json_encode($back));
       }else{
           //��ʵ����Ӧ��������ع������Ͳ�д��
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