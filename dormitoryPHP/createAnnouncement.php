<?php
    include_once("connect.php");
    $Atime=$_POST['Atime'];//APP post�����ķ�������
    $houseparentID=$_POST['houseparentID'];//APP post�������޹ܺ�
    $govern=$_POST['govern'];//APP post�������޹�����¥��
    $content=$_POST['content'];//APP post�����ķ�������
    $title=$_POST['title'];//APP post�����ķ�������
    $houseparentName=$_POST['houseparentName'];//APP post过来的hosueparentName
    $result=mysqli_query($conn,"INSERT INTO announcement (Atime,houseparentID,content,title,govern,houseparentName) VALUES('$Atime','$houseparentID','$content','$title','$govern','$houseparentName')");
     if($result){
       $back['status']="7";
       $back['info']="announce success";
       echo (json_encode($back));
    }else{
       $back['status']="-10";
       $back['info']="announce fail";
       echo (json_encode($back));
    }
    mysqli_close($conn);
 ?>