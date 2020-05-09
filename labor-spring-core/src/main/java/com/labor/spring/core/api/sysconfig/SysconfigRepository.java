package com.labor.spring.core.api.sysconfig;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import com.labor.spring.core.entity.Sysconfig;

public interface SysconfigRepository  extends JpaRepository<Sysconfig,Long> {

	public Sysconfig findById(Integer id);
	
	public List<Sysconfig> findAll(Sort sort);
	
	public Sysconfig findByKeyAndStatusIgnoreCase(String key,String status);
	
	public List<Sysconfig> findByKeyStartingWith(String key);
	
}
