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
public interface ProjectDocumentFinderRepository extends JpaRepository<Document,Long> {
	
	@Query(value = "SELECT t1.* FROM tbl_doc t1 INNER JOIN tbl_project_doc t2 WHERE t1.doc_id = t2.doc_id AND t2.proj_id = ?1 AND t2.pd_type = ?2 AND t2.pd_status = ?3 order by t1.last_update_date desc ", nativeQuery = true)
	public List<Document> findDocumentList(Integer id, String pdType, String pdStatus);

	@Query(value = "SELECT t1.* FROM tbl_doc t1 INNER JOIN tbl_project_doc t2 WHERE t1.doc_id = t2.doc_id AND t2.proj_id = ?1 AND t2.pd_type = ?2 AND t2.pd_status <> ?3 order by t1.last_update_date desc ", nativeQuery = true)
	public List<Document> findDocumentListByStatusNot(Integer id, String pdType, String pdStatus);
	
	@Query(value = "SELECT t1.* FROM tbl_doc t1 INNER JOIN tbl_project_doc t2 WHERE t1.doc_id = t2.doc_id AND t2.proj_id = ?1 AND t2.pd_type = ?2 order by t2.pd_status desc ", nativeQuery = true)
	public List<Document> findDocumentList(Integer id, String pdType);
	
}
