package com.labor.spring.system.ppp.api.product;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.labor.spring.system.ppp.entity.document.Document;


@Repository
public interface ProductDocumentFinderRepository extends JpaRepository<Document,Long> {


	@Query(value = 					 "SELECT t1.* FROM tbl_doc t1 INNER JOIN tbl_product_doc t2 WHERE t1.doc_id = t2.doc_id AND t2.prod_id = ?1 ", nativeQuery = true)
	public List<Document> findDocumentList(Integer productId);
	
	@Query(value = 					 "SELECT t1.* FROM tbl_doc t1 INNER JOIN tbl_product_doc t2 WHERE t1.doc_id = t2.doc_id AND t2.prod_id = ?1 ", 
			countQuery = "SELECT count(t1.doc_id) FROM tbl_doc t1 INNER JOIN tbl_product_doc t2 WHERE t1.doc_id = t2.doc_id AND t2.prod_id = ?1 ", 
			nativeQuery = true)
	public Page<Document> findDocumentList(Integer productId, Pageable pageable);
	
}
