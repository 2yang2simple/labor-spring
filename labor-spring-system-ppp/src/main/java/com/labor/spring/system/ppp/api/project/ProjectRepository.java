package com.labor.spring.system.ppp.api.project;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.labor.spring.system.ppp.entity.project.Project;

@Repository
public interface ProjectRepository extends JpaRepository<Project,Long> {
	
	
	public Optional<Project> findOneById(Integer id);
	public Optional<Project> findOneByUuid(String uuid);
	
	public Page<Project> findByNameContainingIgnoreCaseOrNamePinyinContainingIgnoreCase(
							String name, String namePinyin,	Pageable pageable);

	public Page<Project> findByNameContainingIgnoreCaseAndProjectStatusOrNamePinyinContainingIgnoreCaseAndProjectStatus(
							String name, String status1, String namePinyin, String status2, Pageable pageable);
	
	public Page<Project> findByNameContainingIgnoreCaseAndProjectStatusNotOrNamePinyinContainingIgnoreCaseAndProjectStatusNot(
							String name, String status1, String namePinyin, String status2, Pageable pageable);
	
	public List<Project> findByNameContainingIgnoreCaseAndProjectStatusNotOrNamePinyinContainingIgnoreCaseAndProjectStatusNot(
							String name, String status1, String namePinyin, String status2);
}
