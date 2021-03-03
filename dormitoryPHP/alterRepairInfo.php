<?php
    include_once("connect.php");
    $ApplyID=(integer)$_POST['ApplyID'];//APP post过来的学号
    $result=mysqli_query($conn,"UPDATE repairinfo SET Status = 1 WHERE ApplyID = $ApplyID");
    if($result){
        $back['status']="6";
        $back['info']="alter repair information success";
        echo (json_encode($back));
    }else{
        $back['status']="-9";
        $back['info']="alter repair information fail";
        echo (json_encode($back));
    }
    mysqli_close($conn);
 ?>   