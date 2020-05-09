package com.labor.base.attachment;

import java.util.HashMap;
import java.util.List;

import com.labor.common.exception.ServiceException;
import com.labor.common.service.ServiceIntf;

public interface AttachmentServiceIntf extends ServiceIntf {
	
	public Integer createAttachment(HashMap hm) throws ServiceException;

	public HashMap getAttachmentById(String id) throws ServiceException;

	public HashMap getAttachmentByName(String name) throws ServiceException;

}
