drop table IF EXISTS tbl_project;
drop table IF EXISTS tbl_project_doc;

create table tbl_project
(	
	proj_id 			int NOT NULL AUTO_INCREMENT PRIMARY KEY,
	proj_name			varchar(100),
	proj_namepinyin		varchar(300),
	proj_code			varchar(50),
	proj_customer		varchar(100),
	proj_customercode	varchar(50),
	proj_supplier		varchar(100),
	proj_suppliercode	varchar(50),
	proj_deliverydate	datetime,
	proj_amount			float,
	proj_currency		varchar(10),
	proj_manager		varchar(50),
	proj_manageruserid	int,
	proj_status			varchar(2),
	
	parent_id			int,
	active_status		varchar(1),
	data_description	varchar(1000),
	data_uuid			varchar(100),
	creation_date		datetime,
	created_by			varchar(40),
	last_update_date	datetime,
	last_updated_by 	varchar(40)
);

create table tbl_project_doc
(	
	pd_id 			int NOT NULL AUTO_INCREMENT PRIMARY KEY,
	proj_id			int,
	doc_id			int,
	pd_type			varchar(10),
	pd_status		varchar(2),
	
	active_status		varchar(1),
	data_description	varchar(1000),
	data_uuid			varchar(100),
	creation_date		datetime,
	created_by			varchar(40),
	last_update_date	datetime,
	last_updated_by 	varchar(40)
);
create table tbl_project_gallery
(	
	pg_id 			int NOT NULL AUTO_INCREMENT PRIMARY KEY,
	proj_id			int,
	ga_id			int,
	pg_type			varchar(10),
	pg_status		varchar(2),
	
	active_status		varchar(1),
	data_description	varchar(1000),
	data_uuid			varchar(100),
	creation_date		datetime,
	created_by			varchar(40),
	last_update_date	datetime,
	last_updated_by 	varchar(40)
);