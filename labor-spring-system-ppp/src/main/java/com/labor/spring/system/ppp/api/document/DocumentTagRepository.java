package com.labor.spring.system.ppp.api.document;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.labor.spring.system.ppp.entity.document.DocumentTag;

@Repository
public interface DocumentTagRepository extends JpaRepository<DocumentTag,Long> {
	
//	@Query(value = "SELECT t1.* FROM tbl_doc_tag t1 INNER JOIN tbl_tag t2 ON t1.tag_id = t2.tag_id " 
//			+ "WHERE t1.doc_id = ?1 AND t2.tag_type = ?2 ", nativeQuery = true) 
//	public List<DocumentTag> findByDocIdAndType(Integer docId, String tagType);
//	
//	@Modifying
//	@Query(value = "DELETE t1 FROM tbl_doc_tag t1 INNER JOIN tbl_tag t2 ON t1.tag_id = t2.tag_id "
//			+ "WHERE t1.doc_id = ?1 ANDt2.tag_type = ?2 ", nativeQuery = true)
//	public int deleteByDocIdAndType(Integer docId, String tagType);
	
	
	public int deleteByDocIdAndTagType(Integer docId, String tagType);
	
	public List<DocumentTag> findByDocId(Integer docId);
	public List<DocumentTag> findByDocIdAndTagType(Integer docId, String tagType);
	public List<DocumentTag> findByDocIdAndTagTypeIn(Integer docId, Collection<String> tagTypes);
	
}
