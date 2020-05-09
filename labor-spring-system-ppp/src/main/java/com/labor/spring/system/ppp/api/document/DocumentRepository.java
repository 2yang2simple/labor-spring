package com.labor.spring.system.ppp.api.document;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.labor.spring.system.ppp.entity.document.Document;
import com.labor.spring.system.ppp.entity.tag.Tag;


public interface DocumentRepository extends JpaRepository<Document,Long> {
	

	public Optional<Document> findOneByUuid(String uuid);
	public Optional<Document> findOneById(Integer id);
	
	public Page<Document> findByNameStartingWith(String name,Pageable pageable);
	public Page<Document> findByNameContainingIgnoreCase(String name,Pageable pageable);
	public Page<Document> findByNameContainingIgnoreCaseOrNamePinyinContainingIgnoreCase(
							String name, String namePinyin, Pageable pageable);

	public Page<Document> findByCreatedBy(String userid,Pageable pageable);
	
	@Query(value = "SELECT t1.* FROM tbl_doc t1 INNER JOIN tbl_doc_tag t2 WHERE t1.doc_id = t2.doc_id AND tag_name = ?1",
		    countQuery = "SELECT COUNT(t1.doc_id) FROM tbl_doc t1 INNER JOIN tbl_doc_tag t2 WHERE t1.doc_id = t2.doc_id AND tag_name = ?1",
		    nativeQuery = true)
	public Page<Document> findByTagName(String tagName, Pageable pageable);

}
