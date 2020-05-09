package com.labor.spring.system.ppp.api.oss;

import java.util.Optional;

import com.labor.spring.system.ppp.entity.oss.Attachment;

public interface AttachmentServiceIntf {
	
	public Attachment create(Attachment attachment);
	public Optional<Attachment> findByFilename(String filename);

}
