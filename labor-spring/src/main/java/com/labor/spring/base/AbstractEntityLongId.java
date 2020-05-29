package com.labor.spring.base;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

/***
 * 1.@MappedSuperclass注解只能标准在类上：@Target({java.lang.annotation.ElementType.TYPE})
 * 2.标注为@MappedSuperclass的类将不是一个完整的实体类，他将不会映射到数据库表，但是他的属性都将映射到其子类的数据库字段中。
 * 3.标注为@MappedSuperclass的类不能再标注@Entity或@Table注解，也无需实现序列化接口。
 * @author Administrator
 *
 */
@MappedSuperclass 
public class AbstractEntityLongId extends AbstractEntity {
	
    
	@Column(name="data_longid",updatable = false)
    private Long longid;

	public Long getLongid() {
		return longid;
	}

	public void setLongid(Long longid) {
		this.longid = longid;
	} 
    
    
}
