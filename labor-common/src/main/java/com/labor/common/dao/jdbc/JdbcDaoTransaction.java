package com.labor.common.dao.jdbc;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.labor.common.dao.DaoTransaction;
import com.sun.rowset.CachedRowSetImpl;

public class JdbcDaoTransaction implements DaoTransaction{
	
	private Logger log = LogManager.getLogger(JdbcDaoTransaction.class);
	private Connection _conn = null;
	private Statement _stmt = null;

	//close all when finalize
	public void finalize () throws Throwable{
		try {if (_stmt != null) _stmt.close();}catch(SQLException e){throw e;}
		try {if (_conn != null) _conn.close();}catch(SQLException e){throw e;}
		super.finalize();
	}


	@Override
	public void open() throws Exception {
		
	}
	//start DB transaction
	public void start() throws Exception {
		log.debug(" startTransaction");
		_conn = JdbcDataSource.createConnection();
		_stmt = _conn.createStatement();
		_conn.setAutoCommit(false);
	}
	
	//close DB transaction
	public void close() throws SQLException{
		log.debug(" closeTransaction");
		try {if (_stmt != null) _stmt.close();}catch(SQLException e){throw e;}
		try {if (_conn != null) _conn.close();}catch(SQLException e){log.error("close connection error:" + e );throw e;}
	}
	
	//commit
	public void commit() throws SQLException{
		log.debug(" commitTransaction");
		if (_conn != null) {
			_conn.commit();
		}
	}
	
	//rollback
	public void rollback() throws SQLException{
		log.debug(" rollbackTransaction");
		if (_conn != null) {
			_conn.rollback();
		}
	}
	
	/**
	 * execute the multiple query sql with one transaction.
	 * @param sql
	 * @return
	 * @throws Exception
	 */
	public Object executeQuery(String sql) throws Exception {
		log.info("executeQuery:"+sql);
		ResultSet rs = null;
		CachedRowSetImpl ret = new CachedRowSetImpl();
		try {
			rs = _stmt.executeQuery(sql);
			ret.populate(rs);
		} catch (Exception e){
			log.error("executeQuery error:" + e );
			throw e;
		} finally {
			try {if (rs != null) rs.close();}catch(SQLException e){throw e;}
		}
		return ret;
	}

	/**
	 * execute the update sql with one transaction.
	 * @param sql
	 * @return
	 * @throws Exception
	 */
	public int executeUpdate(String sql) throws Exception {
		log.info("executeQuery:"+sql);
		int ret=0;
		try {
			ret = _stmt.executeUpdate(sql);
		} catch (Exception e) {
			log.error("executeUpdate error:" + e );
			throw e;
		} finally {
		}
		return ret;
	}

}
