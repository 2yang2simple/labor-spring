package com.labor.spring.system.oss.service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.labor.spring.system.oss.entity.ObjectHeader;


public interface ObjectHeaderRepository extends JpaRepository<ObjectHeader,Long> {
	
	public Optional<ObjectHeader> findOneByFileName(String fileName);
	public Optional<ObjectHeader> findOneByUrl(String url);


}
