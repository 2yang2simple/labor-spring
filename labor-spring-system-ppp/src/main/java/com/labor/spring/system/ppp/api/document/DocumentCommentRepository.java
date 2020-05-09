package com.labor.spring.system.ppp.api.document;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.labor.spring.system.ppp.entity.document.DocumentComment;

@Repository
public interface DocumentCommentRepository extends JpaRepository<DocumentComment,Long> {
	
	
	public List<DocumentComment> findByDocIdOrderByIdAsc(Integer docid);
}
