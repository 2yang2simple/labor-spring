package com.labor.common.util;

import cn.hutool.core.lang.Validator;

public class HuToolUtil {

	
	public static void main(String[] args) {
		
		System.err.println(Validator.isMobile("129888823"));
		System.err.println(Validator.isMobile("17000000000"));
		System.err.println(Validator.isMobile("15666664544"));
		System.err.println(Validator.isMobile("8617000000000"));
	}
}
