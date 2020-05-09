package com.labor.base.cfg;

import com.labor.base.acl.AclConstants;
import com.labor.common.constants.CommonConstants;
import com.labor.common.dao.DaoManager;
import com.labor.common.dao.ibatis.IbatisDao;
import com.labor.common.util.StringUtil;

import java.util.List;

public class DDVDao  extends IbatisDao{

	public DDVDao (DaoManager dm){
		super(dm);
	}
	
	public List getDDTypeList() throws Exception{
		return (List)getSession().queryForList("cfg.getDDTypeList",null);
	}
	public String getDDValue(String ddid) throws Exception{
		return (String)getSession().queryForObject("cfg.getDDValue",ddid);
	}
	public Integer getDDId(String ddvalue) throws Exception{
		return (Integer)getSession().queryForObject("cfg.getDDId",ddvalue);
	}
	public Integer countDDV(DDValueVO pvo) throws Exception{
		return (Integer)getSession().queryForObject("cfg.countDDV",pvo);
	}
	public DDValueVO getDDV(String ddid) throws Exception{
		return (DDValueVO)getSession().queryForObject("cfg.getDDV",ddid);
	}
	public List selectDDV(DDValueVO pvo) throws Exception{	
		return getSession().queryForList("cfg.selectDDV",pvo);
	}
	public Integer insertDDV(DDValueVO pvo) throws Exception{
		//must insert the active users.
		pvo.setDdstatus(CommonConstants.ACTIVE);
		return (Integer)getSession().insert("cfg.insertDDV",pvo);
	}
	public int updateDDV(DDValueVO pvo) throws Exception{
		return getSession().update("cfg.updateDDV",pvo);
	}
}
