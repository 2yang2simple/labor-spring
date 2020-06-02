package com.labor.spring.system.ppp.api.document;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import javax.validation.Valid;

import org.apache.logging.log4j.LogManager;
import org.apache.poi.hwpf.converter.PicturesManager;
import org.apache.poi.hwpf.usermodel.PictureType;
import org.apache.shiro.authz.annotation.RequiresPermissions;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.labor.common.constants.CommonConstants;
import com.labor.common.poi.OfficeReader;
import com.labor.common.util.ClassUtil;
import com.labor.common.util.FileUtil;
import com.labor.common.util.StringUtil;
import com.labor.common.util.TokenUtil;
import com.labor.spring.base.BaseRestController;
import com.labor.spring.bean.Result;
import com.labor.spring.bean.ResultCode;
import com.labor.spring.core.service.SysconfigConstants;
import com.labor.spring.feign.api.auth.AuthLoginService;
import com.labor.spring.feign.api.auth.UserVO;
import com.labor.spring.system.ppp.ApplicationProperties;
import com.labor.spring.system.ppp.api.oss.AttachmentServiceIntf;
import com.labor.spring.system.ppp.api.oss.ObjectStorageServiceIntf;
import com.labor.spring.system.ppp.api.tag.TagServiceIntf;
import com.labor.spring.system.ppp.api.tag.TagType;
import com.labor.spring.system.ppp.entity.document.Document;
import com.labor.spring.system.ppp.entity.document.DocumentComment;
import com.labor.spring.system.ppp.entity.document.DocumentContent;
import com.labor.spring.system.ppp.entity.document.DocumentTag;
import com.labor.spring.system.ppp.entity.document.DocumentUser;
import com.labor.spring.system.ppp.entity.oss.Attachment;
import com.labor.spring.system.ppp.entity.oss.ObjectHeader;
import com.labor.spring.system.ppp.entity.tag.Tag;
import com.labor.spring.util.WebUtil;

@RestController
@RequestMapping("/rest/documents")
public class DocumentRestController {

	@Autowired
	private ApplicationProperties properties;
	@Autowired
	private AuthLoginService authLoginService;
	@Autowired
	private ObjectStorageServiceIntf objectStorageService;
	@Autowired
	private DocumentServiceIntf documentService;
	@Autowired
	private TagServiceIntf tagService;	
		
//	//
//	private boolean isDocCreatorLogin(Integer docId) {
//		boolean ret = false;
//		User currentuser = WebUtil.getCurrentUser();
//	    User creator = documentService.findCreator(docId).orElse(null);
//		//if the current user is not the creator , then set content varsions null;
//		if(currentuser!=null&&creator!=null&&currentuser.getId().equals(creator.getId())) {
//			ret = true;
//		} 
//		return ret;
//	}
	//create full document
	@RequiresPermissions("document:create")
	@RequestMapping(value = {""}, method = RequestMethod.POST)
	public Result create(
					@RequestBody @Valid DocumentDto documentDto, 
					BindingResult bindingResult) {
	    if (bindingResult.hasErrors()) {
	        String message = bindingResult.getFieldError().getDefaultMessage();
	        return Result.failure(ResultCode.FAILURE_PARAM_INVALID,message);
	    }
	    Document ret = documentService.create(documentDto);
		return Result.success(ret);
	}
	//update full document
	@RequiresPermissions("document:edit")
	@RequestMapping(value = {"/{id}"}, method = RequestMethod.PUT)
	public Result update(
					@PathVariable(value="id") Integer id, 
					@RequestBody @Valid DocumentDto documentDto, 
					BindingResult bindingResult) {
		Document ret = null;
		if (bindingResult.hasErrors()) {
	        String message = bindingResult.getFieldError().getDefaultMessage();
	        return Result.failure(ResultCode.FAILURE_PARAM_INVALID,message);
	    }
		UserVO creator = documentService.findCreator(id);
	    if (!authLoginService.isCurrentUserOrSuperUser(creator.getUserId(),null)) {
	    	return Result.failure(ResultCode.FAILURE_PERMISSION_NOACCESS, ResultCode.MSG_FAILURE_PERMISSION_NOACCESS);
	    }
	    //save some which will not be updated.
	    if(documentDto.getDocument()!=null) {
		    Document doc = documentService.findDocumentById(id).orElse(null);
		    if (doc!=null) {
		    	documentDto.getDocument().setDocStatus(doc.getDocStatus());
		    	documentDto.getDocument().setStatus(doc.getStatus());
		    	documentDto.getDocument().setDescription(doc.getDescription());
		    	documentDto.getDocument().setFileMd5(doc.getFileMd5());
		    	documentDto.getDocument().setFilePath(doc.getFilePath());		    	
		    }
		}
	    ret = documentService.update(id,documentDto);
		return Result.success(ret);
	}
	
