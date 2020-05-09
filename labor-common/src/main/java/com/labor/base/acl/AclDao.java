package com.labor.base.acl;

import com.labor.common.constants.CommonConstants;
import com.labor.common.dao.DaoManager;
import com.labor.common.dao.ibatis.IbatisDao;
import com.labor.common.util.StringUtil;

import java.util.List;

public class AclDao  extends IbatisDao{
		
	public AclDao (DaoManager dm){
		super(dm);
	}
	public Integer getRoleid(String rolename) throws Exception{
		return (Integer)getSession().queryForObject("acl.getRoleid",rolename);
	}
	public List selectRole(RoleVO pvo) throws Exception{
		return getSession().queryForList("acl.selectRole",pvo);
	}
	public Integer countUser(UserVO pvo) throws Exception{
		return (Integer)getSession().queryForObject("acl.countUser",pvo);
	}
	public List selectUser(UserVO pvo) throws Exception{
		return getSession().queryForList("acl.selectUser",pvo);
	}
	public Integer insertUser(UserVO pvo) throws Exception{
		//must insert the active users.
		pvo.setUserstatus(CommonConstants.ACTIVE);
		return (Integer)getSession().insert("acl.insertUser",pvo);
	}
	public int updateUser(UserVO pvo) throws Exception{
		return getSession().update("acl.updateUser",pvo);
	}
	public UserVO getUser(String userid) throws Exception{
		return (UserVO)getSession().queryForObject("acl.getUser",userid);
	}
	public int deleteUser(String userid) throws Exception{
		return getSession().delete("acl.deleteUser",userid);
	}
}
