package com.labor.spring.system.ppp.api.oss;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.labor.spring.system.ppp.entity.oss.Attachment;
import com.labor.spring.system.ppp.entity.oss.ObjectHeader;


public interface ObjectHeaderRepository extends JpaRepository<ObjectHeader,Long> {
	
	public Optional<ObjectHeader> findOneByFileName(String fileName);
	


}