	//create document content
	@RequiresPermissions("document:edit")
	@RequestMapping(value = {"/{id}/contents"}, method = RequestMethod.POST)
	public Result createContent(
					@PathVariable(value="id") Integer id, 
					@RequestBody @Valid DocumentContent content, 
					BindingResult bindingResult) {
	    if (bindingResult.hasErrors()) {
	        String message = bindingResult.getFieldError().getDefaultMessage();
	        return Result.failure(ResultCode.FAILURE_PARAM_INVALID,message);
	    }
		UserVO creator = documentService.findCreator(id);
		if (!authLoginService.isCurrentUserOrSuperUser(creator.getUserId(),null)) {
	    	return Result.failure(ResultCode.FAILURE_PERMISSION_NOACCESS, ResultCode.MSG_FAILURE_PERMISSION_NOACCESS);
	    }
	    DocumentContent ret = documentService.createContent(id,content);
		return Result.success(ret);
	}
		
	//update document content
	@RequiresPermissions("document:edit")
	@RequestMapping(value = {"/{id}/contents/{contentid}"}, method = RequestMethod.PUT)
	public Result updateContent(
					@PathVariable(value="id") Integer id, 
					@PathVariable(value="contentid") Integer contentid, 
					@RequestBody @Valid DocumentContent content, 
					BindingResult bindingResult) {
		DocumentContent ret = null;
		if (bindingResult.hasErrors()) {
	        String message = bindingResult.getFieldError().getDefaultMessage();
	        return Result.failure(ResultCode.FAILURE_PARAM_INVALID,message);
	    }
		UserVO creator = documentService.findCreator(id);
		if (!authLoginService.isCurrentUserOrSuperUser(creator.getUserId(),null)) {
	    	return Result.failure(ResultCode.FAILURE_PERMISSION_NOACCESS, ResultCode.MSG_FAILURE_PERMISSION_NOACCESS);
	    }
		content.setId(contentid);
		ret = documentService.updateContent(id, contentid, content);
		return Result.success(ret);
	}

	
	@RequiresPermissions("document:edit")
	@RequestMapping(value = {"/{id}/tags"}, method = RequestMethod.POST)
	public Result createTag(
					@PathVariable(value="id") Integer id, 
					@RequestParam(value="tagname", required=true) String tagName,
					@RequestParam(value="tagtype", defaultValue=TagType.DOCUMENT_USERDEFINE) String tagType) {
		
	    DocumentTag ret = null;
	    if (!StringUtil.isEmpty(tagName)) {
	    	documentService.createTag(id, tagName, tagType);
	    }
		return Result.success(ret);
	}
	//find document tag list
	@RequiresPermissions("document:edit")
	@RequestMapping(value = {"/{id}/tags/list"}, method = RequestMethod.POST)
	public Result updateTagList(
					@PathVariable(value="id") Integer id, 
					@RequestBody List<DocumentTag> tagList, 
					@RequestBody String tagType) {
		List<DocumentTag> ret = documentService.updateTagList(id,tagList,tagType);
		return Result.success(ret);

	}
	
	//create document comment
	@RequiresPermissions("document:comment")
	@RequestMapping(value = {"/{id}/comments"}, method = RequestMethod.POST)
	public Result createComment(
					@PathVariable(value="id") Integer id, 
					@RequestBody @Valid DocumentComment comment, 
					BindingResult bindingResult) {
	    if (bindingResult.hasErrors()) {
	        String message = bindingResult.getFieldError().getDefaultMessage();
	        return Result.failure(ResultCode.FAILURE_PARAM_INVALID,message);
	    }
	    if (comment!=null) {
	    	UserVO currentuser = authLoginService.findLoginCacheCurrent();
	    	if (currentuser!=null) {
		    	comment.setCreator(currentuser.getUserName()+" "+currentuser.getUserRealName());
	    	}
	    }
	    DocumentComment ret = documentService.createComment(id,comment);
		return Result.success(ret);
	}
	//find document user list
	@RequiresPermissions("document:edit")
	@RequestMapping(value = {"/{id}/users/list"}, method = RequestMethod.POST)
	public Result updateUserList(
					@PathVariable(value="id") Integer id, 
					@RequestBody List<DocumentUser> userList) {
		List<DocumentUser> ret = documentService.updateUserList(id,userList);
		return Result.success(ret);
	}


	@RequestMapping(value = {""}, method = RequestMethod.GET)
	public Result findList(
					@RequestParam(value="sortby", defaultValue="id") String sortby) {
		Sort sort = new Sort(Sort.Direction.DESC, sortby);
		List<Document> ret = documentService.findDocumentListByExample(new Document(),sort);
		return Result.success(ret);
	}

