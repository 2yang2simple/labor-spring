package com.labor.common.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

import org.apache.logging.log4j.LogManager;

import com.labor.common.constants.CommonConstants;

import cn.hutool.core.lang.Snowflake;
import cn.hutool.core.net.NetUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;

public class TokenUtil {

    public static String md5(String text) {
    	String ret = "";
        byte[] bytes = null;
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(text.getBytes("utf-8"));
            bytes = md.digest();
            //to hex
            String md5 = new BigInteger(1, bytes).toString(16);//
            //BigInteger will cut the 0 in the front ,so fill 0 to be 32bit
            int len = md5.length();
            for (int i = 0; i < 32 - len; i++) {
            	md5 = "0" + md5;
            }
            ret = md5;
            
        } catch (NoSuchAlgorithmException nae) {
        	LogManager.getLogger().error("",nae);
        } catch (UnsupportedEncodingException uee) {
        	LogManager.getLogger().error("",uee);
        }

        return ret;
    }
    public static String md5(byte[] bufferBytes) {
    	String ret = "";
        byte[] bytes = null;
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(bufferBytes);
            bytes = md.digest();
            //to hex
            String md5 = new BigInteger(1, bytes).toString(16);//
            //BigInteger will cut the 0 in the front ,so fill 0 to be 32bit
            int len = md5.length();
            for (int i = 0; i < 32 - len; i++) {
            	md5 = "0" + md5;
            }
            ret = md5;
            
        } catch (NoSuchAlgorithmException nae) {
        	LogManager.getLogger().error("",nae);
        }

        return ret;
    }
    public static String md5(File file) {
    	String ret = "";
    	byte[] bytes = null;
    	FileInputStream fis = null;
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			fis = new FileInputStream(file);
			byte[] buffer = new byte[8192];
			int length;
			while ((length = fis.read(buffer)) != -1) {
				md.update(buffer, 0, length);
			}
			bytes = md.digest();
            //to hex
            String md5 = new BigInteger(1, bytes).toString(16);//
            //BigInteger will cut the 0 in the front ,so fill 0 to be 32bit
            int len = md5.length();
            for (int i = 0; i < 32 - len; i++) {
            	md5 = "0" + md5;
            }
            ret = md5;

		} catch (NoSuchAlgorithmException e) {
			LogManager.getLogger().error("",e);
		} catch (UnsupportedEncodingException e) {
			LogManager.getLogger().error("",e);
		} catch (FileNotFoundException e) {
			LogManager.getLogger().error("",e);
		} catch (IOException e) {
			LogManager.getLogger().error("",e);
		} finally {
			try {
				if (fis != null){
					fis.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return ret;
	}
    
    
    public static String md5(InputStream is) {
    	String ret = "";
    	byte[] bytes = null;
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			byte[] buffer = new byte[8192];
			int length;
			while ((length = is.read(buffer)) != -1) {
				md.update(buffer, 0, length);
			}
			bytes = md.digest();
            //to hex
            String md5 = new BigInteger(1, bytes).toString(16);//
            //BigInteger will cut the 0 in the front ,so fill 0 to be 32bit
            int len = md5.length();
            for (int i = 0; i < 32 - len; i++) {
            	md5 = "0" + md5;
            }
            ret = md5;

		} catch (NoSuchAlgorithmException e) {
			LogManager.getLogger().error("",e);
		} catch (UnsupportedEncodingException e) {
			LogManager.getLogger().error("",e);
		} catch (FileNotFoundException e) {
			LogManager.getLogger().error("",e);
		} catch (IOException e) {
			LogManager.getLogger().error("",e);
		} finally {
			try {
				if (is != null){
					is.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return ret;
	}

    public static String generateRandomCode() {
    	return md5(new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()));
    }
    
    public static String generateUUID(String puuid) {
    	if(!StringUtil.isEmpty(puuid)
    			&&puuid.length()>=32) {
    		return puuid;
    	}
    	return generateUUID();
    }
    
    public static String generateUUID() {
    	UUID uuid = UUID.randomUUID();
        return uuid.toString().replace("-","");
    }
    
    public static long generateLongID(long longid) {
    	long ret = generateLongID();
    	if (Long.toString(longid).length()
    			==Long.toString(ret).length()) {
    		ret = longid;
    	}
    	return ret;
    }
    
    public static long generateLongID() {
		long ret = 0;
		long workid = 0;
		long datacenterid = 0;
		String hostname = NetUtil.getLocalhost().getHostName();
		String hostaddress = NetUtil.getLocalhost().getHostAddress();
		byte[] nbs = null;
		byte[] abs = null;
		int hostnamenum = 0;
		int hostaddressnum = 0;
		if (!StrUtil.isEmpty(hostname)) {
			nbs = hostname.getBytes();
		}
		if (!StrUtil.isEmpty(hostaddress)) {
			abs = hostaddress.getBytes();
		}

		if (nbs!=null&&nbs.length>0) {
			for (byte b : nbs) {
				hostnamenum = hostnamenum + b;
			}
		}
		if (abs!=null&&nbs.length>0) {
			for (byte b : abs) {
				hostaddressnum = hostaddressnum + b;
			}
		}
		if (hostnamenum==0) {
			workid = RandomUtil.randomInt(31);
		} else {
			workid = hostnamenum%31;
		}

		if (hostaddressnum==0) {
			datacenterid = RandomUtil.randomInt(31);
		} else {
			datacenterid = hostaddressnum%31;
		}
		/***************************
		 * use HUTOOL snowflake
		 */
		Snowflake sf = new Snowflake(workid,datacenterid);
		ret = sf.nextId();
//		System.err.println(ret+"-"+workid+"-"+datacenterid);
		return ret;
	}
    
    
    public static volatile int Guid=100; 
    public static String generateUNum() {
		
    	TokenUtil.Guid+=1;
		long now = System.currentTimeMillis();  
		SimpleDateFormat dateFormat=new SimpleDateFormat("yyyy");  
		String time=dateFormat.format(now);
		String info=now+"";

		int ran=0;
		if(TokenUtil.Guid>999){
			TokenUtil.Guid=100;    	
		}
		ran=TokenUtil.Guid;
				
		return time+info.substring(2, info.length())+ran;  
	}    
    public static volatile int dateNumCount=100; 
    public static String generateDateNum() {
		
    	TokenUtil.dateNumCount+=1;
		long now = System.currentTimeMillis();  
		SimpleDateFormat dateFormat=new SimpleDateFormat("yyMMddss");  
		String time=dateFormat.format(now);
		String info=now+"";

		int ran=0;
		if(TokenUtil.dateNumCount>999){
			TokenUtil.dateNumCount=100;    	
		}
		ran=TokenUtil.dateNumCount;
				
		return time+ran;  
	}
    
    /**
     * validate token salted with date。
     * @param time.token(time+token)
     * 	8888.2323243434343
     * @return
     */
    public static boolean isValidatedDateSaltingToken(String timetoken, String token) {
    	if (StringUtil.isEmpty(timetoken)) {
			return false;
		}
    	
		String[] str = timetoken.split("\\.");
		
		if (str==null||str.length!=2) {
			return false;
		}
		
    	return  isValidatedDateSaltingToken(str[0],str[1],token);
    			
    }

    /**
     * validate token salted with date。
     * @param g
     * @param t
     * @return
     */
    public static boolean isValidatedDateSaltingToken(String time, String timetoken, String token) {
		//if token and g time either empty, return false;
		if (StringUtil.isEmpty(time) || StringUtil.isEmpty(token)) {
			return false;
		}
		
		String now = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
		Long gap = Long.valueOf(now) - Long.valueOf(time);
		LogManager.getLogger().debug("g:"+time +"|now:"+now+"|gap:"+gap);
		//if the gap between g time of request and now time of the filter bigger than 100, about 1 min, return false;
		if(gap>1000) {
			return false;
		}
		
		if (!timetoken.equals(TokenUtil.md5(time+token))) {
			LogManager.getLogger().debug("md5t?"+TokenUtil.md5(time+token));
			return false;
		}
		return true;
    }
    public static String addTimestampTokenUrl(String url, String token) {
    	String g = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
		String t = md5(g+token);
    	if(url.indexOf("?")==-1){
      		url = url + "?";
      	} else {
      		url = url + "&";
      	}
//    	url = url + "g="+g+"&t="+t;
    	url = url + "timestamp-token="+g+"."+t;
    	return url;
    }
    public static void main(String[] args) {
//		File desfile = new File("d:\\test.doc");
//		File desfile2 = new File("d:\\test2.doc");
//		File file = new File("D:\\opt\\dm\\labor\\documents\\ISO_DOC\\01.Design\\Software Acquisition Plan.doc");
//		FileInputStream fis = null;
//    	try {
//
//    		
//    		fis = new FileInputStream(file);
//    		FileInputStream fis2 = new FileInputStream(file);
//    		byte[] bytes2 = FileUtil.file2Bytes(file);
//    		byte[] bytes = FileUtil.stream2Bytes(fis);
//    		System.err.println("1"+TokenUtil.md5(bytes));
//    		System.err.println("1"+TokenUtil.md5(bytes2));
//    		System.err.println("5"+TokenUtil.md5(bytes));
//    		System.err.println("2"+TokenUtil.md5(fis2));
//    		System.err.println("3"+TokenUtil.md5(fis));
//    		System.err.println("4"+TokenUtil.md5(file));
//    		
//
//    		FileUtil.writeFile(bytes, desfile);
//    		System.err.println("6"+TokenUtil.md5(desfile));
//
//    		FileInputStream fis3 = new FileInputStream(file);
//    		FileUtil.writeFile(fis3, desfile2);
//    		System.err.println("7"+TokenUtil.md5(desfile));
//    	} catch (Exception e) {
//    		e.printStackTrace();
//		} finally {
//			try {
//				if (fis != null) {
//					LogManager.getLogger().error("222");
//					fis.close();fis.close();
//				}
//			} catch (IOException ie) {
//				LogManager.getLogger().error("", ie);
//			}
//		}
    	
    	
//    	String now1 = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
//    	String now2 = "20190803192159";
//    	String now3 = "20190803192260";
//    	Long i = Long.valueOf(now3) - Long.valueOf(now2);
//    	System.out.println(i+"|"+now1+"|"+now2);
//    	String AuthorizationCode = "ad89f721e3da95b11378c52112eaa492";
//    	
//    	String currentymdhms = "20190730174515";
//    			//new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
//    	System.out.println(generateUUID());
//    	String uuidtest = "8ba0216a-dfbc-2eaa-3b15-42cdf1eb59dd";
//    	
//    	System.out.println(TokenUtil.generateDateNum());
//    	System.out.println(TokenUtil.generateUUID());
//    	String uuid = TokenUtil.generateUUID("40f5db89b30c467ebb8a5377e77e9847");
//    	System.out.println(uuid);

    	long longid = TokenUtil.generateLongID();
    	System.out.println(longid);
    	long longid2 = TokenUtil.generateLongID(longid);
    	System.out.println(longid2);
    	
//    	String token = "20190803232509"+AuthorizationCode;
//    	
//
//        System.out.println(AuthorizationCode);
//        System.out.println(md5("value"));
//        System.out.println(token);
//        
//        //final token send to server. server will compare token==md5(currentymdhms+AuthorizationCode)?
//        token = md5("20190805221815"+"e10adc3949ba59abbe56e057f20f883e");
//        System.out.println(token);
//    	String str = "<p>内部网络不对外开放；</p><figure class=\"image\"><img src=\"/labor/attachment/filename/6e7b300156ba4b539cbf8686b0c5f9c9\"></figure>";
//    			
//    	System.out.println(str.replaceAll("<[^>]*>",""));
    }

}