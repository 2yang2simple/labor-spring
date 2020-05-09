package com.labor.spring.system.ppp.api.project;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.labor.spring.system.ppp.entity.project.ProjectGallery;

@Repository
public interface ProjectGalleryRepository extends JpaRepository<ProjectGallery,Long> {
	
	public Optional<ProjectGallery> findOneById(Integer id);

	public Optional<ProjectGallery> findFirstByProjectIdAndPgTypeOrderByIdDesc(Integer projectId, String pgType);
}
