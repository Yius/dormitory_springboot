<?php
    include_once("connect.php");
    $LatestReplyTime=$_POST['LatestReplyTime'];//APP post���������»ظ�ʱ��(�ռ�ʱ�뷢������ʱ����ͬ)
    $PostsDate=$_POST['PostsDate'];//APP post�����ķ�������
    $ID=$_POST['ID'];//APP post������ѧ��ID
    $name=$_POST['name'];//APP post������ѧ������
    $postsTitle=$_POST['postsTitle'];//APP post���������ӱ���
    $postsContent=$_POST['postsContent'];//APP post��������������
    $result=mysqli_query($conn,"INSERT INTO postsinfo(LatestReplyTime,PostsDate,ID,name,postsTitle,postsContent) VALUES('$LatestReplyTime','$PostsDate','$ID','$name','$postsTitle','$postsContent')");
     if($result){
       $back['status']="15";
       $back['info']="publish success";
       echo (json_encode($back));
    }else{
       $back['status']="-19";
       $back['info']="publish fail";
       echo (json_encode($back));
    }
    mysqli_close($conn);
 ?>