<?php
    $ID=$_POST['ID'];//APP post过来的ID
    $type=$_POST['type'];//APP post过来的type，取值为houseparent和student中其一
    $dirPath = 'userAvatar/';//设置文件保存的目录
    header('Content-type:image/png');
    $data = file_get_contents($dirPath.$type."_".$ID.".png");
    echo($data);
?>