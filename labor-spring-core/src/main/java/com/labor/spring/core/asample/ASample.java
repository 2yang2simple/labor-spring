package com.labor.spring.core.asample;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;

import com.labor.spring.base.AbstractEntity;
@Entity
@Table(name = "asample_labor") 
public class ASample extends AbstractEntity implements Serializable {

	private static final long serialVersionUID = -4639951069990883242L;

	/****
	Constraint	详细信息
	@Null	被注释的元素必须为 null
	@NotNull	被注释的元素必须不为 null
	@AssertTrue	被注释的元素必须为 true
	@AssertFalse	被注释的元素必须为 false
	@Min(value)	被注释的元素必须是一个数字，其值必须大于等于指定的最小值
	@Max(value)	被注释的元素必须是一个数字，其值必须小于等于指定的最大值
	@DecimalMin(value)	被注释的元素必须是一个数字，其值必须大于等于指定的最小值
	@DecimalMax(value)	被注释的元素必须是一个数字，其值必须小于等于指定的最大值
	@Size(max, min)	被注释的元素的大小必须在指定的范围内
	@Digits (integer, fraction)	被注释的元素必须是一个数字，其值必须在可接受的范围内
	@Past	被注释的元素必须是一个过去的日期
	@Future	被注释的元素必须是一个将来的日期
	@Pattern(value)	被注释的元素必须符合指定的正则表达式
	Hibernate Validator 附加的 constraint

	Constraint	详细信息
	@Email	被注释的元素必须是电子邮箱地址
	@Length	被注释的字符串的大小必须在指定的范围内
	@NotEmpty	被注释的字符串的必须非空
	@NotBlank
	@Range	被注释的元素必须在合适的范围内
***/
	
	@Id
    @GeneratedValue 
    @Column(name="as_id")
    private Integer id;
	
	@NotBlank(message = "the name is empty.")
    @Column(name="as_name")
    private String name;

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
	
	
}
