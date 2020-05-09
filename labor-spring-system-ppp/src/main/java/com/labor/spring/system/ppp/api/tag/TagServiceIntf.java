package com.labor.spring.system.ppp.api.tag;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.labor.spring.system.ppp.api.document.DocumentDto;
import com.labor.spring.system.ppp.entity.document.Document;
import com.labor.spring.system.ppp.entity.tag.Tag;

public interface TagServiceIntf {
	
	public Tag createTag(String tagName, String tagType, Integer parentId);
	public Tag createTag(String tagName, String tagType);
//	public Tag createFileDirTag(String tagName, Integer parentid);
//	public Tag createSystemExtractTag(String tagName);
//	public Tag createUserDefineTag(String tagName);

	public Page<Tag> findListByExample(Tag entity, Pageable pageable);
	public Page<Tag> findListByNameLike(String name, Pageable pageable);
	public Page<Tag> findListByNameOrPinyinLike(String name, Pageable pageable);

}
