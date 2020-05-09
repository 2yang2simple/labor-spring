package com.labor.spring.system.ppp.api.product;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.labor.spring.system.ppp.entity.product.ProductDocument;

@Repository
public interface ProductDocumentRepository extends JpaRepository<ProductDocument,Long> {
	
	public Optional<ProductDocument> findOneById(Integer id);
	public Optional<ProductDocument> findOneByProductIdAndDocId(Integer productId, Integer docId);

	
	
}
