package com.labor.spring.system.oss.service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.labor.spring.system.oss.entity.ObjectBody;


public interface ObjectBodyRepository extends JpaRepository<ObjectBody,Long> {

	public Optional<ObjectBody> findOneByMd5(String md5);

}
