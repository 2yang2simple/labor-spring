
drop table IF EXISTS tbl_oss_objectheader;
drop table IF EXISTS tbl_oss_objectbody;



create table tbl_oss_objectheader
(
	oh_id				int NOT NULL AUTO_INCREMENT PRIMARY KEY,
	oh_url				varchar(200),
	oh_name				varchar(200),
	oh_filepath			varchar(100),
	oh_filename			varchar(100),
	oh_filetype			varchar(10),
	oh_filesize			int,
	ob_id				int,
	ob_path				varchar(200),
	ob_md5				varchar(100),
	active_status		varchar(1),
	data_description	varchar(1000),
	data_uuid			varchar(100),
	creation_date		datetime,
	created_by			varchar(40),
	last_update_date	datetime,
	last_updated_by 	varchar(40)
);
create table tbl_oss_objectbody
(
	ob_id				int NOT NULL AUTO_INCREMENT PRIMARY KEY,
	ob_path				varchar(200),
	ob_md5				varchar(100),
	active_status		varchar(1),
	data_description	varchar(1000),
	data_uuid			varchar(100),
	creation_date		datetime,
	created_by			varchar(40),
	last_update_date	datetime,
	last_updated_by 	varchar(40)
);
