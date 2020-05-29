package com.labor.spring.core.service;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.labor.spring.core.entity.Permission;
import com.labor.spring.core.entity.Role;


public interface RoleRepository extends JpaRepository<Role,Long> {
	
	public List<Role> findByStatus(String status);
	
	public Role findByNameAndStatusIgnoreCase(String name,String status);
	
	public List<Role> findByNameStartingWith(String name);
	
	@Query(value = "SELECT t1.* FROM\r\n" + 
			"tbl_core_role t1\r\n" + 
			"INNER JOIN\r\n" + 
			"tbl_core_userrole t2\r\n" + 
			"WHERE t1.role_id = t2.role_id\r\n" + 
			"AND t2.user_id = ?1", nativeQuery = true)
	public List<Role> findByUserid(Long userid);
}
