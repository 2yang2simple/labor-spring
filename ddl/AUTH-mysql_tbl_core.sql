

/*************************************/
/*setup the db tables of labor framework*/
drop table IF EXISTS tbl_core_fingerprint;
drop table IF EXISTS tbl_core_fingerprintonline;

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



create table tbl_core_fingerprint
(	
	fp_id 			bigint NOT NULL AUTO_INCREMENT PRIMARY KEY,
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
	fo_id			bigint NOT NULL AUTO_INCREMENT PRIMARY KEY,
	fp_id 			bigint,
	fp_type			varchar(10),
	fp_value		varchar(100),
	user_id			bigint,
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
	user_id				bigint NOT NULL PRIMARY KEY,
	user_cellphone		varchar(50),
	user_weixin			varchar(200),
	user_sno			varchar(100),
	user_name			varchar(100),
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
	pw_id			bigint NOT NULL AUTO_INCREMENT PRIMARY KEY,
	user_id			bigint,
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
	uf_id			bigint NOT NULL AUTO_INCREMENT PRIMARY KEY,
	uf_rememberme	varchar(1),
	user_id			bigint,
	fp_id			bigint,
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
	his_id			bigint NOT NULL AUTO_INCREMENT PRIMARY KEY,
	user_id			bigint,
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
	data_longid			bigint,
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
	detail_id		bigint NOT NULL AUTO_INCREMENT PRIMARY KEY,
	user_id			bigint,
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
	role_id				bigint NOT NULL AUTO_INCREMENT PRIMARY KEY,
	role_name			varchar(100) not null,
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
	id				bigint NOT NULL AUTO_INCREMENT PRIMARY KEY,
	user_id			bigint,
	role_id			bigint
);
create table tbl_core_rolepermission
(
	id				bigint NOT NULL AUTO_INCREMENT PRIMARY KEY,
	role_id			bigint,
	per_id			bigint
);
create table tbl_core_permission
(
	per_id			bigint NOT NULL AUTO_INCREMENT PRIMARY KEY,
	per_code		varchar(100) not null,
	per_type		varchar(100),
	parent_id		bigint,
	active_status		varchar(1),
	data_description	varchar(1000),
	data_uuid			varchar(100),
	creation_date		datetime,
	created_by			varchar(40),
	last_update_date	datetime,
	last_updated_by 	varchar(40)
);


