package com.labor.common.dao.ibatis;

import com.ibatis.sqlmap.client.SqlMapSession;
import com.labor.common.dao.Dao;
import com.labor.common.dao.DaoManager;

public class IbatisDao implements Dao{
	
	private IbatisDaoTransaction daoTransaction = null;
	
	public IbatisDao (DaoManager dm){
		daoTransaction = (IbatisDaoTransaction)dm.getDaoTransaction();
	}
	
	public SqlMapSession getSession(){
		return daoTransaction.getSession();
	}

}
