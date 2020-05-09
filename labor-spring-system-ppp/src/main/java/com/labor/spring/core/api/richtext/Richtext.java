package com.labor.spring.core.api.richtext;

import java.io.Serializable;
import java.sql.Blob;
import java.sql.Clob;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;

import com.labor.spring.base.AbstractEntity;


@Entity
@Table(name = "tbl_core_richtext") 
public class Richtext extends AbstractEntity implements Serializable {

	private static final long serialVersionUID = -867403120527788332L;
	
	@Id
    @GeneratedValue 
    @Column(name="rt_id")
    private Integer id;
	
	@NotBlank(message = "the name is empty.")
    @Column(name="rt_name")
    private String name;
    
    @Column(name="rt_url")
    private String url; 
    
    @Column(name="rt_text")
    private String text; 
    
    //use longtext
    @Column(name="rt_html")
    private String html; 

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

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getHtml() {
		return html;
	}

	public void setHtml(String html) {
		this.html = html;
	}
    
    
}
