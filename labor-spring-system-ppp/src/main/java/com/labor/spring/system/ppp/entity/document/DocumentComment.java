package com.labor.spring.system.ppp.entity.document;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;

import com.labor.spring.base.AbstractEntity;
@Entity
@Table(name = "tbl_doc_comment") 
public class DocumentComment extends AbstractEntity implements Serializable {

	private static final long serialVersionUID = -2331952364683375056L;
	
	@Id
    @GeneratedValue 
    @Column(name="dcmt_id")
    private Integer id;
	
    @Column(name="doc_id")
    private Integer docId;
    
	@NotBlank(message = "the comment is empty.")
    @Column(name="dcmt_html")
    private String html;
	
    @Column(name="dcmt_text")
    private String text;
    
    @Column(name="dcmt_creator")
    private String creator;
    
	public String getCreator() {
		return creator;
	}

	public void setCreator(String creator) {
		this.creator = creator;
	}

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

	public String getHtml() {
		return html;
	}

	public void setHtml(String html) {
		this.html = html;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}
    
}
