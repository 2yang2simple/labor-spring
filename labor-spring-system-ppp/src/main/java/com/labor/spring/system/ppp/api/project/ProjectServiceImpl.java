package com.labor.spring.system.ppp.api.project;

import java.util.List;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import com.labor.common.constants.CommonConstants;
import com.labor.common.exception.ServiceException;

import com.labor.common.util.StringUtil;
import com.labor.common.util.TokenUtil;
import com.labor.spring.system.ppp.HanLPExtractor;
import com.labor.spring.system.ppp.api.document.DocumentDto;
import com.labor.spring.system.ppp.api.document.DocumentServiceIntf;
import com.labor.spring.system.ppp.api.gallery.GalleryServiceIntf;
import com.labor.spring.system.ppp.api.project.ProjectRepository;
import com.labor.spring.system.ppp.api.tag.TagType;
import com.labor.spring.system.ppp.entity.document.Document;
import com.labor.spring.system.ppp.entity.document.DocumentContent;
import com.labor.spring.system.ppp.entity.document.DocumentTag;
import com.labor.spring.system.ppp.entity.gallery.Gallery;
import com.labor.spring.system.ppp.entity.project.Project;
import com.labor.spring.system.ppp.entity.project.ProjectDocument;
import com.labor.spring.system.ppp.entity.project.ProjectGallery;
import com.labor.spring.util.IgnorePropertiesUtil;

@Service
public class ProjectServiceImpl implements ProjectServiceIntf {

	@Autowired
	private ProjectRepository projectRepository;
	@Autowired
	private ProjectGalleryRepository projectGalleryRepository;
	@Autowired
	private ProjectDocumentRepository projectDocumentRepository;
	@Autowired
	private ProjectDocumentFinderRepository projectDocumentFinderRepository;
	@Autowired
	private DocumentServiceIntf documentService;
	@Autowired
	private GalleryServiceIntf galleryService;

	private Project executeCreateProject(Project entity) {
		Project ret = null;
		if (entity!=null) {
			
			entity.setId(null);
			
			entity.setNamePinyin(HanLPExtractor.pinyin(entity.getName()));
			entity.setStatus(CommonConstants.ACTIVE);
			entity.setProjectStatus(ProjectStatus.OPENED);
			entity.setUuid(TokenUtil.generateUUID());
//			entity.setCode("P"+TokenUtil.generateDateNum());
			Example<Project> example =Example.of(new Project());
			long pcount = projectRepository.count(example);
			entity.setCode("PJ"+StringUtil.prefixZero(Long.toString(pcount+1), 5));
			ret = projectRepository.save(entity);
		}
		return ret;
	}
	
	private Project executeUpdateProject(Integer id, Project entity, boolean ignoreNullProperties) {
		Project newEntity = entity;
		Optional<Project> optional = projectRepository.findOneById(id);
		if (optional.isPresent()){
			if (ignoreNullProperties) {
				newEntity = optional.get();
				BeanUtils.copyProperties(entity,newEntity,IgnorePropertiesUtil.getNullPropertyNames(entity));
			}
			if(!StringUtil.isEmpty(entity.getName())) {
				newEntity.setNamePinyin(HanLPExtractor.pinyin(newEntity.getName()));
			}
			return projectRepository.save(newEntity);
		}
		return null;
	}	

