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

/*************************************/
/*setup the db tables of labor framework*/
drop table IF EXISTS tbl_core_fingerprint;
drop table IF EXISTS tbl_core_fingerprintonline;
drop table IF EXISTS tbl_core_sysconfig;

drop table IF EXISTS tbl_core_user;
drop table IF EXISTS tbl_core_userhistory;
drop table IF EXISTS tbl_core_userfingerprint;
drop table IF EXISTS tbl_core_userpassword;
drop table IF EXISTS tbl_core_userdetail;

drop table IF EXISTS tbl_core_userrole;
drop table IF EXISTS tbl_core_role;
drop table IF EXISTS tbl_core_rolepermission;
drop table IF EXISTS tbl_core_permission;


/*************************************/
/***ALTER tbl_core_user  ADD user_pwdmodify  VARCHAR(100) ;*/
/*mysql*/ 

create table tbl_core_sysconfig
(
	sc_id			int NOT NULL AUTO_INCREMENT PRIMARY KEY,
	sc_key			varchar(100) not null,
	sc_value		varchar(300),
	active_status		varchar(1),
	data_description	varchar(1000),
	data_uuid			varchar(100),
	creation_date		datetime,
	created_by			varchar(40),
	last_update_date	datetime,
	last_updated_by 	varchar(40)
);

create table tbl_core_fingerprint
(	
	fp_id 			int NOT NULL AUTO_INCREMENT PRIMARY KEY,
	fp_type			varchar(10),
	fp_value		varchar(1000),
	active_status		varchar(1),
	data_description	varchar(1000),
	data_uuid			varchar(100),
	creation_date		datetime,
	created_by			varchar(40),
	last_update_date	datetime,
	last_updated_by 	varchar(40)
);

create table tbl_core_fingerprintonline
(	
	fo_id			int NOT NULL AUTO_INCREMENT PRIMARY KEY,
	fp_id 			int,
	fp_type			varchar(10),
	fp_value		varchar(100),
	user_id			int,
	user_name		varchar(50),
	auth_type		varchar(10),
	auth_value		varchar(1000),
	auth_code		varchar(1000),
	session_id		varchar(50),
	active_status		varchar(1),
	data_description	varchar(1000),
	data_uuid			varchar(100),
	creation_date		datetime,
	created_by			varchar(40),
	last_update_date	datetime,
	last_updated_by 	varchar(40)
);

create table tbl_core_user
(
	user_id				int NOT NULL AUTO_INCREMENT PRIMARY KEY,
	user_cellphone		varchar(50),
	user_weixin			varchar(200),
	user_sno			varchar(50),
	user_name			varchar(50),
	user_realname		varchar(100),
	user_realnameen			varchar(100),
	user_pwdmodify 			varchar(1000),
	user_email				varchar(100),
	user_googlesecretkey 	varchar(1000),
	active_status		varchar(1),
	data_description	varchar(1000),
	data_uuid			varchar(100),
	creation_date		datetime,
	created_by			varchar(40),
	last_update_date	datetime,
	last_updated_by 	varchar(40)
);

create table tbl_core_userpassword
(
	pw_id			int NOT NULL AUTO_INCREMENT PRIMARY KEY,
	user_id			int,
	user_pwd		varchar(100),
	user_pwdmd5	    varchar(100),
	active_status		varchar(1),
	data_description	varchar(1000),
	data_uuid			varchar(100),
	creation_date		datetime,
	created_by			varchar(40),
	last_update_date	datetime,
	last_updated_by 	varchar(40)
);

create table tbl_core_userfingerprint
(
	uf_id			int NOT NULL AUTO_INCREMENT PRIMARY KEY,
	uf_rememberme	varchar(1),
	user_id			int,
	fp_id			int,
	fp_type			varchar(10),
	fp_value		varchar(1000),
	active_status		varchar(1),
	data_description	varchar(1000),
	data_uuid			varchar(100),
	creation_date		datetime,
	created_by			varchar(40),
	last_update_date	datetime,
	last_updated_by 	varchar(40)
);


/**
 * save user modified history, ip, token, etc;
 */

create table tbl_core_userhistory
(
	his_id			int NOT NULL AUTO_INCREMENT PRIMARY KEY,
	user_id			int,
	user_cellphone	varchar(50),
	user_weixin		varchar(200),
	user_sno		varchar(50),
	user_name		varchar(50),
	user_realname	varchar(100),
	user_realnameen	varchar(100),
	user_email		varchar(100),
	user_clienttk	varchar(1000),
	user_clientip	varchar(200),
	active_status		varchar(1),
	data_description	varchar(1000),
	data_uuid			varchar(100),
	creation_date		datetime,
	created_by			varchar(40),
	last_update_date	datetime,
	last_updated_by 	varchar(40)
);

/*
 * save the user detail; like hobby address etc;
 */
create table tbl_core_userdetail
(
	detail_id		int NOT NULL AUTO_INCREMENT PRIMARY KEY,
	user_id			int,
	user_address	varchar(4000),
	user_hobby1		varchar(4000),
	user_hobby2		varchar(4000),
	user_hobby3		varchar(4000),
	active_status		varchar(1),
	data_description	varchar(1000),
	data_uuid			varchar(100),
	creation_date		datetime,
	created_by			varchar(40),
	last_update_date	datetime,
	last_updated_by 	varchar(40)
);

/***********
 * acl shiro
 */
create table tbl_core_role
(
	role_id			int NOT NULL AUTO_INCREMENT PRIMARY KEY,
	role_name		varchar(100) not null,
	active_status		varchar(1),
	data_description	varchar(1000),
	data_uuid			varchar(100),
	creation_date		datetime,
	created_by			varchar(40),
	last_update_date	datetime,
	last_updated_by 	varchar(40)
);

create table tbl_core_userrole
(
	id			int NOT NULL AUTO_INCREMENT PRIMARY KEY,
	user_id			int,
	role_id			int
);
create table tbl_core_rolepermission
(
	id			int NOT NULL AUTO_INCREMENT PRIMARY KEY,
	role_id			int,
	per_id			int
);
create table tbl_core_permission
(
	per_id			int NOT NULL AUTO_INCREMENT PRIMARY KEY,
	per_code		varchar(100) not null,
	parent_id		int,
	active_status		varchar(1),
	data_description	varchar(1000),
	data_uuid			varchar(100),
	creation_date		datetime,
	created_by			varchar(40),
	last_update_date	datetime,
	last_updated_by 	varchar(40)
);


