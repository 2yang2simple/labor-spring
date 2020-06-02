/***
 * init mysql;
 set the time zone. 
SHOW VARIABLES LIKE '%time_zone%'
SET GLOBAL time_zone = '+8:00';
SET time_zone = '+8:00';
FLUSH PRIVILEGES;
my.ini/my.cnf
[mysqld]
default-time-zone='+8:00'
 *
 ALTER USER 'labor'@'localhost' IDENTIFIED WITH mysql_native_password BY '123456'; 
 
 * */ 
DROP TABLE IF EXISTS `hibernate_sequence`;

CREATE TABLE `hibernate_sequence` (
  `next_val` bigint(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;
INSERT INTO `hibernate_sequence` (`next_val`) VALUES('10000');