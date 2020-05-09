package com.labor.spring.system.ppp.api.product;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.labor.spring.system.ppp.entity.product.ProductGallery;

@Repository
public interface ProductGalleryRepository extends JpaRepository<ProductGallery,Long> {
	
	public Optional<ProductGallery> findOneById(Integer id);
	public Optional<ProductGallery> findOneByProductIdAndGaId(Integer projectId, Integer gaId);

}
