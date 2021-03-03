<?php
//用cmd运行该文件，找到相应目录，写上php server.php,
//并且要关掉防火墙(找不到不关防火墙的方法...) Yu:不用也行
// $host = '172.18.209.166';
$host = '172.18.209.166';
$port = 8889;
$servsocket = socket_create(AF_INET,SOCK_STREAM, SOL_TCP);
if(FALSE ===$servsocket){
    $errorcode = socket_last_error();
    fwrite(STDERR, "socket create fail: " . socket_strerror($errorcode));
    exit(-1);
}
if(!socket_bind($servsocket, $host,$port)){
    $errorcode = socket_last_error();
    fwrite(STDERR, "socket bind fail: " . socket_strerror($errorcode));
    exit(-1);
}
if(!socket_listen($servsocket,1000)){
    $errorcode = socket_last_error();
    fwrite(STDERR, "socket listen fail: " . socket_strerror($errorcode));
    exit(-1);
}
$read_socks = array();
$write_socks = array();
$except_socks = NULL;
$read_socks[] = $servsocket;
while(true){
    $tmp_reads = $read_socks;
    $tmp_writes = $write_socks;
    $count = socket_select($tmp_reads, $tmp_writes, $except_socks,NULL);
    foreach ($tmp_reads as $read){
        if($read == $servsocket){
            $connsock = socket_accept($servsocket);
            if($connsock){
                socket_getpeername($connsock, $address, $port);
                echo "client connect server: ip = $address, port = $port" . PHP_EOL;
                $read_socks[] = $connsock;
                $write_socks[] = $connsock;
            }
        }else{
            $data = socket_read($read, 1024);
            if($data === ""){
                foreach ($read_socks as $key => $val){
                    if($val == $read)
                        unset($read_socks[$key]);
                }
                foreach ($write_socks as $key => $val){
                    if($val == $read)
                        unset($write_socks[$key]);
                }
                socket_close($read);
                echo "client close" . PHP_EOL;
            }else{
//                 $arr = explode(" ", $data);
//                 socket_getpeername($read, $address, $port);
//                 echo "read from client # $address : $port # PostID: $arr[0], SenderName: $arr[1] Message: " .$data;
                if(in_array($read, $tmp_writes)){
                    foreach ($write_socks as $s){
                        if($s != $read){
                            socket_write($s, $data);
                        }
                    }
                }
            }
        }
    }
}
socket_close($servsocket);
?>