	@Override
	@Transactional
	public ProjectDto create(Project entity) {
		ProjectDto ret = new ProjectDto();
		Project project  = executeCreateProject(entity);
		if (project==null) {
			return null;
		}
		ret.setProject(project);
		
		//create a gallery;
		Gallery gallery = new Gallery();
		gallery.setName("["+project.getCode()+"]");
		gallery.setStatus(CommonConstants.ACTIVE);
		gallery = galleryService.create(gallery);
		if(gallery==null||gallery.getId()==null||gallery.getId()==0) {
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly(); 
			LogManager.getLogger().error("create default gallery error, rollback.");
			return null;
		}
//		ret.setDefaultGalleryId(gallery.getId());
		
		//create default gallery only here, keep unique.
		ProjectGallery pg = new ProjectGallery();
		pg.setId(null);
		pg.setGaId(gallery.getId());
		pg.setProjectId(project.getId());
		pg.setPgType(ProjectType.GALLERY_DEFAULT);
		pg.setPgStatus(ProjectStatus.GALLERY_OPENED);
		pg = projectGalleryRepository.save(pg);
		if(pg==null||pg.getId()==null||pg.getId()==0) {
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly(); 
			LogManager.getLogger().error("create project gallery error, rollback.");
			return null;
		}
		
		//create a document;
		DocumentDto documentDto = new DocumentDto();
		Document document = new Document();
		DocumentContent documentContent = new DocumentContent();
		document.setName("["+project.getCode()+"]"+project.getName());
		documentContent.setHtml("Write some details here.");
		documentDto.setDocument(document);
		documentDto.setContent(documentContent);
		document = documentService.create(documentDto);
		if(document==null||document.getId()==null||document.getId()==0) {
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly(); 
			LogManager.getLogger().error("create default document error, rollback.");
			return null;
		}
//		ret.setDefaultDocumentId(document.getId());
//		documentDto = documentService.findById(document.getId());
//		ret.setDefaultDocumentDto(documentDto);

		//create a project tag to the document.
		documentService.createTag(document.getId(), project.getCode(), TagType.DOCUMENT_PROJECT);
		
		//create default document only here, keep unique.
		ProjectDocument pd = new ProjectDocument();
		pd.setId(null);
		pd.setProjectId(project.getId());
		pd.setDocId(document.getId());
		pd.setPdType(ProjectType.DOCUMENT_DEFAULT);
		pd.setPdStatus(ProjectStatus.DOCUMENT_OPENED);
		pd = projectDocumentRepository.save(pd);
		if(pd==null||pd.getId()==null||pd.getId()==0) {
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly(); 
			LogManager.getLogger().error("create project document error, rollback.");
			return null;
		}
		
		return ret;
	}
	@Override
	@Transactional
	public Document createProjectDocument(String projectUuid, String pdType, DocumentDto documentDto) {
		Optional<Project> optional = projectRepository.findOneByUuid(projectUuid);
		return createProjectDocument(optional.get().getId(),pdType,documentDto);
	}

	@Override
	@Transactional
	public Document createProjectDocument(Integer projectId, String pdType, DocumentDto documentDto) {
		Document ret = null;

		Optional<Project> optional = projectRepository.findOneById(projectId);
		if (optional.isPresent()){
			Project project = optional.get();
			ret = documentService.create(documentDto);
			//create a project tag to the document.
			DocumentTag dt = null;
			dt = documentService.createTag(ret.getId(), project.getCode(), TagType.DOCUMENT_PROJECT);
			dt = documentService.createTag(ret.getId(), project.getCode()+"-todo", TagType.DOCUMENT_PROJECT);
			//create project document map.
			ProjectDocument pd = new ProjectDocument();
			pd.setDocId(ret.getId());
			pd.setProjectId(project.getId());
			pd.setPdType(pdType);
			pd.setPdStatus(ProjectStatus.DOCUMENT_OPENED);
			ProjectDocument newpd = projectDocumentRepository.save(pd);
			if (ret==null||dt==null||newpd==null) {
				TransactionAspectSupport.currentTransactionStatus().setRollbackOnly(); 
				LogManager.getLogger().error("create project document error, rollback.");
				return null;
			}
		}

		return ret;
	}

	@Override
	@Transactional
	public ProjectDocument updateProjectDocumentStatus(
				Integer projectId, Integer docId, String pdType, String pdStatus) {
		ProjectDocument pd = new ProjectDocument();
		ProjectDocument dbpd = projectDocumentRepository.findOneByProjectIdAndDocId(projectId, docId).orElse(null);
		if (dbpd==null) {
			return null;
		}
		pd.setId(dbpd.getId());
		pd.setProjectId(projectId);
		pd.setDocId(docId);		
		pd.setPdType(pdType);
		pd.setPdStatus(pdStatus);
		return projectDocumentRepository.save(pd);
	}

	@Override
	@Transactional
	public Project updateProjectStatus(Integer id, String status) {
		Project project = new Project();
		project.setId(id);
		project.setProjectStatus(status);
		return executeUpdateProject(id,project,true);
	}
	
