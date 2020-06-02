//package com.labor.spring.system.ppp.api.document;
//
//import java.util.List;
//import java.util.Optional;
//
//import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.data.jpa.repository.Query;
//import org.springframework.stereotype.Repository;
//
//import com.labor.spring.core.entity.User;
//
//@Repository
//public interface DocumentUserFinderRepository extends JpaRepository<User,Long> {
//	
//	@Query(value = "SELECT t1.* FROM tbl_core_user t1 INNER JOIN tbl_doc_user t2 WHERE t1.user_id = t2.user_id AND t2.doc_id =?1", nativeQuery = true)
//	public List<User> findUserListByDocId(Integer docId);
//	
//	@Query(value = "SELECT t1.* FROM tbl_core_user t1 INNER JOIN tbl_doc t2 WHERE t1.user_id = t2.created_by AND t2.doc_id =?1", nativeQuery = true)
//	public Optional<User> findCreatorByDocId(Integer docId);
//}
