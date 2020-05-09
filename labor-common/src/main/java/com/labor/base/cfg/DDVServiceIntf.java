package com.labor.base.cfg;

import com.labor.common.exception.ServiceException;
import com.labor.common.service.ServiceIntf;

import java.util.List;

public interface DDVServiceIntf extends ServiceIntf{
	public List getDDTypeList() throws Exception;
	public Integer getDDId(String ddvalue) throws Exception;
	public String getDDValue(String ddid) throws Exception;
	public DDValueVO getDDV(String ddid) throws Exception;
	public Integer countDDV(DDValueVO pvo) throws Exception;
	
	public List listDDV(DDValueVO vo)throws ServiceException;
	public Integer createDDV(DDValueVO vo)throws ServiceException;
	public int editDDV(DDValueVO vo)throws ServiceException;

	public void testinsert() throws Exception;


}
