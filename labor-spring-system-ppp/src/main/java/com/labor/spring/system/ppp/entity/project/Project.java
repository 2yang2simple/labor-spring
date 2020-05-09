package com.labor.spring.system.ppp.entity.project;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.labor.spring.base.AbstractEntity;
@Entity
@Table(name = "tbl_project") 
public class Project extends AbstractEntity implements Serializable {

	private static final long serialVersionUID = -6936677768092060218L;

	@Id
    @GeneratedValue 
    @Column(name="proj_id")
    private Integer id;
    
    @Column(name="proj_code")
    private String code;
	
    @Column(name="proj_name")
    private String name;
    
    @Column(name="proj_namepinyin")
    private String namePinyin;
    
    @Column(name="proj_customer")
    private String customer;
    
    @Column(name="proj_customercode")
    private String customerCode;
    
    @Column(name="proj_supplier")
    private String supplier;
    
    @Column(name="proj_suppliercode")
    private String supplierCode;
    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone="GMT+8")
    @Column(name="proj_deliverydate")
    private Date deliveryDate;
    
    @Column(name="proj_amount")
    private Float amount;
    
    @Column(name="proj_currency")
    private String currency;
    
    @Column(name="proj_manager")
    private String manager;
    
    @Column(name="proj_manageruserid")
    private Integer managerUserid;
    
    @Column(name="proj_status")
    private String projectStatus;
    
    @Column(name="parent_id")
    private Integer parentId;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
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

	public String getCustomer() {
		return customer;
	}

	public void setCustomer(String customer) {
		this.customer = customer;
	}

	public String getCustomerCode() {
		return customerCode;
	}

	public void setCustomerCode(String customerCode) {
		this.customerCode = customerCode;
	}

	public String getSupplier() {
		return supplier;
	}

	public void setSupplier(String supplier) {
		this.supplier = supplier;
	}

	public String getSupplierCode() {
		return supplierCode;
	}

	public void setSupplierCode(String supplierCode) {
		this.supplierCode = supplierCode;
	}

	public Date getDeliveryDate() {
		return deliveryDate;
	}

	public void setDeliveryDate(Date deliveryDate) {
		this.deliveryDate = deliveryDate;
	}

	public Float getAmount() {
		return amount;
	}

	public void setAmount(Float amount) {
		this.amount = amount;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public String getManager() {
		return manager;
	}

	public void setManager(String manager) {
		this.manager = manager;
	}

	public Integer getManagerUserid() {
		return managerUserid;
	}

	public void setManagerUserid(Integer managerUserid) {
		this.managerUserid = managerUserid;
	}

	public String getProjectStatus() {
		return projectStatus;
	}

	public void setProjectStatus(String projectStatus) {
		this.projectStatus = projectStatus;
	}

	public Integer getParentId() {
		return parentId;
	}

	public void setParentId(Integer parentId) {
		this.parentId = parentId;
	}

    
}
