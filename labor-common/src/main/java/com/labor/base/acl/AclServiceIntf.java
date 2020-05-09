package com.labor.base.acl;

import com.labor.common.exception.ServiceException;
import com.labor.common.service.ServiceIntf;

import java.util.List;

public interface AclServiceIntf extends ServiceIntf{
	public Integer getRoleid(String rolename) throws Exception;
	public List listRole(RoleVO vo)throws ServiceException;
	public List listUser(UserVO vo)throws ServiceException;
	public Integer countUser(UserVO pvo) throws Exception;
	public Integer createUser(UserVO vo)throws ServiceException;
	public int editUser(UserVO vo)throws ServiceException;
	public UserVO getUser(String userid) throws Exception;
	public int deleteUser(String userid) throws Exception;
	public void testinsert() throws Exception;

}
