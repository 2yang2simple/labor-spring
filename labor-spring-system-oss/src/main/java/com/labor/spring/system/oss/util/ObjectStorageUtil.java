package com.labor.spring.system.oss.util;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.labor.common.util.StringUtil;
import com.labor.common.util.TokenUtil;
import com.labor.spring.system.oss.controller.vo.ObjectStorageType;
import com.labor.spring.system.oss.controller.vo.ObjectStorageVO;

import cn.hutool.core.util.RandomUtil;

public class ObjectStorageUtil {
	
	public static void main(String[] args) {
		String query = "73580-10-20200531-10eb5d715ff886873753e5605fc88ba7b7-jpg";
		
		String path = "20191211";
		String name = "95f444173ff249589a9051ef33e5c710";
		String type = "png";
		
		String rd = TokenUtil.generateUUID();
		
		String url = rd+"."+path+"."+name+"."+type;
		
		System.err.println(url);
		
		//^start $end
		String regex = "^[a-z0-9A-Z]+$";
		System.err.println(url.matches(regex));
		
        
		System.err.println(args2url(ObjectStorageType.NAS_FILE, path,name,type));
		ObjectStorageVO os = url2objectstorage(args2url(ObjectStorageType.NAS_FILE, path,name,type));
		
		System.err.println(os.getOsType().getType()+"|"+os.getPath()+"|"+os.getName()+"|"+os.getType());
		
		System.err.println(url2fullpath(query));

		
	}
	
	public static String args2url(ObjectStorageType ot, String path, String name, String type) {
//		System.err.println("pp="+(ObjectStorageType.NAS_IMAGE==ot));
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
		
		//add random to make unique
		ret = RandomUtil.randomNumbers(5)+"-"+ot.getType()+"-"+path+"-"+name+"-"+type;
		return ret;
		
	}
	
	public static ObjectStorageVO url2objectstorage(String url) {
		ObjectStorageVO ret = null;
		if (StringUtil.isEmpty(url)) {
			return ret;
		}
		String[] str = url.split("-");
		
		if (str==null||str.length!=5) {
			return ret;
		}
		String osType = str[1];
		String path = str[2];
		String name = str[3];
		String type = str[4];
		String regex = "^[a-z0-9A-Z]+$";
		if (!osType.matches(regex)
				||!path.matches(regex)
				||!name.matches(regex)
				||!type.matches(regex)) {
			System.err.println(osType+"|"+path+"|"+name+"|"+type+"|");
			return ret;
		}
		ret = new ObjectStorageVO();
		ret.setOsType(ObjectStorageType.create(osType));
		ret.setPath(path);
		ret.setName(name);
		ret.setType(type);
		return ret;
	}
		
	public static String url2fullpath(String url) {
		String ret = "";
		ObjectStorageVO os = url2objectstorage(url);
		if (os!=null) {
			ret = os.getOsType().getPath() + "/"
						+ os.getPath() + "/"
						+ os.getName() + "." + os.getType();				
		}
		return ret;
	}

}