	@RequestMapping(value = {"/{id}/contents-versions"}, method = RequestMethod.GET)
	public Result findContentVersionsByDocid(
					@PathVariable(value="id") Integer id) {
		List<DocumentContent> ret = documentService.findContentVersionsByDocid(id);
		return Result.success(ret);
	}

//	@RequiresPermissions("document:view")
	@RequestMapping(value = {"/{id}/contents/{contentid}"}, method = RequestMethod.GET)
	public Result findContentById(
					@PathVariable(value="id") Integer id,
					@PathVariable(value="contentid") Integer contentid) {
		Optional<DocumentContent> ret = documentService.findContentById(id,contentid);
		return Result.success(ret.orElse(null));
	}

//	@RequiresPermissions("document:view")
	@RequestMapping(value = {"/{id}/comments"}, method = RequestMethod.GET)
	public Result findCommentListById(
					@PathVariable(value="id") Integer id) {
		List<DocumentComment> ret = documentService.findCommentListByDocid(id);
		return Result.success(ret);
	}
	
	@RequestMapping(value = {"/tags/name-page-list"}, method = RequestMethod.GET)
	public Result findTagList(
					@RequestParam(value="name", required=false) String name,
					@RequestParam(value="sortby", defaultValue="id") String sortby,
					@RequestParam(value="page", defaultValue="0") Integer page) {
		Sort sort = new Sort(Sort.Direction.DESC, sortby);
		Pageable pageable = PageRequest.of(page, 50, sort);
		Page<Tag> ret = tagService.findListByNameOrPinyinLike(name, pageable);
		return Result.success(ret);
	}
	
//	@RequiresPermissions("document:view")
	@RequestMapping(value = {"/uuid-{uuid}"}, method = RequestMethod.GET)
	public Result findByUuid(
					@PathVariable(value="uuid") String uuid,
					@RequestParam(value="contentid", required=false) Integer contentid) {
		DocumentDto ret = documentService.findByUuid(uuid);
		Document document = ret.getDocument();

		//HIDE FilePath;
		if(document!=null&&document.getFilePath()!=null) {
			String hidepath = "|";
			if("doc".equals(FileUtil.getFileType(document.getFilePath()))){
				hidepath = hidepath + "nockeditor|";//do not display in ckeditor ;
			}
			ret.getDocument().setFilePath(hidepath+"hasdownload|");
		}
		UserVO creator = ret.getCreator();
		Long creatorid = 0L;
		if (creator!=null) {
			creatorid = creator.getUserId();
		}
		if(!authLoginService.isCurrentUserOrSuperUser(creatorid,null)) {
			ret.setContentList(null);
		}

		if (contentid!=null&&contentid>0) {
			if (ret.getContent()!=null&&!contentid.equals(ret.getContent().getId())) {
				ret.setDocStatus(DocumentStatus.EXPIRED);
			}
			Optional<DocumentContent> odc = documentService.findContentById(ret.getId(),contentid);
			DocumentContent dc = odc.orElse(new DocumentContent());
			ret.setContent(dc);
		}

		return Result.success(ret);
	}
	
	@RequestMapping(value = {"/tag-page-list"}, method = RequestMethod.GET)
	public Result findListByTag(
			@RequestParam(value="tag", required=false) String tag,
					@RequestParam(value="sortby", defaultValue="doc_id") String sortby,
					@RequestParam(value="page", defaultValue="0") Integer page) {
		Sort sort = new Sort(Sort.Direction.DESC, sortby);
		Pageable pageable = PageRequest.of(page, SysconfigConstants.DEFAULT_PAGE_SIZE, sort);
		Page<Document> ret = documentService.findDocumentListByTagName(StringUtil.safeToString(tag),pageable);
		return Result.success(ret);
	}
	
	@RequestMapping(value = {"/name-page-list"}, method = RequestMethod.GET)
	public Result findListByNameLike(
					@RequestParam(value="name", required=false) String name,
					@RequestParam(value="sortby", defaultValue="id") String sortby,
					@RequestParam(value="page", defaultValue="0") Integer page) {
		Sort sort = new Sort(Sort.Direction.DESC, sortby);
		Pageable pageable = PageRequest.of(page, SysconfigConstants.DEFAULT_PAGE_SIZE, sort);
		Page<Document> ret = documentService.findDocumentListByNameOrPinyinLike(StringUtil.safeToString(name),pageable);
		return Result.success(ret);
	}
	
	@RequestMapping(value = {"/my-page-list"}, method = RequestMethod.GET)
	public Result findMyDocumentList(
					@RequestParam(value="sortby", defaultValue="id") String sortby,
					@RequestParam(value="page", defaultValue="0") Integer page) {
		Sort sort = new Sort(Sort.Direction.DESC, sortby);
		Pageable pageable = PageRequest.of(page, SysconfigConstants.DEFAULT_PAGE_SIZE, sort);
    	UserVO currentuser = authLoginService.findLoginCacheCurrent();
		String userid= "0";
		if (currentuser!=null) {
			userid = StringUtil.safeToString(currentuser.getUserId());
		}
		Page<Document> ret = documentService.findDocumentListByCreatedBy(userid,pageable);
		return Result.success(ret);
	}
		