	@Override
	@Transactional
	public Project updateProject(Integer id, Project entity) {
		return executeUpdateProject(id,entity,true);
	}
	@Override
	public ProjectDto findById(Integer id) {
		Project project = projectRepository.findOneById(id).orElse(null);
		if (project==null) {
			return null;
		}
		return findByUuid(project.getUuid());
	}
	@Override
	public ProjectDto findByUuid(String uuid) {
		ProjectDto ret = new ProjectDto();
		Project project = projectRepository.findOneByUuid(uuid).orElse(null);
		if (project==null) {
			return null;
		}
		ret.setProject(project);
		
		ProjectDocument pd = projectDocumentRepository.findFirstByProjectIdAndPdTypeOrderByIdDesc(
				project.getId(), ProjectType.DOCUMENT_DEFAULT).get();
		
		ret.setDefaultDocumentId(pd.getDocId());
		ret.setDefaultDocumentDto(documentService.findById(pd.getDocId()));

		ProjectGallery pg = projectGalleryRepository.findFirstByProjectIdAndPgTypeOrderByIdDesc(
				project.getId(), ProjectType.GALLERY_DEFAULT).get();
		ret.setDefaultGalleryId(pg.getGaId());
		Gallery gallery = galleryService.findById(pg.getGaId()).orElse(new Gallery());
		ret.setDefaultGalleryUuid(gallery.getUuid());
		
		List<Document> todoList = projectDocumentFinderRepository
				.findDocumentListByStatusNot(project.getId(), ProjectType.DOCUMENT_TODO, ProjectStatus.DOCUMENT_CLOSED);
		ret.setTodoList(todoList);
		return ret;
	}

	@Override
	public Page<Project> findListByNameOrPinyinLike(String name, Pageable pageable) {
		return projectRepository.findByNameContainingIgnoreCaseOrNamePinyinContainingIgnoreCase(name, name, pageable);
	}
	@Override
	public Page<Project> findListByNameOrPinyinLikeAndStatus(String name, String status, Pageable pageable){
		return projectRepository.findByNameContainingIgnoreCaseAndProjectStatusOrNamePinyinContainingIgnoreCaseAndProjectStatus(name, status, name, status, pageable);
	}
	@Override
	public Page<Project> findListByNameOrPinyinLikeAndStatusNot(String name, String status, Pageable pageable){
		return projectRepository.findByNameContainingIgnoreCaseAndProjectStatusNotOrNamePinyinContainingIgnoreCaseAndProjectStatusNot(name, status, name, status, pageable);
	}
	@Override
	public List<Project> findListByNameOrPinyinLikeAndStatusNot(String name, String status){
		return projectRepository.findByNameContainingIgnoreCaseAndProjectStatusNotOrNamePinyinContainingIgnoreCaseAndProjectStatusNot(name, status, name, status);
	}
	@Override
	public List<Document> findDocumentList(Integer id, String type, String status) {
		if (StringUtil.isEmpty(status)) {
			return projectDocumentFinderRepository.findDocumentList(id, type);
		} else {
			return projectDocumentFinderRepository.findDocumentList(id, type, status);
		}		
	}
	@Override
	public List<Document> findDocumentListByStatusNot(Integer id, String type, String status) {
		if (StringUtil.isEmpty(status)) {
			return projectDocumentFinderRepository.findDocumentList(id, type);
		} else {
			return projectDocumentFinderRepository.findDocumentListByStatusNot(id, type, status);
		}		
	}
	@Override
	public Optional<ProjectDocument> findDocument(Integer projectId, Integer docId, String pdType, String pdStatus){
//		return projectDocumentRepository.findFirstByProjectIdAndDocIdAndPdTypeAndPdStatusOrderByIdDesc(projectId, docId, pdType, pdStatus);
		if (!StringUtil.isEmpty(pdType)) {
			if (!StringUtil.isEmpty(pdStatus)) {
				return projectDocumentRepository.findFirstByProjectIdAndDocIdAndPdTypeAndPdStatusOrderByIdDesc(projectId, docId, pdType, pdStatus);
			} else {
				return projectDocumentRepository.findFirstByProjectIdAndDocIdAndPdStatusOrderByIdDesc(projectId, docId, pdType);
			}
		} else {
			if (!StringUtil.isEmpty(pdStatus)) {
				return projectDocumentRepository.findFirstByProjectIdAndDocIdAndPdStatusOrderByIdDesc(projectId, docId, pdStatus);
			} else {
				return projectDocumentRepository.findFirstByProjectIdAndDocIdOrderByIdDesc(projectId, docId);
			}
		}
		
	}

	@Override
	public Optional<Project> findProjectByUuid(String uuid){
		return projectRepository.findOneByUuid(uuid);
	}
}
