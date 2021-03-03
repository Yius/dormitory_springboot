<?php
    include_once("connect.php");
$ID = $_POST['ID']; //APP post过来的学生ID
    $sql=mysqli_query($conn,"SELECT * FROM postsinfo where ID = '$ID' ");
    $i = 0;
    while($result = mysqli_fetch_array($sql)){
               $back[$i]['PostsID']= $result['PostsID'];
             	$back[$i]['PostsDate']= $result['PostsDate'];
             	$back[$i]['ID'] = $result['ID'];
             	$back[$i]['name']= $result['name'];
             	$back[$i]['postsTitle'] = $result['postsTitle'];
             	$back[$i]['postsContent']= $result['postsContent'];
               $back[$i]['LatestReplyTime']= $result['LatestReplyTime'];
	$i++;
    }
    echo(json_encode($back));
    mysqli_close($conn);
 ?>   