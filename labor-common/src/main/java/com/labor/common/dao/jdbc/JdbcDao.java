package com.labor.common.dao.jdbc;

import java.util.List;

import javax.sql.rowset.CachedRowSet;

import com.labor.common.dao.Dao;
import com.labor.common.dao.DaoManager;
import com.labor.common.dao.jdbc.JdbcRowSetUtil;

public class JdbcDao implements Dao {
	
	private JdbcDaoTransaction daoTransaction = null;
	public JdbcDao (DaoManager dm){
		daoTransaction = (JdbcDaoTransaction)dm.getDaoTransaction();
	}
	
	/**
	 * execute the multiple query sql with one transaction.
	 * return is the hashmap list
	 * @param sql
	 * @return
	 * @throws Exception
	 */
	public List executeSelect(String sql) throws Exception {
		return executeSelect(sql,null);
	}
	/**
	 * execute the multiple query sql with one transaction.
	 * return is clazz object list;
	 * @param sql
	 * @return
	 * @throws Exception
	 */
	public List executeSelect(String sql,Class clazz) throws Exception {
		List ret = null;
		CachedRowSet crs = (CachedRowSet)daoTransaction.executeQuery(sql);
		ret = JdbcRowSetUtil.toResultList(crs,clazz);
		crs.close();
		return ret;
	}
	
	/**
	 * execute the multiple update sql with one transaction.
	 * @param sql
	 * @return
	 * @throws Exception
	 */
	public int executeUpdate(String sql) throws Exception {
		return daoTransaction.executeUpdate(sql);
	}
	
	/**
	 * execute the multiple insert sql with one transaction.
	 * @param sql
	 * @return
	 * @throws Exception
	 */
	public int executeInsert(String sql) throws Exception {
		return daoTransaction.executeUpdate(sql);
	}

}
