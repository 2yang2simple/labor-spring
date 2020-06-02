package com.labor.spring.system.ppp.api.document;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.labor.spring.system.ppp.entity.document.DocumentUser;

@Repository
public interface DocumentUserRepository extends JpaRepository<DocumentUser,Long> {

	
	@Modifying
	@Query(value = "DELETE FROM tbl_doc_user WHERE doc_id = ?1", nativeQuery = true)
	public int deleteByDocId(Integer docId);
	
	
	public List<DocumentUser> findByDocId(Integer docId);
}
