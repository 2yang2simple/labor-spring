package com.labor.common.dao.ibatis;

import java.sql.SQLException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.ibatis.sqlmap.client.SqlMapClient;
import com.ibatis.sqlmap.client.SqlMapSession;
import com.labor.common.dao.DaoTransaction;


public class IbatisDaoTransaction implements DaoTransaction{

	private SqlMapClient sqlMapper = null;
	private SqlMapSession session = null;
	
	public SqlMapSession getSession() {
		return session;
	}
	
	//close all when finalize
	@Override
	public void finalize () throws Throwable{
		if (session != null) session.close();
		super.finalize();
	}


	@Override
	public void open() throws Exception {
		sqlMapper = IbatisDataSource.getSqlMapper();
		session = sqlMapper.openSession();
		
	}
	//start DB transaction
	@Override
	public void start() throws Exception {
		if (session != null) {
			session.startTransaction();
		}
	}
	
	//close DB transaction
	@Override
	public void close() throws SQLException{
		if (session != null) {
			try {
				session.endTransaction();
			} finally {
				session.close();
			}
		}
	}
	
	//commit
	@Override
	public void commit() throws SQLException{
		if (session != null) {
			session.commitTransaction();
		}
	}
	
	//rollback
	@Override
	public void rollback() throws SQLException{

	}
	
}
