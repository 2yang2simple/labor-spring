package com.labor.base.cfg;


import com.labor.common.dao.ibatis.IbatisService;
import com.labor.common.dao.jdbc.JdbcService;
import com.labor.common.exception.ServiceException;
import com.labor.common.service.ServiceTransactionProxy;

import java.util.ArrayList;
import java.util.List;

public class DDVServiceImpl extends IbatisService implements DDVServiceIntf{
	
	public List getDDTypeList() throws Exception{
		List ret= new ArrayList();
		try {
			DDVDao dd = (DDVDao)getDaoManager().buildDao(DDVDao.class);
			ret = dd.getDDTypeList();
		} catch (Exception e){
			throw new ServiceException(e);
		}
		return ret;
	}
	public Integer getDDId(String ddvalue) throws Exception{
		Integer ret=0;
		try {
			DDVDao dd = (DDVDao)getDaoManager().buildDao(DDVDao.class);
			ret = dd.getDDId(ddvalue);
		} catch (Exception e){
			throw new ServiceException(e);
		}
		return ret;
	}
	public String getDDValue(String ddid) throws Exception{
		String ret="";
		try {
			DDVDao dd = (DDVDao)getDaoManager().buildDao(DDVDao.class);
			ret = dd.getDDValue(ddid);
		} catch (Exception e){
			throw new ServiceException(e);
		}
		return ret;
	}
	public Integer countDDV(DDValueVO pvo) throws ServiceException{
		Integer ret=0;
		try {
			DDVDao dd = (DDVDao)getDaoManager().buildDao(DDVDao.class);
			ret = dd.countDDV(pvo);
		} catch (Exception e){
			throw new ServiceException(e);
		}
		return ret;
	}
	
	public DDValueVO getDDV(String ddid) throws ServiceException {
		DDValueVO ret = new DDValueVO();
		try {
			DDVDao dd = (DDVDao)getDaoManager().buildDao(DDVDao.class);
			ret = dd.getDDV(ddid);
		} catch (Exception e){
			throw new ServiceException(e);
		}
		return ret;
	}
	
	public List listDDV(DDValueVO pvo)throws ServiceException{
		List ret=null;
		try {
			DDVDao dd = (DDVDao)getDaoManager().buildDao(DDVDao.class);
			ret = dd.selectDDV(pvo);
		} catch (Exception e){
			throw new ServiceException(e);
		}
		return ret;
	}
	public Integer createDDV(DDValueVO pvo)throws ServiceException {
		Integer ret=0;
		try {
			DDVDao dd = (DDVDao)getDaoManager().buildDao(DDVDao.class);
			ret = dd.insertDDV(pvo);
		} catch (Exception e){
			throw new ServiceException(e);
		}
		return ret;
	}
	public int editDDV(DDValueVO pvo)throws ServiceException{
		int ret=0;
		try {
			DDVDao ad = (DDVDao)getDaoManager().buildDao(DDVDao.class);
			ret = ad.updateDDV(pvo);
		} catch (Exception e){
			throw new ServiceException(e);
		}
		return ret;
	}
	
/*****************************************************************************************************************
 * ***************************************************************************************************************
 * TESTING CODE***************************************************************************************************
 * ***************************************************************************************************************
 */
	public void testinsert() throws ServiceException{
		try {
			Integer roleid = 0;
			int count;
			DDVDao dd = (DDVDao)getDaoManager().buildDao(DDVDao.class);
			
			DDValueVO ddv = new DDValueVO();
			
			ddv.setDdtype("VDCS_NUMBER");
			ddv.setDdvalue("P1L1");
			ddv.setDdorder("1");
			
			count = dd.countDDV(ddv);
			System.out.println(count);
			System.out.println(dd.getDDV("1"));
			System.out.println(dd.selectDDV(new DDValueVO()));
			System.out.println(dd.getDDValue("1"));
			System.out.println(dd.getDDId("P1l1"));
			List list = dd.getDDTypeList();
			System.out.println(dd.getDDTypeList());
			DDValueVO upd = new DDValueVO();
			upd.setDdid("1");
			upd.setDddesc("test");
			dd.updateDDV(upd);
			
			if (count==0){
				dd.insertDDV(ddv);
			}

		} catch (Exception e){
			throw new ServiceException(e);
		}
	}
	public static void main(String arg[]){

		try {

			DDVServiceIntf as = 
				(DDVServiceIntf)new ServiceTransactionProxy().bind(new DDVServiceImpl());	
			DDValueVO ddvvo = new DDValueVO();
			
			ArrayList list = (ArrayList)as.listDDV(ddvvo);
			ArrayList list2 = (ArrayList)as.getDDTypeList();
			ddvvo = (DDValueVO)list2.get(0);
			System.out.print(ddvvo.getDdtype());
		} catch (Exception e){

		}
		
	}
}
