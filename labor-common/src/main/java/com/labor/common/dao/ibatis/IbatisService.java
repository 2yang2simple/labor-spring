package com.labor.common.dao.ibatis;

import com.labor.common.dao.DaoManager;
import com.labor.common.service.ServiceImpl;
import com.labor.common.service.ServiceIntf;

public class IbatisService extends ServiceImpl implements ServiceIntf{

	
	public void initDaoManager(){
		setDaoManager(new DaoManager(new IbatisDaoTransaction()));
	}
	

}
