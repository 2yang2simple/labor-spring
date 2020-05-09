package com.labor.base.attachment;

import java.util.HashMap;
import java.util.List;

import com.labor.common.constants.CommonConstants;
import com.labor.common.dao.DaoManager;
import com.labor.common.dao.ibatis.IbatisDao;

public class AttachmentDao extends IbatisDao{
	
	public AttachmentDao (DaoManager dm){
		super(dm);
	}
	
	//attachment
	public Integer insertAttachment(HashMap hm) throws Exception{
		hm.put("active_status", CommonConstants.ACTIVE);
		return (Integer)getSession().insert("attachment.insertAttachment",hm);
	}
	public HashMap getAttachmentById(String id) throws Exception{
		return (HashMap)getSession().queryForObject("attachment.getAttachmentById",id);
	}
	public HashMap getAttachmentByName(String name) throws Exception{
		return (HashMap)getSession().queryForObject("attachment.getAttachmentByName",name);
	}
	
	

	
	
	
	
}
