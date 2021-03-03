<?php
    include_once("connect.php");
    $registerDate=$_POST['registerDate'];//APP post�������ύʱ��
    $ID=$_POST['ID'];//APP post������ѧ��ID
    $dormID=$_POST['dormID'];//APP post�����������
    $departCause=$_POST['departCause'];//APP post����������ԭ��
    $departTime=$_POST['departTime'];//APP post����������ʱ��
    $backTime=$_POST['backTime'];//APP post�����ķ���ʱ��
    $contact=$_POST['contact'];//APP post��������ϵ��ʽ
    $belong=$_POST['belong'];//APP post����������¥
    $name=$_POST['name'];//APP post过来的name
    $result=mysqli_query($conn,"INSERT INTO departinfo(registerDate,ID,dormID,departCause,departTime,backTime,contact,belong,name) VALUES('$registerDate','$ID','$dormID','$departCause','$departTime','$backTime','$contact','$belong','$name')");
     if($result){
       $back['status']="10";
       $back['info']="commit success";
       echo (json_encode($back));
    }else{
       $back['status']="-13";
       $back['info']="commit fail";
       echo (json_encode($back));
    }
    mysqli_close($conn);
 ?>