<?php
    include_once("connect.php");
    $belong=$_POST['belong'];//APP post过来的学生所属宿舍楼
    $sql=mysqli_query($conn,"SELECT * FROM announcement WHERE govern LIKE '$belong' ORDER BY Atime DESC LIMIT 3");
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