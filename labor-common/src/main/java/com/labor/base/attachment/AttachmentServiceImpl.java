package com.labor.base.attachment;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import com.labor.base.attachment.AttachmentServiceIntf;
import com.labor.common.dao.ibatis.IbatisService;
import com.labor.common.exception.ServiceException;
import com.labor.common.service.ServiceTransactionProxy;


public class AttachmentServiceImpl extends IbatisService implements AttachmentServiceIntf{
	
	//attachment
	public Integer createAttachment(HashMap hm)throws ServiceException {
		Integer ret=0;
		try {
			AttachmentDao ad = (AttachmentDao)getDaoManager().buildDao(AttachmentDao.class);
			ret = ad.insertAttachment(hm);
		} catch (Exception e){
			throw new ServiceException(e);
		}
		return ret;
	}
	public HashMap getAttachmentById(String id)throws ServiceException {
		HashMap ret;
		try {
			AttachmentDao ad = (AttachmentDao)getDaoManager().buildDao(AttachmentDao.class);
			ret = ad.getAttachmentById(id);
		} catch (Exception e){
			throw new ServiceException(e);
		}
		return ret;
	}
	public HashMap getAttachmentByName(String name)throws ServiceException {
		HashMap ret;
		try {
			AttachmentDao ad = (AttachmentDao)getDaoManager().buildDao(AttachmentDao.class);
			ret = ad.getAttachmentByName(name);
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
	
	public static void testGetAttachment() {

		try {

			AttachmentServiceIntf as = 
				(AttachmentServiceIntf)new ServiceTransactionProxy().bind(new AttachmentServiceImpl());	
			HashMap hm= new HashMap();

			hm = as.getAttachmentByName("20190708125730257545");
			if(hm!=null) {
				System.out.println(hm.get("atta_name")+"|"+hm.get("atta_filepath")+"|"+hm.get("atta_filename")+"|"+"|"+hm.get("atta_filetype"));
			}
		} catch (Exception e){
			e.printStackTrace();

		}
		
	}
	public static void testCreateAttachment() {

		try {

			AttachmentServiceIntf as = 
				(AttachmentServiceIntf)new ServiceTransactionProxy().bind(new AttachmentServiceImpl());	
			HashMap hm= new HashMap();
			
	        String fileDir = new SimpleDateFormat("yyyyMMdd").format(new Date());
			String fileName = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()) 
					+ (int)((Math.random()*9+1)*100000);
			String fileType = "jpg";	

			
			hm.put("atta_name", "69310321-2-副本");
			hm.put("atta_filepath", fileDir);
			hm.put("atta_filename", fileName);
			hm.put("atta_filetype", fileType);

			as.createAttachment(hm);
			
		} catch (Exception e){
			e.printStackTrace();

		}
	}
	
	public static void main(String[] args) {
		testGetAttachment();
	}

}
