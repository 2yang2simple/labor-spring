package com.labor.spring.system.ppp.api.gallery;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import com.labor.spring.system.ppp.entity.gallery.Gallery;
import com.labor.spring.system.ppp.entity.gallery.GalleryImage;

public interface GalleryServiceIntf {

	public Gallery create(Gallery entity);
//	public Gallery createImages(Integer galleryId, MultipartFile[] files, Integer startOrder);
	public GalleryImage createImage(Integer galleryId, MultipartFile file, Integer startOrder);

	public Gallery update(Integer id, Gallery entity, boolean ignoreNullProperties);
	public GalleryImage updateImage(Integer galleryId, Integer galleryImageId, GalleryImage entity,
			boolean ignoreNullProperties);

	public Optional<Gallery> findById(Integer id);
	public Optional<Gallery> findByUuid(String uuid);
//	public Optional<Gallery> findByName(String name);
	public Page<Gallery> findList(Pageable pageable);
	public Page<Gallery> findListByExample(Gallery entity, Pageable pageable);
	public Page<Gallery> findListByNameLike(String name, Pageable pageable);
	public Page<Gallery> findListByNameLikeAndStatus(String name, String status, Pageable pageable);

	public Optional<GalleryImage> findFistImageByUuid(String uuid);
	public Optional<GalleryImage> findImageByImageUuid(String uuid);

	public List<GalleryImage> findImageListByUuidAndStatusAndCaptionLike(String uuid, String status, String caption);
	public Page<GalleryImage> findImageListByUuidAndStatusAndCaptionLike(String uuid, String status, String caption,
			Pageable pageable);

	public void deleteImage(String uuid);	

}
