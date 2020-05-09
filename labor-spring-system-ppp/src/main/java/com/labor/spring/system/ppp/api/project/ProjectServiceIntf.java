package com.labor.spring.system.ppp.api.project;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import com.labor.spring.system.ppp.api.document.DocumentDto;
import com.labor.spring.system.ppp.entity.document.Document;
import com.labor.spring.system.ppp.entity.project.Project;
import com.labor.spring.system.ppp.entity.project.ProjectDocument;

public interface ProjectServiceIntf {
	
	public ProjectDto create(Project entity);
	public Project updateProject(Integer id, Project entity);
	public Project updateProjectStatus(Integer id, String status);
	
	public Document createProjectDocument(Integer projectId, String pdType, DocumentDto documentDto);
	public Document createProjectDocument(String projectUuid, String pdType, DocumentDto documentDto);
	public ProjectDocument updateProjectDocumentStatus(Integer projectId, Integer docId, String pdType, String pdStatus);
	
	public ProjectDto findById(Integer id);
	public ProjectDto findByUuid(String uuid);
	public Page<Project> findListByNameOrPinyinLike(String name, Pageable pageable);
	public Page<Project> findListByNameOrPinyinLikeAndStatus(String name, String status, Pageable pageable);
	public Page<Project> findListByNameOrPinyinLikeAndStatusNot(String name, String status, Pageable pageable);
	public List<Project> findListByNameOrPinyinLikeAndStatusNot(String name, String status);
	public List<Document> findDocumentList(Integer id, String type, String status);
	public List<Document> findDocumentListByStatusNot(Integer id, String type, String status);
	public Optional<ProjectDocument> findDocument(Integer projectId, Integer docId, String pdType, String pdStatus);
	public Optional<Project> findProjectByUuid(String uuid);

}
