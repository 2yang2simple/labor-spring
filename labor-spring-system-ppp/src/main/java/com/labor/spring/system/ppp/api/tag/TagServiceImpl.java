package com.labor.spring.system.ppp.api.tag;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.labor.common.constants.CommonConstants;

import com.labor.common.util.StringUtil;
import com.labor.spring.system.ppp.HanLPExtractor;
import com.labor.spring.system.ppp.entity.tag.Tag;


@Service
public class TagServiceImpl implements TagServiceIntf{
	@Autowired
	private TagRepository tagRepository;
	
	private Tag executeCreateTag(String tagName, String tagType, Integer parentId) {
		Tag ret = null;
		if(!StringUtil.isEmpty(tagName)&&!StringUtil.isEmpty(tagType)&&parentId!=null) {
			Tag tmp = new Tag();
			/***** [name for ex.] need to unique; START */
			tmp.setName(tagName);
			tmp.setType(tagType);
			tmp.setParentId(parentId);
			tmp.setStatus(CommonConstants.ACTIVE);
			Example<Tag> example =Example.of(tmp);
			Optional<Tag> ot = tagRepository.findOne(example);
			if (ot.isPresent()) {
				return ot.get();
			}
			/***** [name for ex.] need to unique; END */
			Tag newtag = new Tag();
			newtag.setId(null);
			newtag.setType(tagType);
			newtag.setName(tagName);
			newtag.setParentId(parentId);
			newtag.setStatus(CommonConstants.ACTIVE);
			newtag.setNamePinyin(HanLPExtractor.pinyin(tagName));
			ret = tagRepository.save(newtag);
			
		}
		return ret;
	}

	@Override
	@Transactional
	public Tag createTag(String tagName, String tagType, Integer parentId) {
		return executeCreateTag(tagName, tagType, parentId);
	}

	@Override
	@Transactional
	public Tag createTag(String tagName, String tagType) {
		return executeCreateTag(tagName, tagType, 0);
	}


	static int count = 0;
	public static void main(String[] args) {
		
	}
	
	public static void printDir(String path, String parentid) {
		File file = new File(path);
		File[] files = file.listFiles();
		if (files == null) {
			return;
		}

		for (File f : files) {
			if (f.isFile()) {
				//do some file thing; tag = parentid,
				//System.err.println("file:"+parentid+"."+f.getName());
			} if (f.isDirectory()) {
				count++;
				System.err.println(""+parentid+"-"+f.getName());
				//do some insert get a new parentid
				String newparentid = parentid+"0";
				
				printDir(f.getAbsolutePath(),newparentid);
			}
		}
	}

//	@Override
//	@Transactional
//	public Tag createFileDirTag(String tagName, Integer parentid) {
//		return executeCreateTag(tagName, TagType.DOCUMENT_FILEDIR, parentid);
//	}
//	@Override
//	@Transactional
//	public Tag createSystemExtractTag(String tagName) {
//		return executeCreateTag(tagName, TagType.DOCUMENT_SYSTEMEXTRACT, 0);
//	}
//
//	@Override
//	@Transactional
//	public Tag createUserDefineTag(String tagName) {
//		return executeCreateTag(tagName, TagType.DOCUMENT_USERDEFINE, 0);
//	}
	
	@Override
	public Page<Tag> findListByExample(Tag entity, Pageable pageable){
		Example<Tag> example =Example.of(entity);
		return tagRepository.findAll(example, pageable);
	}
	@Override
	public Page<Tag> findListByNameLike(String name, Pageable pageable){		
		if (StringUtil.isEmpty(name)) {
			return tagRepository.findAll(pageable);
		}
		return tagRepository.findByNameContainingIgnoreCase(name,pageable);
	}
	@Override
	public Page<Tag> findListByNameOrPinyinLike(String name, Pageable pageable){
		List<String> types = new ArrayList<String>();
		types.add(TagType.DOCUMENT_USERDEFINE);
		types.add(TagType.DOCUMENT_SYSTEMEXTRACT);
		types.add(TagType.DOCUMENT_PROJECT);
		types.add(TagType.DOCUMENT_PRODUCT);
		if (StringUtil.isEmpty(name)) {
			return tagRepository.findByTypeIn(types,pageable);
		}
//		return tagRepository.findByNameContainingIgnoreCaseOrNamePinyinContainingIgnoreCase(name,name,pageable);
		return tagRepository.findByNameContainingIgnoreCaseAndTypeInOrNamePinyinContainingIgnoreCaseAndTypeIn(
				name,types,name,types,pageable);
	}
}
