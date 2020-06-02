package com.labor.spring.util;

import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.web.bind.annotation.RestController;

import com.labor.common.util.ClassUtil;

public class RequiresPermissionsUtil {
	
	public static void main(String[] args) {
//		Set<String> ret = scanRequiresPermissionsValueFromRestController("com.labor.spring");
		
		Set<String> set1 = new HashSet<String>();
		Set<String> set2 = new HashSet<String>();
		Set<String> set3 = new HashSet<String>();
		set1.add("qqqq");
		set2.add("wwww");
		set2.add("qqqq");
		set3.addAll(set1);
		set3.addAll(set2);
		
		System.err.println(set3.size());
	}
	
	public static Set<String> scanRequiresPermissionsValueFromRestController(String packagename){
		Set<String> ret = new HashSet<String>();
		Set<Class<?>> set = ClassUtil.findClasses(packagename);
		for (Object obj : set) {
			if (obj instanceof Class<?>) {
				Class<?> clazz = (Class<?>) obj;
				RestController declaredAnnotation = clazz.getDeclaredAnnotation(RestController.class);
				//if class is the rest controller then 
				if (declaredAnnotation != null&&declaredAnnotation.value() != null) {
					Method[] declaredMethods = clazz.getDeclaredMethods();

					for (Method declaredMethod : declaredMethods) {
					
						RequiresPermissions fieldAnnotation = declaredMethod.getDeclaredAnnotation(RequiresPermissions.class);
						//get requires permission value of the method  
						if (fieldAnnotation != null&&fieldAnnotation.value()!=null) {

							int len = fieldAnnotation.value().length;
							for (int i = 0; i < len; i++) {
								LogManager.getLogger().debug(clazz.getName()+"|"
																+declaredMethod.getName()+"|"
																+"value"+i+"|"
																+fieldAnnotation.value()[i]);
								ret.add(fieldAnnotation.value()[i]);

							}
						}

					}

				}
				
			}
		}
		
		return ret;
		
	}
	

}
