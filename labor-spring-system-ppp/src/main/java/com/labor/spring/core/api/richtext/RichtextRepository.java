package com.labor.spring.core.api.richtext;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;



public interface RichtextRepository extends JpaRepository<Richtext,Long> {

//	public List<Richtext> findAll(Sort sort);
	public List<Richtext> findByNameStartingWith(String name);
	public Richtext findByUuid(String uuid);
	public Richtext findByNameAndStatus(String name,String Status);
	public Richtext findById(Integer id);
	
	@Query(value = "SELECT t1.* FROM tbl_core_richtext t1 WHERE t1.rt_name = ?1 AND active_status = '1'", nativeQuery = true)
	public Richtext findOneByName(String name);
}
