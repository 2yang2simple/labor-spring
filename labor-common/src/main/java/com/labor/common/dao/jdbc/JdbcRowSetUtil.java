package com.labor.common.dao.jdbc;

import java.lang.reflect.Method;
import java.util.Date;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.sql.RowSet;



public class JdbcRowSetUtil {

	/***
	 * transfer the rowset to a vo or hashmap list;
	 * @param rs		rowset
	 * @param clazz   	the vo class, default is hashmap
	 * @return
	 * @throws SQLException
	 */
	public static List toResultList(RowSet rs,Class clazz) throws Exception{
		List ret = new ArrayList();
		ResultSetMetaData rmd = rs.getMetaData();
		int columncount = rmd.getColumnCount();
		while (rs.next()) {
			Object obj;
			//default is hashMap
			if (clazz==null||HashMap.class.equals(clazz)){
				obj = new HashMap(); 
			} else {
				obj = clazz.newInstance();
			}
			for (int i=1;i<=columncount;i++){
				String columnlabel = rmd.getColumnLabel(i);
				String columnvalue = rs.getString(i);
				//call the set method of the vo
				if (clazz==null||HashMap.class.equals(clazz)){
					((HashMap)obj).put(columnlabel, columnvalue);
				}else{
					Method getMethod = clazz.getMethod("get"+upperFirstLetter(columnlabel),
												new Class[]{});
					Method setMethod = clazz.getMethod("set"+upperFirstLetter(columnlabel),
												new Class[]{getMethod.getReturnType()});

					Class type = getMethod.getReturnType();
					Object[] paras = null;
					if (String.class.equals(type)){
						paras = new Object[]{columnvalue};
					} else if (Integer.class.equals(type)){
						paras = new Object[]{Integer.valueOf(columnvalue)};
					} else if (int.class.equals(type)){
						paras = new Object[]{Integer.valueOf(columnvalue)};
					} else if (long.class.equals(type)){
						paras = new Object[]{Long.valueOf(columnvalue)};
					} else if (float.class.equals(type)){
						paras = new Object[]{new Float(columnvalue)};
					} else if (Date.class.equals(type)){
						paras = new Object[]{rs.getDate(i)};
					}
					setMethod.invoke(obj,paras);
				}
			}
			ret.add(obj);
		}
		return ret;
	}
	
	//put the first letter to the upper case
	public static String upperFirstLetter(String s){
		return String.valueOf(s.charAt(0)).toUpperCase()
					+ s.substring(1);
	}
	
	public static void main(String arg[]){
		System.out.println(upperFirstLetter("xxxxx"));
		System.out.println(upperFirstLetter("x"));
	}
	
}
