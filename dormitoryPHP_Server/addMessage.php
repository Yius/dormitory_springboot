<?php
    include_once("connect.php");
    $SendTime=$_POST['SendTime'];//APP post��������Ϣ����ʱ��
    $SenderID=$_POST['SenderID'];//APP post�����ķ�����ID
    $SenderName=$_POST['SenderName'];//APP post�����ķ���������
    $PostsID=$_POST['PostsID'];//APP post�����ķ���������ID
    $Message=$_POST['Message'];//APP post��������Ϣ����
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