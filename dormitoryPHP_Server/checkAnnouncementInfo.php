<?php
    include_once("connect.php");
    $houseparentID=$_POST['houseparentID'];//APP post过来的宿管号
    $sql=mysqli_query($conn,"SELECT * FROM announcement WHERE houseparentID LIKE '$houseparentID' ORDER BY Atime DESC");
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