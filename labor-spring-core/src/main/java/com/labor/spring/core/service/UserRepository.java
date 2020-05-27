package com.labor.spring.core.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;

import com.labor.spring.core.entity.User;

//JpaRepository<Entity,Primary Key Type>
public interface UserRepository extends JpaRepository<User,Long> {
	
	//	@Query(value = "select * from @EntityTable t where t.user_cfmd5=?1", nativeQuery = true)
	
	/***
	 * finByXxxBbb  xxxBbb is the attribute of the entity 
	 * @param cfmd5
	 * @return
	 */
    
    public List<User> findAll(Sort sort);
    
    public Page<User> findAll(Pageable pageable);

    public User findById(Integer id);
    
    public User findByUuidIgnoreCase(String uuid);
    
    public User findByNameIgnoreCase(String name);
    
    public User findBySnoIgnoreCase(String sno);
    
    public User findByWeixinIgnoreCase(String weixin);
    
    public User findByEmailIgnoreCase(String email);
    
    public User findByCellPhoneIgnoreCase(String cellPhone);
    
    public User findByPwdmodifyIgnoreCase(String pwdmodify);

    public User findByGoogleSecretKeyIgnoreCase(String googleSecretKey);
    
	public List<User> findByNameStartingWith(String name);
	
	public Page<User> findByNameContainingIgnoreCaseOrCellPhoneContainingIgnoreCase(String name,String cellPhone,Pageable pageable);
	
	public Page<User> findByNameStartingWith(String name,Pageable pageable);
	
	@Query(value = "SELECT user_googlesecretkey FROM tbl_core_user WHERE data_uuid = ?1 ", nativeQuery = true)
	public String findGoogleSecretKeyByUuid(String uuid);
	
	@Query(value = "SELECT MAX(user_sno) FROM tbl_core_user ", nativeQuery = true)
	public String findMaxSno();

}
