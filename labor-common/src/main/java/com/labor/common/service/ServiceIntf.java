package com.labor.common.service;

import com.labor.common.exception.ServiceException;

/***
 * dao should be execute in service.
 * 
 * @author yang yang1
 *
 */
public interface ServiceIntf {


	public void open() throws ServiceException;
	
	public void startTransaction() throws ServiceException;

	public void commitTransaction() throws ServiceException;

	public void rollbackTransaction() throws ServiceException;

	public void closeTransaction() throws ServiceException;
}
