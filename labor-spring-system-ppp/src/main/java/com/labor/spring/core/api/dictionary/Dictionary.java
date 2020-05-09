package com.labor.spring.core.api.dictionary;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;

import com.labor.spring.base.AbstractEntity;


@Entity
@Table(name = "tbl_core_dictionary") 
public class Dictionary extends AbstractEntity implements Serializable {

	private static final long serialVersionUID = 262727203485477430L;

	@Id
    @GeneratedValue 
    @Column(name="dc_id")
    private Integer id;
	
    @NotBlank(message = "the name is empty.")
    @Column(name="dc_name")
    private String name;

    @Column(name="dc_code")
    private String code;
	
    @Column(name="dc_value1")
    private String value1;
	
    @Column(name="dc_value2")
    private String value2;
	
    @Column(name="dc_value3")
    private String value3;

    @Column(name="dc_value4")
    private String value4;
	
    @Column(name="dc_value5")
    private String value5;
	
    @Column(name="dc_order")
    private Integer order;
    
	@Column(name="parent_id")
    private Integer parentid;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	
    public Integer getOrder() {
		return order;
	}

	public void setOrder(Integer order) {
		this.order = order;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getValue1() {
		return value1;
	}

	public void setValue1(String value1) {
		this.value1 = value1;
	}

	public String getValue2() {
		return value2;
	}

	public void setValue2(String value2) {
		this.value2 = value2;
	}

	public String getValue3() {
		return value3;
	}

	public void setValue3(String value3) {
		this.value3 = value3;
	}

	public String getValue4() {
		return value4;
	}

	public void setValue4(String value4) {
		this.value4 = value4;
	}

	public String getValue5() {
		return value5;
	}

	public void setValue5(String value5) {
		this.value5 = value5;
	}

	public Integer getParentid() {
		return parentid;
	}

	public void setParentid(Integer parentid) {
		this.parentid = parentid;
	}
	

}
