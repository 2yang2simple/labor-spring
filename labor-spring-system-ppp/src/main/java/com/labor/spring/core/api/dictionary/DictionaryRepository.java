package com.labor.spring.core.api.dictionary;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;



public interface DictionaryRepository extends JpaRepository<Dictionary,Long> {

	@Query(value = "SELECT t1.* FROM tbl_core_dictionary t1 " + 
			"INNER JOIN tbl_core_dictionary t2 WHERE t1.parent_id = t2.dc_id " +
			"AND t2.dc_code = ?1 AND t1.active_status = ?2 ORDER BY dc_order ASC ", nativeQuery = true)
	public List<Dictionary> findByParentCodeAndStatus(String code, String status);
	
	public List<Dictionary> findByParentidIsNotNullOrderByParentidDesc();
	
	public List<Dictionary> findByParentidIsNullOrderByIdDesc();
	
	public Optional<Dictionary> findOneById(Integer id);
}
