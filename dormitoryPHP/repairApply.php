<?php
    include_once("connect.php");
    $ApplyDate=$_POST['ApplyDate'];//APP post过来的申请日期
    $dormID=$_POST['dormID'];//APP post过来的宿舍号
    $RepairName=$_POST['RepairName'];//APP post过来的维修物件
    $DamageCause=$_POST['DamageCause'];//APP post过来的损坏原因
    $Details=$_POST['Details'];//APP post过来的详细描述
    $Contact=$_POST['Contact'];//APP post过来的联系方式(手机号)
    $OtherRemarks=$_POST['OtherRemarks'];//APP post过来的其他备注
    $belong=$_POST['belong'];//APP post过来的宿舍楼
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