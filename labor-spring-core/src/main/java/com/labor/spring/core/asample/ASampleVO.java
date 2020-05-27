package com.labor.spring.core.asample;

import javax.validation.constraints.NotBlank;

import com.labor.spring.base.AbstractVO;
import com.labor.spring.util.PropertyMapper;

@PropertyMapper
public class ASampleVO extends AbstractVO{
	
	
	@NotBlank(message = "the id is empty.")
	private Integer id;
	
	@NotBlank(message = "the name is empty.")
	@PropertyMapper(value="name")
    private String displayName;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}



}
