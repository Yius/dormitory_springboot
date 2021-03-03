-- phpMyAdmin SQL Dump
-- version 4.8.5
-- https://www.phpmyadmin.net/
--
-- 主机： localhost
-- 生成日期： 2020-04-05 20:49:51
-- 服务器版本： 5.6.47-log
-- PHP 版本： 7.3.5

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET AUTOCOMMIT = 0;
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- 数据库： `dormitory`
--

-- --------------------------------------------------------

--
-- 表的结构 `announcement`
--

CREATE TABLE `announcement` (
  `ID` int(11) NOT NULL,
  `Atime` datetime NOT NULL,
  `houseparentID` varchar(12) NOT NULL,
  `content` text NOT NULL,
  `title` varchar(25) NOT NULL,
  `govern` varchar(12) NOT NULL,
  `houseparentName` varchar(10) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- 转存表中的数据 `announcement`
--

INSERT INTO `announcement` (`ID`, `Atime`, `houseparentID`, `content`, `title`, `govern`, `houseparentName`) VALUES
(11, '2020-04-02 19:44:58', '20001', '名单如下:\nA101、A224、A404、A666\n希望以上宿舍再接再厉！', 'A栋2月卫生标兵宿舍表彰', 'A', '李月'),
(13, '2020-04-02 20:28:20', '12', '今天下午，A224林同学在楼道捡到五元，并交到了宿管处，特此表扬林同学！有遗失的同学请尽快来认领！', 'A栋宿舍好人好事表彰', 'A', '张红专'),
(14, '2020-04-02 20:34:15', '10000', '各位同学，四月份我们将开展全体宿舍卫生检查，请大家提前搞好卫生！谢谢合作！', '关于四月宿舍卫生检查事项', 'A', '李建国'),
(16, '2020-04-02 21:01:14', '1', '在2019年4月时，经过一整月的考察，发现以下宿舍卫生突出，在此特别表彰:\nA224，A335，A446\n希望这些宿舍再接再厉！', '2019年4月优秀宿舍表彰', 'A', '王大海'),
(17, '2020-04-02 21:02:53', '1', '在2019年5月时，经过一整月的考察，发现以下宿舍卫生突出，在此特别表彰:\r\nA224，A333，A555\r\n希望这些宿舍再接再厉！', '2019年5月优秀宿舍表彰', 'A', '王大海'),
(18, '2020-04-02 21:03:49', '1', '在2019年6月时，经过一整月的考察，发现以下宿舍卫生突出，在此特别表彰:\r\nA224,A225,A226\r\n希望这些宿舍再接再厉！', '2019年6月优秀宿舍表彰', 'A', '王大海'),
(19, '2020-04-02 21:04:52', '1', '在2019年7月时，经过一整月的考察，发现以下宿舍卫生突出，在此特别表彰:\r\nA101,A224,A665\r\n希望这些宿舍再接再厉！', '2019年7月优秀宿舍表彰', 'A', '王大海'),
(25, '2020-04-02 23:13:57', '1', '无', '标题', 'A', '王大海');

-- --------------------------------------------------------

--
-- 表的结构 `departinfo`
--

CREATE TABLE `departinfo` (
  `departID` int(10) UNSIGNED NOT NULL,
  `registerDate` datetime NOT NULL,
  `ID` varchar(12) NOT NULL,
  `dormID` varchar(10) NOT NULL,
  `departCause` text NOT NULL,
  `departTime` datetime NOT NULL,
  `backTime` datetime NOT NULL,
  `contact` varchar(12) NOT NULL,
  `belong` varchar(12) NOT NULL,
  `name` varchar(10) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- 转存表中的数据 `departinfo`
--

INSERT INTO `departinfo` (`departID`, `registerDate`, `ID`, `dormID`, `departCause`, `departTime`, `backTime`, `contact`, `belong`, `name`) VALUES
(5, '2020-04-02 19:24:18', '1', 'A101', '肚子疼', '2020-01-15 19:23:00', '2020-03-19 23:28:00', '13412341234', 'A', '小明'),
(6, '2020-04-02 19:40:00', '100', 'A101', '家里有事', '2020-04-01 19:39:00', '2020-04-02 23:39:00', '13312341234', 'A', '小刚'),
(8, '2020-04-02 21:56:48', '10002', 'A105', '生病去看医生', '2020-04-02 21:56:00', '2020-04-03 21:56:00', '13212345678', 'A', '张三');

-- --------------------------------------------------------

--
-- 表的结构 `postsinfo`
--

CREATE TABLE `postsinfo` (
  `PostsID` int(10) UNSIGNED NOT NULL,
  `LatestReplyTime` datetime NOT NULL,
  `PostsDate` datetime NOT NULL,
  `ID` varchar(12) NOT NULL,
  `name` varchar(10) NOT NULL,
  `postsTitle` text NOT NULL,
  `postsContent` text NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- 转存表中的数据 `postsinfo`
--

INSERT INTO `postsinfo` (`PostsID`, `LatestReplyTime`, `PostsDate`, `ID`, `name`, `postsTitle`, `postsContent`) VALUES
(32, '2020-04-02 19:26:08', '2020-04-02 19:26:08', '1', '小明', '疫情什么时候才过去啊', '想念无忧无虑的日子'),
(33, '2020-04-02 19:32:16', '2020-04-02 19:32:16', '10', '小红', '熹园约饭', '有姐妹一起来吃饭吗？'),
(34, '2020-04-02 19:40:35', '2020-04-02 19:40:35', '100', '小刚', '下午篮球场', '老铁就等你了'),
(35, '2020-04-02 19:47:51', '2020-04-02 19:47:51', '20002', '王五', '等下有人乐跑吗', '8点半操场乐跑互助[滑稽]'),
(36, '2020-04-02 20:08:00', '2020-04-02 20:08:00', '1', '小明', '什么时候开学啊', '在家都快呆坏了?'),
(37, '2020-04-02 20:37:39', '2020-04-02 20:37:39', '666', '小爱', '新人报道！', '大家好，我是小爱同学'),
(38, '2020-04-02 21:18:15', '2020-04-02 21:18:15', '10001', '张三', '请问有认识17级软件工程的林同学的吗', ''),
(39, '2020-04-03 14:15:00', '2020-04-02 21:46:05', '20172005161', '李三四', '大家好', '我是新同学小小'),
(46, '2020-04-03 14:13:11', '2020-04-03 14:12:49', '10002', '张三', '等下有人去打球吗', '5点半左右');

-- --------------------------------------------------------

--
-- 表的结构 `repairinfo`
--

CREATE TABLE `repairinfo` (
  `ApplyID` int(10) UNSIGNED NOT NULL,
  `ApplyDate` datetime NOT NULL,
  `dormID` varchar(10) NOT NULL,
  `RepairName` text,
  `DamageCause` text,
  `Details` text,
  `Contact` varchar(11) NOT NULL,
  `OtherRemarks` text,
  `belong` varchar(12) NOT NULL,
  `Status` smallint(1) NOT NULL DEFAULT '0'
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- 转存表中的数据 `repairinfo`
--

INSERT INTO `repairinfo` (`ApplyID`, `ApplyDate`, `dormID`, `RepairName`, `DamageCause`, `Details`, `Contact`, `OtherRemarks`, `belong`, `Status`) VALUES
(24, '2020-04-02 19:23:32', 'A101', '灯管', '自然损坏', '昨晚突然暗了', '13412341234', '无', 'A', 0),
(25, '2020-04-02 19:31:21', 'A000', '门', '老鼠咬烂了', '昨晚被老鼠咬烂了', '15012341234', '', 'A', 0),
(26, '2020-04-02 19:42:30', 'A101', '水管', '水管炸了', '昨晚洗澡的时候水压太大，就炸了', '16012341234', '记得顺便看一看另一条水管', 'A', 0),
(27, '2020-04-02 20:20:30', 'A133', '柜子', '不小心踢坏了', '打扫卫生时不小心踢坏了', '17012341234', '无', 'A', 0),
(31, '2020-04-02 22:54:24', 'A105', '日关灯', '灯管烧了', '靠近厕所那边的', '13212345678', '无', 'A', 1);

-- --------------------------------------------------------

--
-- 表的结构 `sendmessageinfo`
--

CREATE TABLE `sendmessageinfo` (
  `SendTime` datetime NOT NULL,
  `SenderID` varchar(12) CHARACTER SET utf8 NOT NULL,
  `SenderName` varchar(10) CHARACTER SET utf8 NOT NULL,
  `PostsID` int(11) NOT NULL,
  `Message` text
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- 转存表中的数据 `sendmessageinfo`
--

INSERT INTO `sendmessageinfo` (`SendTime`, `SenderID`, `SenderName`, `PostsID`, `Message`) VALUES
('2020-04-03 14:12:54', '10010', '林毅飞', 46, '我去'),
('2020-04-03 14:13:01', '10010', '林毅飞', 46, '带我一个'),
('2020-04-03 14:13:08', '10002', '张三', 46, '好啊'),
('2020-04-03 14:13:11', '10002', '张三', 46, '👌'),
('2020-04-03 14:15:00', '10001', '张五', 39, '你好啊');

-- --------------------------------------------------------

--
-- 表的结构 `signed`
--

CREATE TABLE `signed` (
  `SID` varchar(12) NOT NULL,
  `recordID` int(11) NOT NULL,
  `signedtime` datetime NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- 转存表中的数据 `signed`
--

INSERT INTO `signed` (`SID`, `recordID`, `signedtime`) VALUES
('10002', 33, '2020-04-02 23:17:54');

-- --------------------------------------------------------

--
-- 表的结构 `signrecord`
--

CREATE TABLE `signrecord` (
  `ID` int(11) NOT NULL,
  `Rtime` datetime NOT NULL,
  `houseparentID` varchar(12) NOT NULL,
  `nums` smallint(6) NOT NULL DEFAULT '0',
  `title` varchar(30) NOT NULL,
  `govern` varchar(12) NOT NULL,
  `totalnums` smallint(6) NOT NULL,
  `latitude` double NOT NULL,
  `longitude` double NOT NULL,
  `detailAddress` varchar(50) DEFAULT NULL,
  `houseparentName` varchar(10) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- 转存表中的数据 `signrecord`
--

INSERT INTO `signrecord` (`ID`, `Rtime`, `houseparentID`, `nums`, `title`, `govern`, `totalnums`, `latitude`, `longitude`, `detailAddress`, `houseparentName`) VALUES
(33, '2020-04-02 23:14:46', '1', 2, '签到', 'A', 7, 23.287762, 116.363803, '广东省汕头市潮南区新华路10号靠近华里西村', '王大海');

-- --------------------------------------------------------

--
-- 表的结构 `stayinfo`
--

CREATE TABLE `stayinfo` (
  `stayID` int(10) UNSIGNED NOT NULL,
  `registerDate` datetime NOT NULL,
  `ID` varchar(12) NOT NULL,
  `dormID` varchar(10) NOT NULL,
  `startDate` datetime NOT NULL,
  `endDate` datetime NOT NULL,
  `contact` varchar(12) NOT NULL,
  `belong` varchar(12) NOT NULL,
  `name` varchar(10) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- 转存表中的数据 `stayinfo`
--

INSERT INTO `stayinfo` (`stayID`, `registerDate`, `ID`, `dormID`, `startDate`, `endDate`, `contact`, `belong`, `name`) VALUES
(5, '2020-04-02 19:24:45', '1', 'A101', '2020-02-12 19:24:00', '2020-03-26 00:24:00', '13412341234', 'A', '小明'),
(6, '2020-04-02 19:31:43', '10', 'A000', '2020-02-05 19:31:00', '2020-04-01 19:31:00', '15012341234', 'A', '小红'),
(7, '2020-04-02 20:21:33', '20001', 'A133', '2020-04-02 14:21:00', '2020-04-02 23:21:00', '17012341234', 'A', '李四'),
(9, '2020-04-02 21:56:25', '10002', 'A105', '2020-04-02 21:56:00', '2020-04-03 21:56:00', '13212345678', 'A', '张三');

-- --------------------------------------------------------

--
-- 表的结构 `userinfo`
--

CREATE TABLE `userinfo` (
  `ID` varchar(12) NOT NULL,
  `name` varchar(10) NOT NULL,
  `dormID` varchar(10) NOT NULL,
  `phone` varchar(12) DEFAULT NULL,
  `password` varchar(11) NOT NULL,
  `nickname` varchar(10) DEFAULT NULL,
  `belong` varchar(12) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- 转存表中的数据 `userinfo`
--

INSERT INTO `userinfo` (`ID`, `name`, `dormID`, `phone`, `password`, `nickname`, `belong`) VALUES
('1', '小明', 'A101', '121321', '123456', 'HAA', 'A'),
('10', '小红', 'A000', '3131351', '123456', 'HAA', 'A'),
('100', '小刚', 'A101', '5646451', '123456', 'AH', 'A'),
('10001', '张五', 'A101', '13212345678', '123456', '张五', 'A'),
('10002', '张三', 'A105', '13212345678', '123456', '取名字太难了', 'A'),
('10010', '林毅飞', 'A696', '15012351235', '123456', '精神小伙', 'B'),
('20001', '李四', 'A133', '1231456655', '123456', 'mike', 'A'),
('20002', '王五', 'B333', '123456789', '123456', 'didi', 'B'),
('20003', '赵六', 'C414', '134654645', '123456', 'Amy', 'C'),
('20172005161', '李三四', '321', '18924565642', '123456', '小小', 'A'),
('321', '阿Q', 'B000', '1236547890', '123456', 'dada', 'B'),
('666', '小爱', 'C000', '1472583690', '123456', 'OK', 'C');

-- --------------------------------------------------------

--
-- 表的结构 `userinfoh`
--

CREATE TABLE `userinfoh` (
  `ID` varchar(12) NOT NULL,
  `name` varchar(10) NOT NULL,
  `govern` varchar(12) NOT NULL,
  `phone` varchar(12) NOT NULL,
  `password` varchar(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- 转存表中的数据 `userinfoh`
--

INSERT INTO `userinfoh` (`ID`, `name`, `govern`, `phone`, `password`) VALUES
('1', '王大海', 'A', '111111111', '123456'),
('10000', '李建国', 'A', '123456', '123456'),
('12', '张红专', 'A', '12312', '123456'),
('2', '李乐', 'B', '1346516165', '123456'),
('20001', '李月', 'A', '132132465', '123456'),
('20003', '赵有恒', 'C', '1654649894', '123456');

-- --------------------------------------------------------

--
-- 表的结构 `waterandelectricity`
--

CREATE TABLE `waterandelectricity` (
  `dorm` varchar(12) CHARACTER SET utf8 NOT NULL,
  `waterUrl` text CHARACTER SET utf8 NOT NULL,
  `electricityUrl` text CHARACTER SET utf8 NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- 转存表中的数据 `waterandelectricity`
--

INSERT INTO `waterandelectricity` (`dorm`, `waterUrl`, `electricityUrl`) VALUES
('A', 'https://ssp.scnu.edu.cn', ''),
('B', 'https://ssp.scnu.edu.cn', '');

--
-- 转储表的索引
--

--
-- 表的索引 `announcement`
--
ALTER TABLE `announcement`
  ADD PRIMARY KEY (`ID`),
  ADD KEY `houseparentID` (`houseparentID`);

--
-- 表的索引 `departinfo`
--
ALTER TABLE `departinfo`
  ADD PRIMARY KEY (`departID`);

--
-- 表的索引 `postsinfo`
--
ALTER TABLE `postsinfo`
  ADD PRIMARY KEY (`PostsID`);

--
-- 表的索引 `repairinfo`
--
ALTER TABLE `repairinfo`
  ADD PRIMARY KEY (`ApplyID`);

--
-- 表的索引 `sendmessageinfo`
--
ALTER TABLE `sendmessageinfo`
  ADD PRIMARY KEY (`SendTime`,`SenderID`,`PostsID`);

--
-- 表的索引 `signed`
--
ALTER TABLE `signed`
  ADD PRIMARY KEY (`SID`,`recordID`),
  ADD KEY `recordID` (`recordID`);

--
-- 表的索引 `signrecord`
--
ALTER TABLE `signrecord`
  ADD PRIMARY KEY (`ID`),
  ADD KEY `houseparentID` (`houseparentID`);

--
-- 表的索引 `stayinfo`
--
ALTER TABLE `stayinfo`
  ADD PRIMARY KEY (`stayID`);

--
-- 表的索引 `userinfo`
--
ALTER TABLE `userinfo`
  ADD PRIMARY KEY (`ID`);

--
-- 表的索引 `userinfoh`
--
ALTER TABLE `userinfoh`
  ADD PRIMARY KEY (`ID`);

--
-- 表的索引 `waterandelectricity`
--
ALTER TABLE `waterandelectricity`
  ADD PRIMARY KEY (`dorm`);

--
-- 在导出的表使用AUTO_INCREMENT
--

--
-- 使用表AUTO_INCREMENT `announcement`
--
ALTER TABLE `announcement`
  MODIFY `ID` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=26;

--
-- 使用表AUTO_INCREMENT `departinfo`
--
ALTER TABLE `departinfo`
  MODIFY `departID` int(10) UNSIGNED NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=9;

--
-- 使用表AUTO_INCREMENT `postsinfo`
--
ALTER TABLE `postsinfo`
  MODIFY `PostsID` int(10) UNSIGNED NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=47;

--
-- 使用表AUTO_INCREMENT `repairinfo`
--
ALTER TABLE `repairinfo`
  MODIFY `ApplyID` int(10) UNSIGNED NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=32;

--
-- 使用表AUTO_INCREMENT `signrecord`
--
ALTER TABLE `signrecord`
  MODIFY `ID` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=34;

--
-- 使用表AUTO_INCREMENT `stayinfo`
--
ALTER TABLE `stayinfo`
  MODIFY `stayID` int(10) UNSIGNED NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=10;

--
-- 限制导出的表
--

--
-- 限制表 `announcement`
--
ALTER TABLE `announcement`
  ADD CONSTRAINT `announcement_ibfk_1` FOREIGN KEY (`houseparentID`) REFERENCES `userinfoh` (`ID`);

--
-- 限制表 `signrecord`
--
ALTER TABLE `signrecord`
  ADD CONSTRAINT `signrecord_ibfk_1` FOREIGN KEY (`houseparentID`) REFERENCES `userinfoh` (`ID`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
