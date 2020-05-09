package com.labor.base.picture;

import java.util.HashMap;
import java.util.List;

import com.labor.common.exception.ServiceException;
import com.labor.common.service.ServiceIntf;

public interface PictureServiceIntf extends ServiceIntf {

	public Integer createPicture(HashMap hm) throws ServiceException;

	public Integer updatePicture(HashMap hm) throws ServiceException;

	public HashMap getPictureById(String id) throws ServiceException;

	public List listPictureBySubjectId(HashMap hm) throws ServiceException;
}
