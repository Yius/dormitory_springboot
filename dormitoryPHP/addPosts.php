<?php
    include_once("connect.php");
    $LatestReplyTime=$_POST['LatestReplyTime'];//APP post过来的最新回复时间(刚加时与发布帖子时间相同)
    $PostsDate=$_POST['PostsDate'];//APP post过来的发布日期
    $ID=$_POST['ID'];//APP post过来的学生ID
    $name=$_POST['name'];//APP post过来的学生姓名
    $postsTitle=$_POST['postsTitle'];//APP post过来的帖子标题
    $postsContent=$_POST['postsContent'];//APP post过来的帖子内容
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