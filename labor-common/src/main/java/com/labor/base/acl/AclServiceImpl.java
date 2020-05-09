package com.labor.base.acl;

import com.labor.common.constants.CommonConstants;
import com.labor.common.dao.ibatis.IbatisService;
import com.labor.common.exception.ServiceException;
import com.labor.common.service.ServiceTransactionProxy;
import com.labor.common.util.StringUtil;

import java.util.List;

public class AclServiceImpl extends IbatisService implements AclServiceIntf{
	
	public Integer getRoleid(String rolename) throws Exception{
		Integer ret=0;
		try {
			AclDao ad = (AclDao)getDaoManager().buildDao(AclDao.class);
			ret = ad.getRoleid(rolename);
		} catch (Exception e){
			throw new ServiceException(e);
		}
		return ret;
	}
	public List listRole(RoleVO pvo)throws ServiceException{
		List ret=null;
		try {
			AclDao ad = (AclDao)getDaoManager().buildDao(AclDao.class);
			ret = ad.selectRole(pvo);
		} catch (Exception e){
			throw new ServiceException(e);
		}
		return ret;
	}
	public List listUser(UserVO pvo)throws ServiceException{
		List ret=null;
		try {
			AclDao ad = (AclDao)getDaoManager().buildDao(AclDao.class);
			ret = ad.selectUser(pvo);
		} catch (Exception e){
			throw new ServiceException(e);
		}
		return ret;
	}
	
	public Integer countUser(UserVO pvo) throws ServiceException{
		Integer ret=0;
		try {
			AclDao ad = (AclDao)getDaoManager().buildDao(AclDao.class);
			ret = ad.countUser(pvo);
		} catch (Exception e){
			throw new ServiceException(e);
		}
		return ret;
	}
	
	public Integer createUser(UserVO pvo)throws ServiceException {
		Integer ret=0;
		try {
			AclDao ad = (AclDao)getDaoManager().buildDao(AclDao.class);
			ret = ad.insertUser(pvo);
		} catch (Exception e){
			throw new ServiceException(e);
		}
		return ret;
	}
	public int editUser(UserVO pvo)throws ServiceException{
		int ret=0;
		try {
			AclDao ad = (AclDao)getDaoManager().buildDao(AclDao.class);
			ret = ad.updateUser(pvo);
		} catch (Exception e){
			throw new ServiceException(e);
		}
		return ret;
	}
	public UserVO getUser(String userid) throws ServiceException {
		UserVO ret = new UserVO();
		try {
			AclDao ad = (AclDao)getDaoManager().buildDao(AclDao.class);
			ret = ad.getUser(userid);
		} catch (Exception e){
			throw new ServiceException(e);
		}
		return ret;
	}
	
	public int deleteUser(String userid) throws ServiceException{
		int ret=0;
		try {
			AclDao ad = (AclDao)getDaoManager().buildDao(AclDao.class);
			//not delete , just inactive it ;
//			ret = ad.deleteUser(userid);
			UserVO uvo = new UserVO();
			uvo.setCreateby(userid);
			uvo.setUserstatus(CommonConstants.INACTIVE);
			ret = ad.updateUser(uvo);
		} catch (Exception e){
			throw new ServiceException(e);
		}
		return ret;
	}
	public void testinsert() throws ServiceException{
		try {
			Integer roleid = 0;
			int count;
			AclDao ad = (AclDao)getDaoManager().buildDao(AclDao.class);
			
			UserVO user = new UserVO();
			
			user.setUsersno("3p07156");
			count = ad.countUser(user);
			System.out.println(count);
			if (count==0){
				user.setUsername("yang yang");
				user.setRoleid(StringUtil.safeToString(ad.getRoleid(AclConstants.ROLE_ADMIN)));
				ad.insertUser(user);
			}
			
			user = new UserVO();
			user.setUsersno("3p07157");
			count = ad.countUser(user);
			System.out.println(count);
			if (count==0){
				user.setUsername("yang yang2");
				user.setRoleid(StringUtil.safeToString(ad.getRoleid("dd")));
				ad.insertUser(user);
			}
			
			user = new UserVO();
			user.setUsersno("3p07158");
			count = ad.countUser(user);
			System.out.println(count);
			if (count==0){
				user.setUsername("yang yang3");
				user.setRoleid(StringUtil.safeToString(ad.getRoleid(AclConstants.ROLE_TECHNICIAN)));
				ad.insertUser(user);
			}
			
//			throw new Exception();
		} catch (Exception e){
			throw new ServiceException(e);
		}
	}
	public static void main(String arg[]){
		try {
			AclServiceIntf as = 
				(AclServiceIntf)new ServiceTransactionProxy().bind(new AclServiceImpl());	
			as.testinsert();
			
		} catch (Exception e){
			e.printStackTrace();
		}
		
	}
}
