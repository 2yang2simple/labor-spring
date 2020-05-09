package com.labor.spring.core.api.richtext;

import java.util.List;
import org.springframework.data.domain.Sort;

public interface RichtextServiceIntf {

	public Richtext save(Richtext richtext);
	public Richtext create(Richtext richtext);
	public Richtext update(Richtext richtext);
	
	public List<Richtext> findList(Sort sort);
	public List<Richtext> findListByNameStartingWith(String name);
	
	public Richtext findByUuid(String uuid);
	public Richtext findByName(String name);
	public Richtext findById(Integer id);
	
	
}
