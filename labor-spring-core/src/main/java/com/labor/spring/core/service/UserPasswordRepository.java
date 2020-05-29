package com.labor.spring.core.service;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import com.labor.spring.core.entity.UserPassword;

public interface UserPasswordRepository  extends JpaRepository<UserPassword,Long> {

	public UserPassword findFirstByUseridOrderByIdDesc(Long userid);
}
