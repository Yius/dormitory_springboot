<?php
    include_once("connect.php");
    $belong=$_POST['belong'];//APP post过来的学生所属楼号
    $SID=$_POST['SID'];//APP post过来的学号
    $sql=mysqli_query($conn,"SELECT * FROM signrecord WHERE govern LIKE '$belong' AND DATE_SUB(CURDATE(), INTERVAL 7 DAY) <= DATE(Rtime) AND ID NOT IN (SELECT recordID FROM signed WHERE SID LIKE '$SID') ORDER BY Rtime DESC");
    $res="[";
    while($result=mysqli_fetch_assoc($sql)){
        $res.=json_encode($result);
        $res.=",";
    }
    $res=rtrim($res,",");
    $res.="]";
    echo $res;
    mysqli_close($conn);
 ?>   