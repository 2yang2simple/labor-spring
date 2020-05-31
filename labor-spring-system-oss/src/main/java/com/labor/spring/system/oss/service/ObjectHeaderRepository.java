package com.labor.spring.system.oss.service;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import com.labor.spring.system.oss.entity.ObjectHeader;


public interface ObjectHeaderRepository extends JpaRepository<ObjectHeader,Long> {
	
	public Optional<ObjectHeader> findOneByFileName(String fileName);
	public Optional<ObjectHeader> findOneByUrl(String url);


}
