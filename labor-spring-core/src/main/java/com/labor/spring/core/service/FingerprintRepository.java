package com.labor.spring.core.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import com.labor.spring.core.entity.Fingerprint;

public interface FingerprintRepository  extends JpaRepository<Fingerprint,Long> {

	public Fingerprint findById(Integer id);
	
	public Fingerprint findByValueAndType(String value,String type);
	
	public List<Fingerprint> findByValueStartingWith(String value);
	
//	public List<Fingerprint> findAll(Sort sort);
    
//    public Page<Fingerprint> findAll(Pageable pageable);
	
	public Page<Fingerprint> findByValueStartingWith(String value,Pageable pageable);
	
}
