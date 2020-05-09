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
@Table(name = "tbl_doc_content") 
public class DocumentContent extends AbstractEntity implements Serializable {

	private static final long serialVersionUID = 161375502000876129L;

	@Id
    @GeneratedValue 
    @Column(name="dcon_id")
    private Integer id;
	
    @Column(name="doc_id")
    private Integer docId;
	
	@NotBlank(message = "the content is empty.")
    @Column(name="dcon_html")
    private String html;
	
    @Column(name="dcon_text")
    private String text;
	
    @Column(name="dcon_md5")
    private String md5;

	public String getMd5() {
		return md5;
	}

	public void setMd5(String md5) {
		this.md5 = md5;
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
