<?php
    include_once("connect.php");
    $SendTime=$_POST['SendTime'];//APP post过来的消息发送时间
    $SenderID=$_POST['SenderID'];//APP post过来的发送者ID
    $SenderName=$_POST['SenderName'];//APP post过来的发送者姓名
    $PostsID=$_POST['PostsID'];//APP post过来的发送所在帖ID
    $Message=$_POST['Message'];//APP post过来的消息内容
    $result=mysqli_query($conn,"INSERT INTO sendmessageinfo VALUES('$SendTime','$SenderID','$SenderName',$PostsID,'$Message')");
     if($result){
       $result=mysqli_query($conn,"UPDATE postsinfo SET LatestReplyTime = '$SendTime' WHERE PostsID = '$PostsID'");
       if($result){
       	$back['status']="16";
      	$back['info']="send success and update postsinfo success";
         	echo (json_encode($back));
       }else{
           $back['status']="-20";
           $back['info']="send success but update postsinfo fail";
           echo (json_encode($back));
       }	
    }else{
       $back['status']="-21";
       $back['info']="send fail";
       echo (json_encode($back));
    }
    mysqli_close($conn);
 ?>