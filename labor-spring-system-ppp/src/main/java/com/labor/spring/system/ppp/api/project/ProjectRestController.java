package com.labor.spring.system.ppp.api.project;

import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.labor.common.util.FileUtil;
import com.labor.common.util.StringUtil;
import com.labor.spring.bean.LoginCache;
import com.labor.spring.bean.Result;
import com.labor.spring.bean.ResultCode;
import com.labor.spring.core.entity.User;
import com.labor.spring.core.service.SysconfigConstants;
import com.labor.spring.feign.auth.AuthLoginService;
import com.labor.spring.system.ppp.api.document.DocumentDto;
import com.labor.spring.system.ppp.api.document.DocumentServiceIntf;
import com.labor.spring.system.ppp.api.document.DocumentStatus;
import com.labor.spring.system.ppp.api.oss.ObjectStorageServiceIntf;
import com.labor.spring.system.ppp.api.tag.TagType;
import com.labor.spring.system.ppp.entity.document.Document;
import com.labor.spring.system.ppp.entity.document.DocumentComment;
import com.labor.spring.system.ppp.entity.document.DocumentContent;
import com.labor.spring.system.ppp.entity.gallery.Gallery;
import com.labor.spring.system.ppp.entity.project.Project;
import com.labor.spring.system.ppp.entity.project.ProjectDocument;
import com.labor.spring.system.ppp.util.ApplicationProperties;
import com.labor.spring.util.WebUtil;

@RestController
@RequestMapping("/rest/projects")
public class ProjectRestController {

	@Autowired
	private ApplicationProperties properties;
	@Autowired
	private DocumentServiceIntf documentService;
	@Autowired
	private ProjectServiceIntf projectService;	
	@Autowired
	private AuthLoginService authLoginService;
	
	@RequiresPermissions("project:create")
	@RequestMapping(value = {""}, method = RequestMethod.POST)
	public Result create(
					@RequestBody @Valid Project project, 
					BindingResult bindingResult) {
	    if (bindingResult.hasErrors()) {
	        String message = bindingResult.getFieldError().getDefaultMessage();
	        return Result.failure(ResultCode.FAILURE_PARAM_INVALID,message);
	    }
	    ProjectDto ret = projectService.create(project);
		return Result.success(ret);
	}
	@RequiresPermissions("project:edit")
	@RequestMapping(value = {"/{id}"}, method = RequestMethod.PUT)
	public Result updateProject(
					@PathVariable(value="id") Integer id, 
					@RequestBody @Valid Project project, 
					BindingResult bindingResult) {
	    if (bindingResult.hasErrors()) {
	        String message = bindingResult.getFieldError().getDefaultMessage();
	        return Result.failure(ResultCode.FAILURE_PARAM_INVALID,message);
	    }
	    Project ret = projectService.updateProject(id,project);
		return Result.success(ret);
	}
	
	@RequiresPermissions("project:memo")
	@RequestMapping(value = {"/{id}/status/closed"}, method = RequestMethod.PATCH)
	public Result updateProjectClosed(
					@PathVariable(value="id") Integer id) {
	    Project ret = projectService.updateProjectStatus(id,ProjectStatus.CLOSED);
		return Result.success(ret);
	}
	@RequiresPermissions("project:memo")
	@RequestMapping(value = {"/{id}/status/opened"}, method = RequestMethod.PATCH)
	public Result updateProjectOpened(
					@PathVariable(value="id") Integer id) {
		Project ret = projectService.updateProjectStatus(id,ProjectStatus.OPENED);
		return Result.success(ret);
	}

	@RequiresPermissions("project:create")
	@RequestMapping(value = {"/{id}/docs/{docid}/contents/{contentid}"}, method = RequestMethod.PUT)
	public Result updateContent(
					@PathVariable(value="id") Integer id, 
					@PathVariable(value="docid") Integer docid,
					@PathVariable(value="contentid") Integer contentid, 
					@RequestBody @Valid DocumentContent content, 
					BindingResult bindingResult) {
		DocumentContent ret = null;
		if (bindingResult.hasErrors()) {
	        String message = bindingResult.getFieldError().getDefaultMessage();
	        return Result.failure(ResultCode.FAILURE_PARAM_INVALID,message);
	    }
		User creator = documentService.findCreator(docid).orElse(null);
		if (creator==null||!authLoginService.isCurrentUserOrSuperUser(creator.getId(),null)) {
	    	return Result.failure(ResultCode.FAILURE_PERMISSION_NOACCESS, ResultCode.MSG_FAILURE_PERMISSION_NOACCESS);
	    }
		ProjectDocument pd = projectService.findDocument(id, docid, null,ProjectStatus.DOCUMENT_OPENED).orElse(null);
		if (pd==null) {
			return Result.failure(ResultCode.FAILURE_DATA_NOT_FOUND,ResultCode.MSG_FAILURE_DATA_NOT_FOUND);
		}
		content.setId(contentid);
		ret = documentService.updateContent(docid, contentid, content);
		return Result.success(ret);
	}

