<?php
    include_once("connect.php");
    $ApplyDate=$_POST['ApplyDate'];//APP post��������������
    $dormID=$_POST['dormID'];//APP post�����������
    $RepairName=$_POST['RepairName'];//APP post������ά�����
    $DamageCause=$_POST['DamageCause'];//APP post��������ԭ��
    $Details=$_POST['Details'];//APP post��������ϸ����
    $Contact=$_POST['Contact'];//APP post��������ϵ��ʽ(�ֻ���)
    $OtherRemarks=$_POST['OtherRemarks'];//APP post������������ע
    $belong=$_POST['belong'];//APP post����������¥
    $result=mysqli_query($conn,"INSERT INTO repairinfo(ApplyDate,dormID,RepairName,DamageCause,Details,Contact,OtherRemarks,belong,Status) VALUES('$ApplyDate','$dormID','$RepairName','$DamageCause','$Details','$Contact','$OtherRemarks','$belong',0)");
     if($result){
       $back['status']="5";
       $back['info']="commit success";
       echo (json_encode($back));
    }else{
       $back['status']="-8";
       $back['info']="commit fail";
       echo (json_encode($back));
    }
    mysqli_close($conn);
 ?>