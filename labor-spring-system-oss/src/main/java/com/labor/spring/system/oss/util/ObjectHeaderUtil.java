package com.labor.spring.system.oss.util;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.labor.common.util.StringUtil;
import com.labor.common.util.TokenUtil;
import com.labor.spring.system.oss.controller.vo.ObjectStorageVO;

import cn.hutool.core.util.RandomUtil;

public class ObjectHeaderUtil {
	
	public static String generateUrl() {
		String ret = "";
		String filePath = new SimpleDateFormat("yyyyMMdd").format(new Date());
		String md5 = TokenUtil.generateUUID();
		return ret;
	}
	
	public static void main(String[] args) {
		String path = "20191211";
		String name = "95f444173ff249589a9051ef33e5c710";
		String type = "png";
		
		String rd = TokenUtil.generateUUID();
		
		String url = rd+"."+path+"."+name+"."+type;
		
		System.err.println(url);
		
		//^start $end
		String regex = "^[a-z0-9A-Z]+$";
		System.err.println(url.matches(regex));
		
        
		System.err.println(args2url(path,name,type));
		
		System.err.println(url2fullpath(args2url(path,name,type)));
	}

	public static String args2url(String path, String name, String type) {
		String ret = "";
		if (StringUtil.isEmpty(path)
				||StringUtil.isEmpty(name)
				||StringUtil.isEmpty(type)) {
			return ret;
		}
		String regex = "^[a-z0-9A-Z]+$";
		if (!path.matches(regex)
				||!name.matches(regex)
				||!type.matches(regex)) {
			return ret;
		}
		
//		ret = TokenUtil.generateUUID()+"-"+path+"-"+name+"-"+type;
		ret = RandomUtil.randomInt(10)+"-"+path+"-"+name+"-"+type;
		return ret;
		
	}
	public static ObjectStorageVO url2objectstorage(String url) {
		ObjectStorageVO ret = null;
		if (StringUtil.isEmpty(url)) {
			return ret;
		}
		String[] str = url.split("-");
		if (str==null||str.length!=4) {
			return ret;
		}
		String path = str[1];
		String name = str[2];
		String type = str[3];
		String regex = "^[a-z0-9A-Z]+$";
		if (!path.matches(regex)
					||!name.matches(regex)
					||!type.matches(regex)) {
			return ret;
		}
		ret = new ObjectStorageVO();
		ret.setPath(path);
		ret.setName(name);
		ret.setType(type);
		return ret;
	}
	
	public static String url2fullpath(String url) {
		String ret = "";
		if (StringUtil.isEmpty(url)) {
			return ret;
		}
		String[] str = url.split("-");
		if (str==null||str.length!=4) {
			return ret;
		}
		String path = str[1];
		String name = str[2];
		String type = str[3];
		String regex = "^[a-z0-9A-Z]+$";
		if (!path.matches(regex)
					||!name.matches(regex)
					||!type.matches(regex)) {
			return ret;
		}
		
		return File.separator+path+File.separator+name+"."+type;
	}

}
