package com.labor.spring.core.asample;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

public interface ASampleServiceIntf {
	
	public ASample create(ASample entity);
	public ASample update(ASample entity);
	public ASample update(Integer id, ASample entity, boolean ignoreNullProperties);
	public ASample save(ASample entity, boolean ignoreNullProperties);
	
	public Long countByExample(ASample entity);

	public Optional<ASample> findById(Integer id);
	public Optional<ASample> findByExample(ASample entity);
	public Optional<ASample> findByNameAndStatus(String name, String status);

	public List<ASample> findList(Sort sort);
	public List<ASample> findListByExample(ASample entity);
	

	public Page<ASample> findList(Pageable pageable);
	public Page<ASample> findListByExample(ASample entity, Pageable pageable);
	public Page<ASample> findListByNameStartingWith(String name,Pageable pageable);
	
}
