package com.labor.spring.system.ppp.api.oss;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.labor.spring.system.ppp.entity.oss.Attachment;

public interface AttachmentRepository extends JpaRepository<Attachment,Long> {
	
	@Query(value = "SELECT t1.* FROM tbl_oss_attachment t1 WHERE t1.atta_filename = ?1 ", nativeQuery = true)
	public Optional<Attachment> findOneByFilename(String fileName);

}
