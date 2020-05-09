package com.labor.spring.system.ppp.api.document;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.labor.spring.system.ppp.entity.document.DocumentContent;

@Repository
public interface DocumentContentRepository extends JpaRepository<DocumentContent,Long> {

	public DocumentContent findFirstByDocIdOrderByIdDesc(Integer docId);
	public List<DocumentContent> findByDocIdOrderByIdDesc(Integer docId);
	
	@Query(value = "SELECT t1.dcon_id, t1.doc_id, t1.dcon_md5, \r\n" + 
			"       '' dcon_html, '' dcon_text, \r\n" +
			"       t1.active_status,\r\n" + 
			"	    t1.data_description,\r\n" + 
			"	    t1.data_uuid,\r\n" + 
			"	    t1.creation_date,\r\n" + 
			"	    t1.created_by,\r\n" + 
			"	    t1.last_update_date,\r\n" + 
			"	    t1.last_updated_by FROM tbl_doc_content t1 WHERE doc_id = ?1 ORDER BY dcon_id DESC ", nativeQuery = true)
	public List<DocumentContent> findVersionsByDocIdOrderByIdDesc(Integer docId);
	
	public Optional<DocumentContent> findOneByDocIdAndId(Integer docId, Integer contentId);
	
	public List<DocumentContent> findByDocIdAndMd5(Integer docId, String md5);
}
