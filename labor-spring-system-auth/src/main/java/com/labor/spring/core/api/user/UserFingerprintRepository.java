package com.labor.spring.core.api.user;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.labor.spring.core.entity.UserFingerprint;

public interface UserFingerprintRepository extends JpaRepository<UserFingerprint,Long> {

	public UserFingerprint findFirstByValueAndTypeOrderByLastUpdateDateDesc(String value, String type);

	public UserFingerprint findFirstByFpidAndUseridOrderByIdDesc(Integer fpid,Integer userid);
	
	@Modifying
	@Query(value = "UPDATE tbl_core_userfingerprint t1 SET uf_rememberme=?1 WHERE t1.fp_value=?2 and t1.fp_type=?3 ", nativeQuery = true)
	public int updateRemembermeByValueAndType(String rememberme, String value, String type);
	

	@Modifying
	@Query(value = "UPDATE tbl_core_userfingerprint t1 SET uf_rememberme=?1 WHERE t1.user_id=?2 ", nativeQuery = true)
	public int updateRemembermeByUserid(String rememberme, Integer userid);
}
