package com.labor.spring.system.ppp.api.product;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.labor.spring.system.ppp.entity.product.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product,Long> {
	
	
	public Optional<Product> findOneById(Integer id);
	public Optional<Product> findOneByUuid(String uuid);
	
	public Page<Product> findByNameContainingIgnoreCaseOrNamePinyinContainingIgnoreCase(
							String name, String namePinyin,	Pageable pageable);

	
	public List<Product> findByNameContainingIgnoreCaseOrNamePinyinContainingIgnoreCase(
							String name, String namePinyin);

}
