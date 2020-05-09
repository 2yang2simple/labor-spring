package com.labor.spring.system.ppp.entity.document;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import com.labor.spring.base.AbstractEntity;

@Entity
@Table(name = "tbl_doc_tag") 
public class DocumentTag extends AbstractEntity implements Serializable {

	private static final long serialVersionUID = 9030965715472092808L;
	
	@Id
    @GeneratedValue 
    @Column(name="dtag_id")
    private Integer id;
	
    @Column(name="doc_id")
    private Integer docId;
    
    @Column(name="tag_id")
    private Integer tagId;
    
    @Column(name="tag_name")
    private String tagName;
    
    @Column(name="tag_type")
    private String tagType;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getDocId() {
		return docId;
	}

	public void setDocId(Integer docId) {
		this.docId = docId;
	}

	public Integer getTagId() {
		return tagId;
	}

	public void setTagId(Integer tagId) {
		this.tagId = tagId;
	}

	public String getTagName() {
		return tagName;
	}

	public void setTagName(String tagName) {
		this.tagName = tagName;
	}

	public String getTagType() {
		return tagType;
	}

	public void setTagType(String tagType) {
		this.tagType = tagType;
	}

}