	@RequiresPermissions("project:edit")
	@RequestMapping(value = {"/{id}/docs/{docid}/contents"}, method = RequestMethod.POST)
	public Result createContent(
					@PathVariable(value="id") Integer id, 
					@PathVariable(value="docid") Integer docid,
					@RequestBody @Valid DocumentContent content, 
					BindingResult bindingResult) {
	    if (bindingResult.hasErrors()) {
	        String message = bindingResult.getFieldError().getDefaultMessage();
	        return Result.failure(ResultCode.FAILURE_PARAM_INVALID,message);
	    }
		User creator = documentService.findCreator(docid).orElse(null);
		if (!authLoginService.isCurrentUserOrSuperUser(creator.getId(),null)) {
	    	return Result.failure(ResultCode.FAILURE_PERMISSION_NOACCESS, ResultCode.MSG_FAILURE_PERMISSION_NOACCESS);
	    }
		ProjectDocument pd = projectService.findDocument(id, docid, null, ProjectStatus.DOCUMENT_OPENED).orElse(null);
		if (pd==null) {
			return Result.failure(ResultCode.FAILURE_DATA_NOT_FOUND,ResultCode.MSG_FAILURE_DATA_NOT_FOUND);
		}
	    DocumentContent ret = documentService.createContent(docid,content);
		return Result.success(ret);
	}
	
	@RequiresPermissions("project:edit")
	@RequestMapping(value = {"/{id}/docs/{docid}/comments"}, method = RequestMethod.POST)
	public Result createComment(
					@PathVariable(value="id") Integer id, 
					@PathVariable(value="docid") Integer docid,
					@RequestBody @Valid DocumentComment comment, 
					BindingResult bindingResult) {
	    if (bindingResult.hasErrors()) {
	        String message = bindingResult.getFieldError().getDefaultMessage();
	        return Result.failure(ResultCode.FAILURE_PARAM_INVALID,message);
	    }
		ProjectDocument pd = projectService.findDocument(id, docid, null, null).orElse(null);
		if (pd==null) {
			return Result.failure(ResultCode.FAILURE_DATA_NOT_FOUND,ResultCode.MSG_FAILURE_DATA_NOT_FOUND);
		}
	    if (comment!=null) {
	    	LoginCache currentuser = authLoginService.findLoginCacheCurrent();
	    	if (currentuser!=null) {
		    	comment.setCreator(currentuser.getUserName()+" "+currentuser.getUserRealName());
	    	}
	    }
	    DocumentComment ret = documentService.createComment(docid,comment);
		return Result.success(ret);
	}

	@RequiresPermissions("project:todo:create")
	@RequestMapping(value = {"/{uuid}/docs/todo"}, method = RequestMethod.POST)
	public Result createTodoDocument(
					@PathVariable(value="uuid") String uuid, 
					@RequestBody @Valid DocumentDto documentDto, 
					BindingResult bindingResult) {
	    if (bindingResult.hasErrors()) {
	        String message = bindingResult.getFieldError().getDefaultMessage();
	        return Result.failure(ResultCode.FAILURE_PARAM_INVALID,message);
	    }
	    Document ret = projectService.createProjectDocument(uuid, ProjectType.DOCUMENT_TODO,documentDto);
		return Result.success(ret);
	}
	
