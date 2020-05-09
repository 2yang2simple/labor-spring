package com.labor.spring.system.ppp.entity.product;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import com.labor.spring.base.AbstractEntity;

@Entity
@Table(name = "tbl_product_doc") 
public class ProductDocument extends AbstractEntity implements Serializable {

	private static final long serialVersionUID = 2352905253412710670L;

	@Id
    @GeneratedValue 
    @Column(name="pd_id")
    private Integer id;
	
    @Column(name="prod_id")
    private Integer productId;
	
    @Column(name="doc_id")
    private Integer docId;
	
    @Column(name="pd_type")
    private String pdType;
    
    @Column(name="pd_status")
    private String pdStatus;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getProductId() {
		return productId;
	}

	public void setProductId(Integer productId) {
		this.productId = productId;
	}

	public Integer getDocId() {
		return docId;
	}

	public void setDocId(Integer docId) {
		this.docId = docId;
	}

	public String getPdType() {
		return pdType;
	}

	public void setPdType(String pdType) {
		this.pdType = pdType;
	}

	public String getPdStatus() {
		return pdStatus;
	}

	public void setPdStatus(String pdStatus) {
		this.pdStatus = pdStatus;
	}
    
    

}
