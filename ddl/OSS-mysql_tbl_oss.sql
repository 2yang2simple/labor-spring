DROP TABLE IF EXISTS `hibernate_sequence`;

CREATE TABLE `hibernate_sequence` (
  `next_val` bigint(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;
INSERT INTO `hibernate_sequence` (`next_val`) VALUES('10000');

drop table IF EXISTS tbl_oss_objectheader;
drop table IF EXISTS tbl_oss_objectbody;

create table tbl_oss_objectheader
(
	oh_id				bigint NOT NULL AUTO_INCREMENT PRIMARY KEY,
	oh_url				varchar(500),
	oh_name				varchar(200),
	oh_filepath			varchar(100),
	oh_filename			varchar(100),
	oh_filetype			varchar(10),
	oh_filesize			bigint,
	ob_id				bigint,
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
	ob_id				bigint NOT NULL AUTO_INCREMENT PRIMARY KEY,
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
