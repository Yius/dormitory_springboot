<?php
    include_once("connect.php");
    $AID=(int)$_POST['AID'];//APP post过来的announcement的ID
    $result=mysqli_query($conn,"DELETE FROM announcement WHERE ID = '$AID'");
    if($result){
        $back['status']="18";
        $back['info']="delete success";
        echo (json_encode($back));
    }else{
        $back['status']="-24";
        $back['info']="delete fail";
        echo (json_encode($back));
    }
    mysqli_close($conn);
 ?>