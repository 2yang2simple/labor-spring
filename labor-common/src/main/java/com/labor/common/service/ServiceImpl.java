package com.labor.common.service;

import com.labor.common.dao.DaoManager;
import com.labor.common.exception.ServiceException;

/**
 * MUST THROWS THE EXCEPTIONS, OR THE TRANSACTION CAN NOT BE ROLLED BACK!!!!
 * 
 * @author yang yang1
 *
 */
public abstract class ServiceImpl implements ServiceIntf {
	private DaoManager daoManager = null;

	public ServiceImpl() {
		initDaoManager();
	}

	/**
	 * must create a daomanager for service.
	 */
	public abstract void initDaoManager();

	public DaoManager getDaoManager() {
		return daoManager;
	}

	public void setDaoManager(DaoManager daoManager) {
		this.daoManager = daoManager;
	}

	@Override
	public void open() throws ServiceException {
		try {
			daoManager.open();
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

	public void startTransaction() throws ServiceException {
		try {
			daoManager.startTransaction();
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

	public void commitTransaction() throws ServiceException {
		try {
			daoManager.commitTransaction();
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

	public void rollbackTransaction() throws ServiceException {
		try {
			daoManager.rollbackTransaction();
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

	public void closeTransaction() throws ServiceException {
		try {
			daoManager.closeTransaction();
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}
}
