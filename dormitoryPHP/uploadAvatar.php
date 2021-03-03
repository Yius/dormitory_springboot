<?php
    if (empty($_FILES)) {
        $back['status']="-25";
        $back['info']="upload file is empty!";
    } else {
        $dirPath = 'userAvatar/';//设置文件保存的目录
        if (!is_dir($dirPath)) {
            //目录不存在则创建目录
            @mkdir($dirPath);
        }
        foreach ($_FILES as $key => $value){
            $tmp = $value['name'];//获取上传文件名
            $tmpName = $value['tmp_name'];//临时文件路径
            //上传的文件会被保存到php临时目录，调用函数将文件复制到指定目录
            if (move_uploaded_file($tmpName,$dirPath.$tmp)) {
                $back['status']="19";
                $back['info']="upload file succeed!";
            }else{
                $back['status']="-26";
                $back['info']="upload file fails!";
            }
        }
    }
    echo (json_encode($back));
?>