<?php
    include_once("connect.php");
    $PostsID=$_POST['PostsID'];//APP post过来的帖子ID
    $result=mysqli_query($conn,"DELETE FROM postsinfo WHERE PostsID = '$PostsID'");
     if($result){
       $result=mysqli_query($conn,"DELETE FROM sendmessageinfo WHERE PostsID = '$PostsID'");
       if($result){
         $back['status']="17";
         $back['info']="delete success";
         echo (json_encode($back));
       }else{
           $back['status']="-22";
           $back['info']="delete fail";
           echo (json_encode($back));
       }
    }else{
       $back['status']="-23";
       $back['info']="delete fail";
       echo (json_encode($back));
    }
    mysqli_close($conn);
 ?>