package com.labor.spring.system.ppp.api.gallery;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.labor.spring.system.ppp.entity.document.Document;
import com.labor.spring.system.ppp.entity.gallery.Gallery;

public interface GalleryRepository extends JpaRepository<Gallery, Long> {

	public Optional<Gallery> findFirstByNameOrderByIdDesc(String name);

	public Optional<Gallery> findOneByUuid(String uuid);

	public Optional<Gallery> findOneById(Integer id);

	public Page<Gallery> findByStatus(String status, Pageable pageable);

	public Page<Gallery> findByNameContainingIgnoreCaseOrNamePinyinContainingIgnoreCase(String name, String namePinyin,
			Pageable pageable);

	@Query(value = "SELECT t1.* FROM tbl_gallery t1 WHERE (t1.ga_name LIKE CONCAT('%',?1,'%') OR t1.ga_namepinyin LIKE CONCAT('%',?2,'%')) AND t1.active_status = ?3 ", countQuery = "SELECT count(t1.ga_id) FROM tbl_gallery t1 WHERE (t1.ga_name LIKE CONCAT('%',?1,'%') OR t1.ga_namepinyin LIKE CONCAT('%',?2,'%')) AND t1.active_status = ?3 ", nativeQuery = true)
	public Page<Gallery> findByNameLikeAndStatus(String name, String namepinyin, String status, Pageable pageable);
}
