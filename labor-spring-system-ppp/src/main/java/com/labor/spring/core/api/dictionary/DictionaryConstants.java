package com.labor.spring.core.api.dictionary;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import org.apache.logging.log4j.LogManager;

import com.labor.common.util.StringUtil;
import com.labor.common.util.TokenUtil;

public class DictionaryConstants {

	/**
	 * the top code in tbl_core_dictionary which the parent id is null 
	 * findSubDictionarys() will return a group of dictionary with the parent id is the code id;
	 * findTopDictionarys() will return a group of dictionary with the parent id is null;
	 */
	public static String TOPCODE_DEMO1 = "DEMOCODE1";
	public static String TOPCODE_DEMO2 = "DEMOCODE2";
	
//	public static HashSet<String> KEYCODE_LIST = new HashSet<String>(); 

	public static HashMap<String,String> TOP_DICTIONARY = new HashMap<String,String>(); 
	
	static {
		TOP_DICTIONARY.put(TOPCODE_DEMO1,"DEMONAME1");
		TOP_DICTIONARY.put(TOPCODE_DEMO2,"DEMONAME2");
		
	}

	public static void main(String[] args) {
		
		String code = TOPCODE_DEMO1.substring(0,2)+TokenUtil.generateUNum();
		
		System.out.println(StringUtil.prefixZero("98",1));
	}
}