	@RequiresPermissions("project:todo:edit")
	@RequestMapping(value = {"/{id}/docs/todo/{docid}/status/closed"}, method = RequestMethod.PATCH)
	public Result updateTodoDocumentClosed(
					@PathVariable(value="id") Integer id, 
					@PathVariable(value="docid") Integer docid) {
	    ProjectDocument ret = projectService.updateProjectDocumentStatus(id, docid, ProjectType.DOCUMENT_TODO, ProjectStatus.DOCUMENT_CLOSED);
		return Result.success(ret);
	}
	@RequiresPermissions("project:todo:edit")
	@RequestMapping(value = {"/{id}/docs/todo/{docid}/status/opened"}, method = RequestMethod.PATCH)
	public Result updateTodoDocumentOpened(
					@PathVariable(value="id") Integer id, 
					@PathVariable(value="docid") Integer docid) {
	    ProjectDocument ret = projectService.updateProjectDocumentStatus(id, docid, ProjectType.DOCUMENT_TODO, ProjectStatus.DOCUMENT_OPENED);
		return Result.success(ret);
	}
	@RequestMapping(value = {"/uuid-{uuid}" }, method = RequestMethod.GET)
	public Result findByUuid(
					@PathVariable(value="uuid") String uuid) {
		return Result.success(projectService.findByUuid(uuid));
	}
	@RequestMapping(value = {"/name-page-list"}, method = RequestMethod.GET)
	public Result findListByNameLike(
					@RequestParam(value="name", defaultValue="") String name,
					@RequestParam(value="notinstatus", required=false) String notinstatus,
					@RequestParam(value="sortby", defaultValue="id") String sortby,
					@RequestParam(value="pagesize", required=false) Integer pagesize,
					@RequestParam(value="page", defaultValue="0") Integer page) {
		Sort sort = new Sort(Sort.Direction.DESC, sortby);
		if(pagesize==null) {
			pagesize = SysconfigConstants.DEFAULT_PAGE_SIZE;
		}
		Pageable pageable = PageRequest.of(page, pagesize, sort);
		name = StringUtil.safeToString(name);
		Page<Project> ret = null;
		if(notinstatus==null) {
			ret = projectService.findListByNameOrPinyinLike(name, pageable);
		} else {
			ret = projectService.findListByNameOrPinyinLikeAndStatusNot(name,notinstatus,pageable);
		}
		
		return Result.success(ret);
	}

	@RequestMapping(value = {"/name-list-non-closed"}, method = RequestMethod.GET)
	public Result findListNonClosedByNameLike(
					@RequestParam(value="name", defaultValue="") String name) {
		List<Project> ret = projectService.findListByNameOrPinyinLikeAndStatusNot(name,ProjectStatus.CLOSED);
		return Result.success(ret);
	}

	@RequestMapping(value = {"/{id}/docs/todo-list"}, method = RequestMethod.GET)
	public Result findTodoDocumentList(
					@PathVariable(value="id") Integer id) {
		List<Document> ret = projectService.findDocumentList(id, ProjectType.DOCUMENT_TODO, null);
		return Result.success(ret);
	}
	
	@RequestMapping(value = {"/{id}/docs/todo-list-non-closed"}, method = RequestMethod.GET)
	public Result findTodoDocumentListOpened(
					@PathVariable(value="id") Integer id) {
		List<Document> ret = projectService.findDocumentListByStatusNot(id, 
						ProjectType.DOCUMENT_TODO, ProjectStatus.DOCUMENT_CLOSED);
		return Result.success(ret);
	}

	@RequestMapping(value = {"/{id}/docs/{docid}/contents/{contentid}"}, method = RequestMethod.GET)
	public Result findDocumentContentById(
					@PathVariable(value="id") Integer id,
					@PathVariable(value="docid") Integer docid,
					@PathVariable(value="contentid") Integer contentid) {
		Optional<DocumentContent> ret = documentService.findContentById(docid,contentid);
		return Result.success(ret.orElse(null));
	}

	@RequestMapping(value = {"/{id}/docs/{docid}/comments"}, method = RequestMethod.GET)
	public Result findDocumentCommentListById(
					@PathVariable(value="id") Integer id,
					@PathVariable(value="docid") Integer docid) {
		List<DocumentComment> ret = documentService.findCommentListByDocid(docid);
		return Result.success(ret);
	}
	@RequestMapping(value = {"/{id}/docs/uuid-{docuuid}"}, method = RequestMethod.GET)
	public Result findDocumentByUuid(
			@PathVariable(value="id") Integer id,
			@PathVariable(value="docuuid") String docuuid) {
		
		DocumentDto ret = documentService.findByUuid(docuuid);

		if (ret==null) {
			return Result.failure(ResultCode.FAILURE_DATA_NOT_FOUND,ResultCode.MSG_FAILURE_DATA_NOT_FOUND);
		} else {
			ProjectDocument pd = projectService.findDocument(id, ret.getId(), null, null).orElse(null);
			if (pd==null) {
				return Result.failure(ResultCode.FAILURE_DATA_NOT_FOUND,ResultCode.MSG_FAILURE_DATA_NOT_FOUND);
			} else {
				ret.setDocStatus(pd.getPdStatus());
			}
		}
		Document document = ret.getDocument();
		//HIDE FilePath;
		if(document!=null&&document.getFilePath()!=null) {
			String hidepath = "|";
			if("doc".equals(FileUtil.getFileType(document.getFilePath()))){
				hidepath = hidepath + "nockeditor|";//do not display in ckeditor ;
			}
			ret.getDocument().setFilePath(hidepath+"hasdownload|");
		}
		
		User creator = ret.getCreator();
		Long creatorid = 0L;
		if (creator!=null) {
			creatorid = creator.getId();
		}
		if(!authLoginService.isCurrentUserOrSuperUser(creatorid,null)) {
			ret.setContentList(null);
		}
		
		return Result.success(ret);
	}
}
