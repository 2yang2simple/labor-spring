select 
	    t1.user_id id,
	    t1.data_uuid uuid,
	    t1.user_cellphone cellPhone,
	    t1.user_weixin weixin,
	    t1.user_sno sno,
	    t1.user_name name,
	    t1.user_realname realName,
	    t1.user_realnameen realNameEn,
	    t1.user_email email,
	    t1.user_pwdmodify pwdmodify,
	    t1.active_status status,
	    t1.data_description description,
	    t1.creation_date creationDate,
	    t1.created_by createdBy,
	    t1.last_update_date lastUpdateDate,
	    t1.last_updated_by lastUpdatedBy
	from 
		tbl_core_user t1
