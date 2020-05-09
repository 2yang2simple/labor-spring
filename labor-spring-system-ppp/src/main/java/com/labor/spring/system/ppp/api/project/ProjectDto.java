package com.labor.spring.system.ppp.api.project;

import java.util.List;

import com.labor.spring.system.ppp.api.document.DocumentDto;
import com.labor.spring.system.ppp.entity.document.Document;
import com.labor.spring.system.ppp.entity.project.Project;

public class ProjectDto {

	private Project project;
	
	private List<Document> todoList;
	
	private Integer defaultGalleryId;
	
	private String defaultGalleryUuid;
	
	private Integer defaultDocumentId;
	
	private DocumentDto defaultDocumentDto;

	public Project getProject() {
		return project;
	}

	public void setProject(Project project) {
		this.project = project;
	}

	public Integer getDefaultGalleryId() {
		return defaultGalleryId;
	}

	public void setDefaultGalleryId(Integer defaultGalleryId) {
		this.defaultGalleryId = defaultGalleryId;
	}

	public Integer getDefaultDocumentId() {
		return defaultDocumentId;
	}

	public void setDefaultDocumentId(Integer defaultDocumentId) {
		this.defaultDocumentId = defaultDocumentId;
	}

	public DocumentDto getDefaultDocumentDto() {
		return defaultDocumentDto;
	}

	public void setDefaultDocumentDto(DocumentDto defaultDocumentDto) {
		this.defaultDocumentDto = defaultDocumentDto;
	}

	public String getDefaultGalleryUuid() {
		return defaultGalleryUuid;
	}

	public void setDefaultGalleryUuid(String defaultGalleryUuid) {
		this.defaultGalleryUuid = defaultGalleryUuid;
	}

	public List<Document> getTodoList() {
		return todoList;
	}

	public void setTodoList(List<Document> todoList) {
		this.todoList = todoList;
	}
	
	
}
