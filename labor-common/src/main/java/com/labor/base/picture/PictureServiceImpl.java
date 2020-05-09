package com.labor.base.picture;

import java.util.HashMap;
import java.util.List;

import com.labor.base.attachment.AttachmentServiceImpl;
import com.labor.base.attachment.AttachmentServiceIntf;
import com.labor.base.subject.SubjectDao;
import com.labor.base.subject.SubjectServiceImpl;
import com.labor.base.subject.SubjectServiceIntf;
import com.labor.common.dao.ibatis.IbatisService;
import com.labor.common.exception.ServiceException;
import com.labor.common.service.ServiceIntf;
import com.labor.common.service.ServiceTransactionProxy;

public class PictureServiceImpl extends IbatisService implements PictureServiceIntf {

	// picture
	public Integer createPicture(HashMap hm) throws ServiceException {
		Integer ret = 0;
		try {
			PictureDao dao = (PictureDao) getDaoManager().buildDao(PictureDao.class);
			ret = dao.insertPicture(hm);
		} catch (Exception e) {
			throw new ServiceException(e);
		}
		return ret;
	}

	public Integer updatePicture(HashMap hm) throws ServiceException {
		Integer ret = 0;
		try {
			PictureDao dao = (PictureDao) getDaoManager().buildDao(PictureDao.class);
			ret = dao.updatePicture(hm);
		} catch (Exception e) {
			throw new ServiceException(e);
		}
		return ret;
	}

	public HashMap getPictureById(String id) throws ServiceException {
		HashMap ret;
		try {
			PictureDao dao = (PictureDao) getDaoManager().buildDao(PictureDao.class);
			ret = dao.getPictureById(id);
		} catch (Exception e) {
			throw new ServiceException(e);
		}
		return ret;
	}

	public List listPictureBySubjectId(HashMap hm) throws ServiceException {
		List ret;
		try {
			PictureDao dao = (PictureDao) getDaoManager().buildDao(PictureDao.class);
			ret = dao.listPictureBySubjectId(hm);
		} catch (Exception e) {
			throw new ServiceException(e);
		}
		return ret;
	}

	/*****************************************************************************************************************
	 * ***************************************************************************************************************
	 * TESTING
	 * CODE***************************************************************************************************
	 * ***************************************************************************************************************
	 */

	public static void testcreate() {

		try {

			PictureServiceIntf service = (PictureServiceIntf) new ServiceTransactionProxy()
					.bind(new PictureServiceImpl());
			HashMap hm = new HashMap();

			hm.put("pic_desvarchar", "ibati最终项目也顺利完成，不");
			hm.put("pic_showorder", "3");
			hm.put("atta_id", "3");
			hm.put("atta_filename", "20190708");
			hm.put("sub_id", "2");
			hm.put("sub_type", "SUBJECT");

			System.out.println(service.createPicture(hm));

		} catch (Exception e) {
			e.printStackTrace();

		}

	}

	public static void testupdate() {

		try {

			PictureServiceIntf service = (PictureServiceIntf) new ServiceTransactionProxy()
					.bind(new PictureServiceImpl());
			HashMap hm = new HashMap();

			hm.put("pic_desvarchar", "ibati最终项目也顺利完成，不22");
			hm.put("pic_showorder", "2");
			hm.put("pic_id", "2");


			System.out.println(service.updatePicture(hm));

		} catch (Exception e) {
			e.printStackTrace();

		}

	}

	public static void testget() {
		
		try {

			PictureServiceIntf service = (PictureServiceIntf) new ServiceTransactionProxy()
					.bind(new PictureServiceImpl());

			System.out.println(service.getPictureById("2"));

		} catch (Exception e) {
			e.printStackTrace();

		}

	}

	public static void testlist() {

		try {

			PictureServiceIntf service = (PictureServiceIntf) new ServiceTransactionProxy()
					.bind(new PictureServiceImpl());
			HashMap hm = new HashMap();

			hm.put("sub_id", "3");

			System.out.println(service.listPictureBySubjectId(hm));

		} catch (Exception e) {
			e.printStackTrace();

		}

	}

	public static void main(String[] args) {
//			testcreate();
//			testupdate();
		testlist();
	}
}
