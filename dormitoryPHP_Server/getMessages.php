<?php
    include_once("connect.php");
    $PostsID = (integer)$_POST['PostsID'];//APP post过来的帖子ID
    $sql=mysqli_query($conn,"SELECT * FROM sendmessageinfo WHERE PostsID = $PostsID");
    $i = 0;
    while($result = mysqli_fetch_array($sql)){
             	$back[$i]['SendTime']= $result['SendTime'];
             	$back[$i]['SenderID'] = $result['SenderID'];
             	$back[$i]['SenderName'] = $result['SenderName'];
             	$back[$i]['PostsID']= $result['PostsID'];
             	$back[$i]['Message'] = $result['Message'];
	$i++;
    }
    echo(json_encode($back));
    mysqli_close($conn);
 ?>