<?php
    include_once("connect.php");
    $registerDate=$_POST['registerDate'];//APP post�������ύʱ��
    $ID=$_POST['ID'];//APP post������ѧ��ID
    $dormID=$_POST['dormID'];//APP post�����������
    $startDate=$_POST['startDate'];//APP post���������޿�ʼʱ��
    $endDate=$_POST['endDate'];//APP post���������޽���ʱ��
    $contact=$_POST['contact'];//APP post��������ϵ��ʽ
    $belong=$_POST['belong'];//APP post����������¥
    $name=$_POST['name'];//APP post过来的name
    $result=mysqli_query($conn,"INSERT INTO stayinfo(registerDate,ID,dormID,startDate,endDate,contact,belong,name) VALUES('$registerDate','$ID','$dormID','$startDate','$endDate','$contact','$belong','$name')");
     if($result){
       $back['status']="11";
       $back['info']="commit success";
       echo (json_encode($back));
    }else{
       $back['status']="-14";
       $back['info']="commit fail";
       echo (json_encode($back));
    }
    mysqli_close($conn);
 ?>