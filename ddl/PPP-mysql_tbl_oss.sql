
drop table IF EXISTS tbl_oss_attachment;
drop table IF EXISTS tbl_oss_objectmap;
drop table IF EXISTS tbl_oss_objectheader;
drop table IF EXISTS tbl_oss_objectbody;



/****
 * 
 * status: active or inactive (deleted)
 * atta_name: the original name when attachment is uploading;
 * atta_filepath
 * +atta_filename
 * +atta_filetype:	
 *  	this is the only way to read the attachment,the filename is not the original name; 
 * 		like: image/20190415/23243213532532.jpg
 * 
 */
create table tbl_oss_attachment
(
	atta_id			int NOT NULL AUTO_INCREMENT PRIMARY KEY,
	atta_url		varchar(200),
	atta_name		varchar(200),
	atta_filepath	varchar(100),
	atta_filename	varchar(100),
	atta_filetype	varchar(10),
	atta_filesize	int,
	active_status		varchar(1),
	data_description	varchar(1000),
	data_uuid			varchar(100),
	creation_date		datetime,
	created_by			varchar(40),
	last_update_date	datetime,
	last_updated_by 	varchar(40)
);



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
create table tbl_oss_objectmap
(
	om_id				int NOT NULL AUTO_INCREMENT PRIMARY KEY,
	oh_id				int,
	om_typeid			int,
	om_type				varchar(10),
	active_status		varchar(1),
	data_description	varchar(1000),
	data_uuid			varchar(100),
	creation_date		datetime,
	created_by			varchar(40),
	last_update_date	datetime,
	last_updated_by 	varchar(40)
);