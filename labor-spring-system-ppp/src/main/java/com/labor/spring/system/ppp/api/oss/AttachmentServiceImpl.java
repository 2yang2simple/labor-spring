package com.labor.spring.system.ppp.api.oss;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.labor.common.constants.CommonConstants;
import com.labor.spring.system.ppp.entity.oss.Attachment;

@Service
public class AttachmentServiceImpl implements AttachmentServiceIntf{
	
	@Autowired
	private AttachmentRepository attachmentRepository;
	
	@Override
	@Transactional
	public Attachment create(Attachment attachment) {
		Attachment ret = null;
		if (attachment!=null) {
			attachment.setStatus(CommonConstants.ACTIVE);
			ret = attachmentRepository.save(attachment);
		}
		return ret;
	}
	
	@Override
	public Optional<Attachment> findByFilename(String fileName){
		return attachmentRepository.findOneByFilename(fileName);
	}
}
