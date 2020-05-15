package com.labor.spring.core.api.permission;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import com.labor.spring.core.entity.Permission;

public interface PermissionRepository extends JpaRepository<Permission,Long> {
	
	@Query(value = "SELECT t1.* FROM\r\n" + 
			"tbl_core_permission t1\r\n" + 
			"INNER JOIN\r\n" + 
			"tbl_core_rolepermission t2\r\n" + 
			"INNER JOIN \r\n" + 
			"tbl_core_userrole t3\r\n" + 
			"WHERE t1.per_id = t2.per_id\r\n" + 
			"AND t2.role_id = t3.role_id\r\n" + 
			"AND t3.user_id = ?1", nativeQuery = true)
	public List<Permission> findByUserid(Integer userid);
	
	@Query(value = "SELECT t1.* FROM\r\n" + 
			"tbl_core_permission t1\r\n" + 
			"INNER JOIN\r\n" + 
			"tbl_core_rolepermission t2\r\n" + 
			"WHERE t1.per_id = t2.per_id\r\n" + 
			"AND t2.role_id = ?1", nativeQuery = true)
	public List<Permission> findByRoleid(Integer roleid);
	
//    public List<Permission> findAll(Sort sort);
    
    public List<Permission> findByStatusOrderByCodeDesc(String status);
    
    public Permission findById(Integer id);

    public Permission findByCode(String code);
    
    @Transactional
    @Modifying
	@Query(value = "UPDATE tbl_core_permission SET active_status = 0", nativeQuery = true)
	public void inactivePermissions();
}
