package com.labor.spring.system.ppp.api.project;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.labor.spring.system.ppp.entity.document.Document;
import com.labor.spring.system.ppp.entity.project.ProjectDocument;

@Repository
public interface ProjectDocumentRepository extends JpaRepository<ProjectDocument,Long> {
	
	
	public Optional<ProjectDocument> findOneById(Integer id);
	public Optional<ProjectDocument> findOneByProjectIdAndDocId(Integer projectId, Integer docId);
	public Optional<ProjectDocument> findFirstByProjectIdAndPdTypeOrderByIdDesc(
										Integer projectId, String pdType);
	public Optional<ProjectDocument> findFirstByProjectIdAndDocIdOrderByIdDesc(
										Integer projectId, Integer docId);
	public Optional<ProjectDocument> findFirstByProjectIdAndDocIdAndPdTypeOrderByIdDesc(
										Integer projectId, Integer docId, String pdType);
	public Optional<ProjectDocument> findFirstByProjectIdAndDocIdAndPdStatusOrderByIdDesc(
										Integer projectId, Integer docId, String pdStatus);
	public Optional<ProjectDocument> findFirstByProjectIdAndDocIdAndPdTypeAndPdStatusOrderByIdDesc(
										Integer projectId, Integer docId, String pdType, String pdStatus);
	
	
}
