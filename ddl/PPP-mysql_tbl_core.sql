


drop table IF EXISTS tbl_core_richtext;
drop table IF EXISTS tbl_core_dictionary;


/***
 * article content; rich text etc.
 * ALTER tbl_core_richtext MODIFY rt_varchar  longtext ; 
 * alter table tbl_core_richtext change rt_varchar rt_text longtext
 * con_varchar = key words
 */
create table tbl_core_richtext
(
	rt_id			int NOT NULL AUTO_INCREMENT PRIMARY KEY,
	rt_name			varchar(100),
	rt_url			varchar(100),
	rt_html			longtext,
	rt_text			longtext,
	active_status		varchar(1),
	data_description	varchar(1000),
	data_uuid			varchar(100),
	creation_date		datetime,
	created_by			varchar(40),
	last_update_date	datetime,
	last_updated_by 	varchar(40)
);

create table tbl_core_dictionary
(
	dc_id				int NOT NULL AUTO_INCREMENT PRIMARY KEY,
	dc_code				varchar(100),
	dc_name				varchar(100),
	dc_value1			varchar(100),
	dc_value2			varchar(100),
	dc_value3			varchar(100),
	dc_value4			varchar(100),
	dc_value5			varchar(100),
	dc_order			int,
	parent_id			int,
	active_status		varchar(1),
	data_description	varchar(1000),
	data_uuid			varchar(100),
	creation_date		datetime,
	created_by			varchar(40),
	last_update_date	datetime,
	last_updated_by 	varchar(40)
);