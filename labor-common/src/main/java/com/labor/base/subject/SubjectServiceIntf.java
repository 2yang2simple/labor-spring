package com.labor.base.subject;

import java.util.HashMap;
import java.util.List;

import com.labor.common.exception.ServiceException;
import com.labor.common.service.ServiceIntf;

public interface SubjectServiceIntf extends ServiceIntf {
	
	public Integer createSubject(HashMap hm) throws ServiceException;

	public Integer updateSubject(HashMap hm) throws ServiceException;

	public HashMap getSubjectById(String id) throws ServiceException;

	public List listSubject(HashMap hm) throws ServiceException;
	
	public Integer testupdatetransaction()throws ServiceException;

}
