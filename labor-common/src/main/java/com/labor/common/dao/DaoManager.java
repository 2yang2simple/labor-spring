package com.labor.common.dao;

import java.lang.reflect.Constructor;
import java.sql.SQLException;

/***
 * to manage the transaction and the dao
 * generate the dao with the self transaction.
 * @author yang yang1
 *
 */
public class DaoManager {
	
	protected DaoTransaction daoTransaction = null;
	/**
	 * constructor with a dao transaction
	 * @param dt
	 */
	public DaoManager(DaoTransaction dt){
		daoTransaction = dt;
	}
	/**
	 * return a transaction
	 * @return
	 */
	public DaoTransaction getDaoTransaction() {
		return daoTransaction;
	}
	
	public void setDaoTransaction(DaoTransaction daoTransaction) {
		this.daoTransaction = daoTransaction;
	}
	public void open()throws Exception{
		daoTransaction.open();
	}
	public void startTransaction()throws Exception{
		daoTransaction.start();
	}
	public void commitTransaction() throws SQLException{
		daoTransaction.commit();
	}
	public void rollbackTransaction() throws SQLException{
		daoTransaction.rollback();
	}
	public void closeTransaction() throws SQLException{
		daoTransaction.close();
	}	
	/**
	 * to extends the dao generation
	 */
	public Object buildDao(Class dao) throws Exception{
		Object ret = null;
		Class clazz = dao;
		Class paratypes[] = new Class[]{DaoManager.class};
		Object paraobjs[] = new Object[]{this};
		Constructor cons = clazz.getConstructor(paratypes);
		ret = cons.newInstance(paraobjs);
		return ret;
	}
}
