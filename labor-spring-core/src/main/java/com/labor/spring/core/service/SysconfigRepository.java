package com.labor.spring.core.service;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import com.labor.spring.core.entity.Sysconfig;

public interface SysconfigRepository  extends JpaRepository<Sysconfig,Long> {

	public List<Sysconfig> findAll(Sort sort);
	
	public Sysconfig findByKeyAndStatusIgnoreCase(String key,String status);
	
	public List<Sysconfig> findByKeyStartingWith(String key);
	
}
