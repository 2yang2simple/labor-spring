package com.labor.spring.core.service;

import java.util.List;

import org.springframework.data.domain.Sort;

import com.labor.spring.core.entity.Sysconfig;

public interface SysconfigServiceIntf {

	public Sysconfig save(Sysconfig sysconfig);
	public String createDefaultSysconfig();
	public String initialization();
	
	public Sysconfig findById(Long id);

	public List<Sysconfig> findList(Sort sort);
	public List<Sysconfig> findListByKeyStartingWith(String key);
	
	public String findValueByKey(String key);
}
