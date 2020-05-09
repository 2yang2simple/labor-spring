package com.labor.spring.system.ppp.api.gallery;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.labor.spring.system.ppp.entity.gallery.GalleryImage;

public interface GalleryImageRepository extends JpaRepository<GalleryImage, Long> {

	public List<GalleryImage> findByGaId(Integer gaId);

	public List<GalleryImage> findByGaIdAndStatus(Integer gaId, String status);

	public List<GalleryImage> findByGaIdAndCaptionContainingIgnoreCase(Integer gaId, String caption);

	public List<GalleryImage> findByGaIdAndStatusAndCaptionContainingIgnoreCase(Integer gaId, String status,
			String caption);

	public Page<GalleryImage> findByGaIdAndStatus(Integer gaId, String status, Pageable pageable);

	public Page<GalleryImage> findByGaIdAndStatusAndCaptionContainingIgnoreCase(Integer gaId, String status,
			String caption, Pageable pageable);

	public Optional<GalleryImage> findOneByUuid(String uuid);

	public Optional<GalleryImage> findOneById(Integer id);

	public Optional<GalleryImage> findFirstByGaIdAndStatusOrderByOrderAsc(Integer gaid, String status);

	@Modifying
	@Query(value = "DELETE FROM tbl_gallery_image WHERE data_uuid = ?1", nativeQuery = true)
	public int deleteByUuid(String uuid);

}
