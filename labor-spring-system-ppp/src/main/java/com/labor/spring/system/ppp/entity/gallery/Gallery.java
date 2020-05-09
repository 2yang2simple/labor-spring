package com.labor.spring.system.ppp.entity.gallery;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import com.labor.spring.base.AbstractEntity;
@Entity
@Table(name = "tbl_gallery") 
public class Gallery extends AbstractEntity implements Serializable {

	private static final long serialVersionUID = 209330631929115246L;

	@Id
    @GeneratedValue 
    @Column(name="ga_id")
    private Integer id;

    @Column(name="ga_name")
    private String name;

    @Column(name="ga_namepinyin")
    private String namePinyin;

    @Column(name="ga_caption")
    private String caption;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getNamePinyin() {
		return namePinyin;
	}

	public void setNamePinyin(String namePinyin) {
		this.namePinyin = namePinyin;
	}

	public String getCaption() {
		return caption;
	}

	public void setCaption(String caption) {
		this.caption = caption;
	}
    
    
}
