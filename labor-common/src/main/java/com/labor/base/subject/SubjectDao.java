package com.labor.base.subject;

import java.util.HashMap;
import java.util.List;

import com.labor.common.constants.CommonConstants;
import com.labor.common.dao.DaoManager;
import com.labor.common.dao.ibatis.IbatisDao;

public class SubjectDao extends IbatisDao {

	public SubjectDao(DaoManager dm) {
		super(dm);
	}
	
	//subject
	public Integer insertSubject(HashMap hm) throws Exception{
		hm.put("active_status", CommonConstants.ACTIVE);
		return (Integer)getSession().insert("subject.insertSubject",hm);
	}
	public Integer updateSubject(HashMap hm) throws Exception{
		return (Integer)getSession().insert("subject.updateSubject",hm);
	}
	public HashMap getSubjectById(String id) throws Exception{
		return (HashMap)getSession().queryForObject("subject.getSubjectById",id);
	}
	public List listSubject(HashMap hm) throws Exception{
		return (List)getSession().queryForList("subject.listSubject",hm);
	}
}
