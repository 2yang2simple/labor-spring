package com.labor.base.picture;

import java.util.HashMap;
import java.util.List;

import com.labor.common.constants.CommonConstants;
import com.labor.common.dao.DaoManager;
import com.labor.common.dao.ibatis.IbatisDao;

public class PictureDao extends IbatisDao {

	public PictureDao(DaoManager dm) {
		super(dm);
	}

	// picture
	public Integer insertPicture(HashMap hm) throws Exception {
		hm.put("active_status", CommonConstants.ACTIVE);
		return (Integer) getSession().insert("picture.insertPicture", hm);
	}

	public Integer updatePicture(HashMap hm) throws Exception {
		return (Integer) getSession().insert("picture.updatePicture", hm);
	}

	public HashMap getPictureById(String id) throws Exception {
		return (HashMap) getSession().queryForObject("picture.getPictureById", id);
	}

	public List listPictureBySubjectId(HashMap hm) throws Exception {
		return (List) getSession().queryForList("picture.listPictureBySubjectId", hm);
	}

}
