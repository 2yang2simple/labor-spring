package com.labor.spring.util;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.lang.annotation.ElementType;
import java.lang.annotation.RetentionPolicy;

@Target({ElementType.FIELD,ElementType.TYPE}) //Target 注解的使用域，FIELD表示使用在属性上面，TYPE表示使用在类上面
@Retention(RetentionPolicy.RUNTIME) //Retention 设置注解的生命周期 ，这里定义为RetentionPolicy.RUNTIME 非常关键
@Documented
public @interface PropertyMapper {     

     String value() default ""; 
     String type() default "";  // value : status(标记属性值为Y/N的属性) / date(标记属性类型为时间) 
     
     
}