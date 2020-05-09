package com.labor.spring.system.ppp.entity.product;

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
@Table(name = "tbl_product") 
public class Product extends AbstractEntity implements Serializable {

	private static final long serialVersionUID = 752246774014475748L;

	@Id
    @GeneratedValue 
    @Column(name="prod_id")
    private Integer id;
    
    @Column(name="prod_code")
    private String code;
    
    @Column(name="prod_itemno")
    private String itemNo;
    
    @Column(name="prod_name")
    private String name;
    
    @Column(name="prod_namepinyin")
    private String namePinyin;
    
    @Column(name="prod_price")
    private Float price;
    
    @Column(name="prod_costprice")
    private Float costPrice;
    
    @Column(name="prod_marketprice")
    private Float marketPrice;
    
    @Column(name="prod_currency")
    private String currency;
    
    @Column(name="prod_size")
    private String size;
    
    @Column(name="prod_color")
    private String color;
    
    @Column(name="prod_status")
    private String productStatus;
    
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

	public String getItemNo() {
		return itemNo;
	}

	public void setItemNo(String itemNo) {
		this.itemNo = itemNo;
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

	public Float getPrice() {
		return price;
	}

	public void setPrice(Float price) {
		this.price = price;
	}

	public Float getCostPrice() {
		return costPrice;
	}

	public void setCostPrice(Float costPrice) {
		this.costPrice = costPrice;
	}

	public Float getMarketPrice() {
		return marketPrice;
	}

	public void setMarketPrice(Float marketPrice) {
		this.marketPrice = marketPrice;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public String getSize() {
		return size;
	}

	public void setSize(String size) {
		this.size = size;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public String getProductStatus() {
		return productStatus;
	}

	public void setProductStatus(String productStatus) {
		this.productStatus = productStatus;
	}

	public Integer getParentId() {
		return parentId;
	}

	public void setParentId(Integer parentId) {
		this.parentId = parentId;
	}
    
    
}
