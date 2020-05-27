package com.labor.spring.core.service;

import org.springframework.data.jpa.repository.JpaRepository;

import com.labor.spring.core.entity.UserHistory;


//JpaRepository<Entity,Primary Key Type>
public interface UserHistoryRepository extends JpaRepository<UserHistory,Long> {
	
	
}
