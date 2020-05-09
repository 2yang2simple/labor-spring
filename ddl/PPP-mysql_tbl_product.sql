drop table IF EXISTS tbl_product;
drop table IF EXISTS tbl_product_doc;
drop table IF EXISTS tbl_product_gallery;

create table tbl_product
(	
	prod_id 			int NOT NULL AUTO_INCREMENT PRIMARY KEY,
	prod_code			varchar(50),
	prod_itemno			varchar(50),
	prod_name			varchar(100),
	prod_namepinyin	    varchar(100),
	prod_price			float,
	prod_costprice		float,
	prod_marketprice	float,
	prod_currency		varchar(10),
	prod_size			varchar(50),
	prod_color			varchar(10),
	prod_status			varchar(2),
	
	parent_id			int,
	active_status		varchar(1),
	data_description	varchar(1000),
	data_uuid			varchar(100),
	creation_date		datetime,
	created_by			varchar(40),
	last_update_date	datetime,
	last_updated_by 	varchar(40)
);

create table tbl_product_doc
(	
	pd_id 			int NOT NULL AUTO_INCREMENT PRIMARY KEY,
	prod_id			int,
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
create table tbl_product_gallery
(	
	pg_id 			int NOT NULL AUTO_INCREMENT PRIMARY KEY,
	prod_id			int,
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