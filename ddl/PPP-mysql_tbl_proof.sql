
/**************************************
 * document gallery 
 */

drop table IF EXISTS tbl_tag;

drop table IF EXISTS tbl_doc;
drop table IF EXISTS tbl_doc_content;
drop table IF EXISTS tbl_doc_comment;
drop table IF EXISTS tbl_doc_tag;
drop table IF EXISTS tbl_doc_user;

drop table IF EXISTS tbl_gallery;
drop table IF EXISTS tbl_gallery_image;

/*gallery*/
create table tbl_gallery
(
	ga_id				int NOT NULL AUTO_INCREMENT PRIMARY KEY,
	ga_name				varchar(400),
	ga_namepinyin		varchar(1000),
	ga_caption			varchar(5000),

	active_status		varchar(1),
	data_description	varchar(1000),
	data_uuid			varchar(100),
	creation_date		datetime,
	created_by			varchar(40),
	last_update_date	datetime,
	last_updated_by 	varchar(40)
);
create table tbl_gallery_image
(
	gi_id				int NOT NULL AUTO_INCREMENT PRIMARY KEY,
	gi_name				varchar(300),
	gi_namepinyin		varchar(600),
	gi_caption			varchar(5000),
	gi_url				varchar(200),
	gi_order			int,
	ga_id				int,
	oh_id				int,
	oh_filename			varchar(100),
	active_status		varchar(1),
	data_description	varchar(1000),
	data_uuid			varchar(100),
	creation_date		datetime,
	created_by			varchar(40),
	last_update_date	datetime,
	last_updated_by 	varchar(40)
);

/*document*/
create table tbl_doc
(
	doc_id				int NOT NULL AUTO_INCREMENT PRIMARY KEY,
	doc_name			varchar(1000),
	doc_namepinyin		varchar(2000),
	doc_filepath		varchar(100),
	doc_filemd5			varchar(100),
	doc_status			varchar(1),
	active_status		varchar(1),
	data_description	varchar(1000),
	data_uuid			varchar(100),
	creation_date		datetime,
	created_by			varchar(40),
	last_update_date	datetime,
	last_updated_by 	varchar(40)
);
create table tbl_doc_content
(
	dcon_id				int NOT NULL AUTO_INCREMENT PRIMARY KEY,
	doc_id				int,
	dcon_html			longtext,
	dcon_text			longtext,
	dcon_md5			varchar(100),
	active_status		varchar(1),
	data_description	varchar(1000),
	data_uuid			varchar(100),
	creation_date		datetime,
	created_by			varchar(40),
	last_update_date	datetime,
	last_updated_by 	varchar(40)
);

create table tbl_doc_comment
(
	dcmt_id				int NOT NULL AUTO_INCREMENT PRIMARY KEY,
	doc_id				int,
	dcmt_html			longtext,
	dcmt_text			longtext,
	dcmt_creator		varchar(200),
	active_status		varchar(1),
	data_description	varchar(1000),
	data_uuid			varchar(100),
	creation_date		datetime,
	created_by			varchar(40),
	last_update_date	datetime,
	last_updated_by 	varchar(40)
);

create table tbl_doc_tag
(
	dtag_id				int NOT NULL AUTO_INCREMENT PRIMARY KEY,
	doc_id				int,
	tag_id				int,
	tag_type			varchar(10),
	tag_name			varchar(1000),
	active_status		varchar(1),
	data_description	varchar(1000),
	data_uuid			varchar(100),
	creation_date		datetime,
	created_by			varchar(40),
	last_update_date	datetime,
	last_updated_by 	varchar(40)
);
create table tbl_doc_user
(
	dusr_id				int NOT NULL AUTO_INCREMENT PRIMARY KEY,
	doc_id				int,
	user_id				bigint,
	user_name			varchar(100),
	active_status		varchar(1),
	data_description	varchar(1000),
	data_uuid			varchar(100),
	creation_date		datetime,
	created_by			varchar(40),
	last_update_date	datetime,
	last_updated_by 	varchar(40)
);

create table tbl_tag
(
	tag_id				int NOT NULL AUTO_INCREMENT PRIMARY KEY,
	tag_type			varchar(10),
	tag_name			varchar(100),
	tag_namepinyin		varchar(200),
	parent_id			int,
	active_status		varchar(1),
	data_description	varchar(1000),
	data_uuid			varchar(100),
	creation_date		datetime,
	created_by			varchar(40),
	last_update_date	datetime,
	last_updated_by 	varchar(40)
);
