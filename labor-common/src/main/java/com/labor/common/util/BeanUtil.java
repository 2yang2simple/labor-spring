package com.labor.common.util;

import java.lang.reflect.Field;
import java.util.HashMap;

import org.apache.logging.log4j.LogManager;

public class BeanUtil {


	/**
	 * get value by key
	 * @param obj
	 * @param key
	 * @return
	 */
	public static Object getValueByKey(Object obj, String key) {
		
		Class userCla = (Class) obj.getClass();
		
		if(("java.util.HashMap").equals(userCla.getName())){
			HashMap hm = (HashMap)obj;
			return hm.get(key);
		}	
		
		Field[] fs = userCla.getDeclaredFields();
		for (int i = 0; i < fs.length; i++) {
			Field f = fs[i];
			f.setAccessible(true);
			try {
				if (f.getName().endsWith(key)) {
					return f.get(obj);
				}
			} catch (IllegalArgumentException e) {
				LogManager.getLogger().error("",e);
			} catch (IllegalAccessException e) {
				LogManager.getLogger().error("",e);
			}
		}
		return "";
	}

}
