package com.labor.spring.system.ppp.api.tag;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import com.labor.spring.system.ppp.entity.tag.Tag;

public interface TagRepository extends JpaRepository<Tag,Long> {
	
	public Optional<Tag> findOneById(Integer id);
	public Optional<Tag> findOneByNameIgnoreCase(String name);

	public Page<Tag> findByTypeIn(Collection<String> ptypes, Pageable pageable);
	public Page<Tag> findByNameContainingIgnoreCase(String name, Pageable pageable);
	public Page<Tag> findByNameContainingIgnoreCaseOrNamePinyinContainingIgnoreCase(String name, String namePinyin, Pageable pageable);
	public Page<Tag> findByNameContainingIgnoreCaseAndTypeInOrNamePinyinContainingIgnoreCaseAndTypeIn(
			String name, Collection<String> ntypes, String namePinyin, Collection<String> ptypes, Pageable pageable);
	
	public List<Tag> findFirst10ByNameContainingIgnoreCaseAndTypeInOrNamePinyinContainingIgnoreCaseAndTypeIn(
			String name, Collection<String> ntypes, String namePinyin, Collection<String> ptypes, Sort sort);

}
