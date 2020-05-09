package com.labor.base.subject;

import java.util.HashMap;
import java.util.List;

import com.labor.common.dao.ibatis.IbatisService;
import com.labor.common.exception.ServiceException;
import com.labor.common.service.ServiceTransactionProxy;

public class SubjectServiceImpl extends IbatisService implements SubjectServiceIntf{
	
	//subject
	public Integer createSubject(HashMap hm)throws ServiceException {
		Integer ret=0;
		try {
			SubjectDao dao = (SubjectDao)getDaoManager().buildDao(SubjectDao.class);
			ret = dao.insertSubject(hm);
		} catch (Exception e){
			throw new ServiceException(e);
		}
		return ret;
	}	
	public Integer updateSubject(HashMap hm)throws ServiceException {
		Integer ret=0;
		try {
			SubjectDao dao = (SubjectDao)getDaoManager().buildDao(SubjectDao.class);
			ret = dao.updateSubject(hm);
		} catch (Exception e){
			throw new ServiceException(e);
		}
		return ret;
	}
	public HashMap getSubjectById(String id)throws ServiceException {
		HashMap ret;
		try {
			SubjectDao dao = (SubjectDao)getDaoManager().buildDao(SubjectDao.class);
			ret = dao.getSubjectById(id);
		} catch (Exception e){
			throw new ServiceException(e);
		}
		return ret;
	}
	
	public List listSubject(HashMap hm)throws ServiceException {
		List ret;
		try {
			SubjectDao dao = (SubjectDao)getDaoManager().buildDao(SubjectDao.class);
			ret = dao.listSubject(hm);
		} catch (Exception e){
			throw new ServiceException(e);
		}
		return ret;
	}
	
	public Integer testupdatetransaction()throws ServiceException {
		Integer ret=0;
		try {
			HashMap hm= new HashMap();
			hm.put("sub_name1", "可园七期3"+Math.random());
			hm.put("sub_name2", "12324");
			hm.put("sub_name3", "很多人");
			hm.put("sub_name4", "xxx");
			hm.put("sub_id", "1");
			SubjectDao dao = (SubjectDao)getDaoManager().buildDao(SubjectDao.class);
			ret = dao.updateSubject(hm);
			int i =1/0;
			hm.put("sub_id", "2");
			ret = dao.updateSubject(hm);
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
	
	public static void testcreate() {

		try {

			SubjectServiceIntf service = 
				(SubjectServiceIntf)new ServiceTransactionProxy().bind(new SubjectServiceImpl());	
			HashMap hm= new HashMap();
			
			hm.put("sub_name1", "可园七期");
			hm.put("sub_name2", "12324");
			hm.put("sub_name3", "很多人");
			hm.put("sub_name4", "xxx");
			

			System.out.println( service.createSubject(hm));
			
		} catch (Exception e){
			e.printStackTrace();
	
		}
		
	}
	public static void testtransaction() {

		try {

			SubjectServiceIntf service = 
				(SubjectServiceIntf)new ServiceTransactionProxy().bind(new SubjectServiceImpl());	

			System.out.println( service.testupdatetransaction());
			
		} catch (Exception e){
			e.printStackTrace();

		}
		
	}
	public static void testupdate() {

		try {

			SubjectServiceIntf service = 
				(SubjectServiceIntf)new ServiceTransactionProxy().bind(new SubjectServiceImpl());	
			HashMap hm= new HashMap();
			
			hm.put("sub_name1", "可园七期3");
			hm.put("sub_name2", "12324");
			hm.put("sub_name3", "很多人");
			hm.put("sub_name4", "xxx");
			hm.put("sub_id", "1");

			System.out.println( service.updateSubject(hm));
			
		} catch (Exception e){
			e.printStackTrace();

		}
		
	}
	
	public static void testget() {

		try {

			SubjectServiceIntf service = 
				(SubjectServiceIntf)new ServiceTransactionProxy().bind(new SubjectServiceImpl());	

			System.out.println( service.getSubjectById("1"));
			
		} catch (Exception e){
			e.printStackTrace();

		}
		
	}
	
	public static void testlist() {

		try {

			SubjectServiceIntf service = 
				(SubjectServiceIntf)new ServiceTransactionProxy().bind(new SubjectServiceImpl());	
			HashMap hm= new HashMap();
			
			//hm.put("sub_name1", "可园七期");

			System.out.println( service.listSubject(hm));
			
		} catch (Exception e){
			e.printStackTrace();
		}
		
	}
	
	public static void main(String[] args) {
//		testcreate();
//		testupdate();
//		testlist();
		testtransaction();
//		System.out.println("createSubject".toLowerCase().indexOf("create"));
	}
}
