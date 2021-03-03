<?php
    include_once("connect.php");
    $Rtime=$_POST['Rtime'];//APP post������ǩ������
    $houseparentID=$_POST['houseparentID'];//APP post�������޹ܺ�
    $title=$_POST['title'];//APP post������ǩ������
    $govern=$_POST['govern'];//APP post�������޹ܹ�����¥��
    $latitude=(double)$_POST['latitude'];//APP post过来的纬度
    $longitude=(double)$_POST['longitude'];//APP post过来的经度
    $detailAddress=$_POST['detailAddress'];//APP post过来的经度  
    $houseparentName=$_POST['houseparentName'];//APP post过来的经度 
    $sql=mysqli_query($conn,"SELECT COUNT(*) as 'totalnums' FROM userinfo WHERE belong LIKE '$govern'");
    $totalnums=0;
    if($result=mysqli_fetch_assoc($sql)){
        $totalnums=$result['totalnums'];
    }
    $result=mysqli_query($conn,"INSERT INTO signrecord (Rtime,houseparentID,title,govern,totalnums,latitude,longitude,detailAddress,houseparentName) VALUES('$Rtime','$houseparentID','$title','$govern','$totalnums','$latitude','$longitude','$detailAddress','$houseparentName')");
     if($result){
       $back['status']="13";
       $back['info']="start a new sign success";
       echo (json_encode($back));
    }else{
       $back['status']="-16";
       $back['info']="start a new sign fail";
       echo (json_encode($back));
    }
    mysqli_close($conn);
 ?>