	@RequestMapping(value = { "/{uuid}/file" }, method = RequestMethod.GET)
	public ResponseEntity<byte[]> findEntityByFilename(
					@PathVariable(value="uuid") String uuid) {
		ResponseEntity<byte[]> ret = null;
		byte[] fileBody = null;
		Optional<Document> od = documentService.findDocumentByUuid(uuid);
		if (!od.isPresent()) {
			return ret;
		}
		fileBody = FileUtil.file2Bytes(od.get().getFilePath());

		if (fileBody == null) {
			return ret;
		}
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Disposition", "attachment;filename=" + od.get().getName());
		HttpStatus statusCode = HttpStatus.OK;
		ret = new ResponseEntity<byte[]>(fileBody, headers, statusCode);
		return ret;
	}

	@RequiresPermissions("document:init")
	@RequestMapping(value = {"/init"}, method = RequestMethod.GET)
	public Result initDocuments() {
		successdircount = 0;
		sucessfilecount = 0;
		initDocByDir(properties.DOCUMENTS_DIR,0,"");
		return Result.success(successdircount+" dirs; "+sucessfilecount+" new files created;");
	}
	
	private int successdircount = 0;
	private int sucessfilecount = 0;
	private void initDocByDir(String dirpath, Integer parentid, String parentname) {
		File file = new File(dirpath);
		File[] files = file.listFiles();
		if (files == null) {
			return;
		}

		for (File f : files) {
			if (f.isFile()) {
				//do some file thing; tag = parentid,
//				System.err.println("file:"+f.getAbsolutePath());
				
				initDocByFileWithDir(f, parentid, parentname);
				
			} if (f.isDirectory()) {
				
//				System.err.println(""+parentid+"-"+f.getName());
				//do some insert get a new parentid
				Integer newparentid;
				String newparentname;
				Tag newtag = tagService.createTag(f.getName(), TagType.DOCUMENT_FILEDIR, parentid);
				if (newtag!=null) {
					successdircount++;
					newparentid = newtag.getId();
					newparentname = newtag.getName();
					initDocByDir(f.getAbsolutePath(),newparentid,newparentname);
				}
				
				
			}
		}
	}
	private Document initDocByFileWithDir(File file, Integer parentid, String parentname) {
		Document ret = null;
		try {
			String filePath = file.getAbsolutePath();
			//mdf file
			String md5 = TokenUtil.md5(file);
			Document example = new Document();
			example.setFilePath(filePath);
			List<Document> doclist = documentService.findDocumentListByExample(example);
			if(doclist!=null&&doclist.size()>0) {
				example.setFileMd5(md5);
				doclist = documentService.findDocumentListByExample(example);
				if(doclist!=null&&doclist.size()>0) {
					LogManager.getLogger().error(file.getName()+":exist content.");
					return ret;
				} else {
					//update content;
							
				}
			}

			String content = OfficeReader.extractHtml(file.getAbsolutePath(), PicturesManager());
			DocumentDto documentDto = new DocumentDto();
			Document document = new Document();
			DocumentContent documentContent = new DocumentContent();
			document.setName(file.getName());
			document.setFilePath(file.getAbsolutePath());
			document.setFileMd5(md5);
			documentContent.setHtml(content);
			documentDto.setDocument(document);
			documentDto.setContent(documentContent);
			ret = documentService.create(documentDto);
			if (ret!=null&&ret.getId()!=null&&ret.getId()>0) {
				documentService.createTag(ret.getId(), parentid, parentname);
				sucessfilecount++;
			}
		} catch (Exception e) {
			LogManager.getLogger().error("",e);
		}
		return ret;
	}
	

	private PicturesManager PicturesManager() {
		return new PicturesManager() {
			public String savePicture(byte[] content, PictureType pictureType, String suggestedName, float widthInches,
					float heightInches) {
				String ret = "";
				try {
					//do some upload data;
					String name = "document.image."+pictureType.getExtension();
					ObjectHeader objectHeader =  objectStorageService.createImage(content, name);
					// <img src="/oss/objects/images/774fd40d84414b03b7aa6be1482b529a">
				    ret = properties.CONTEXT_PATH + ObjectHeader.DOWNLOAD_IMAGE_URL+objectHeader.getFileName();
				    LogManager.getLogger().debug("suggestedName:"+suggestedName);
				} catch (Exception e) {
					LogManager.getLogger().error("",e);
				}
				suggestedName = ret;
				return suggestedName;
			}
		};
	}
	
}
