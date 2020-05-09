package com.labor.common.dao;

import java.sql.SQLException;

/***
 * dao transaction
 * @author yang yang1
 *
 */
public interface DaoTransaction{

	/**
	 * close all when finalize
	 * @throws Throwable
	 */
	public void finalize () throws Throwable;
	
	/**
	 * open DB session
	 * @throws Exception
	 */
	public void open() throws Exception;
	
	/**
	 * start DB transaction
	 * @throws Exception
	 */
	public void start() throws Exception;
	
	/**
	 * close DB transaction
	 * @throws SQLException
	 */
	public void close() throws SQLException;
	
	/**
	 * commit DB transaction
	 * @throws SQLException
	 */
	public void commit() throws SQLException;
	
	/**
	 * rollback DB transaction
	 * @throws SQLException
	 */
	public void rollback() throws SQLException;
	
//	/**
//	 * execute the multiple query sql with one transaction.
//	 * @param sql
//	 * @return
//	 * @throws Exception
//	 */
//	public Object executeQuery(String sql) throws Exception;
//	/**
//	 * execute the update sql with one transaction.
//	 * @param sql
//	 * @return
//	 * @throws Exception
//	 */
//	public int executeUpdate(String sql) throws Exception;

}
