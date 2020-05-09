package com.labor.spring.system.ppp.api.product;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.labor.spring.system.ppp.entity.document.Document;
import com.labor.spring.system.ppp.entity.gallery.Gallery;
import com.labor.spring.system.ppp.entity.project.ProjectDocument;

@Repository
public interface ProductGalleryFinderRepository extends JpaRepository<Gallery,Long> {

	@Query(value = "SELECT t1.* FROM tbl_Gallery t1 INNER JOIN tbl_product_gallery t2 WHERE t1.ga_id = t2.ga_id AND t2.prod_id = ?1 AND t1.active_status = ?2 ", nativeQuery = true)
	public List<Gallery> findGalleryList(Integer productId, String status);
	
	@Query(value = "SELECT t1.* FROM tbl_Gallery t1 INNER JOIN tbl_product_gallery t2 WHERE t1.ga_id = t2.ga_id AND t2.prod_id = ?1 AND t1.active_status = ?2 ", 
			countQuery = "SELECT count(t1.ga_id) FROM tbl_Gallery t1 INNER JOIN tbl_product_gallery t2 WHERE t1.ga_id = t2.ga_id AND t2.prod_id = ?1 AND t1.active_status = ?2 ", 
			nativeQuery = true)
	public Page<Gallery> findGalleryList(Integer productId, String status, Pageable pageable);
	
	@Query(value = "SELECT t1.* FROM tbl_Gallery t1 INNER JOIN tbl_product_gallery t2 WHERE t1.ga_id = t2.ga_id AND t2.prod_id = ?1 AND t1.active_status = ?2 ORDER BY t1.ga_id ASC LIMIT 1 ", nativeQuery = true)
	public Optional<Gallery> findFistGallery(Integer productId, String status);
}
