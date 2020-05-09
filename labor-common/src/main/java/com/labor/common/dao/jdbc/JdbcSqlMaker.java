package com.labor.common.dao.jdbc;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.RandomAccessFile;
import java.net.URL;

import com.labor.common.util.FileUtil;

public class JdbcSqlMaker {
	
	private static final String FILE_FOLDER = "jdbc";
	private static final String FILE_TYPE = "sql";
	private static final String NOTE_SYMBOL = "--";
	
	/***
	 * generate the sql via the file, and replace the parameters 
	 * via the object and class type.
	 * @param file
	 * @param paramobj
	 * @param paraclazz
	 * @return
	 * @throws Exception
	 */
	public static String getSql(String file,Object paramobj, Class paraclazz) throws Exception{
		String ret = null;
		String filename = FILE_FOLDER+File.separator+file+"."+FILE_TYPE;
		ret =  bufferdReaderToString(filename);
		ret =  randomAccessFileToString(filename);
		return ret;
	}
	
	/**
	 * replace the parameters via the object and class type.
	 * @param sql
	 * @param paramobj
	 * @param paraclazz
	 * @return
	 */
	private static String replacePara(String sql, Object paramobj, Class paraclazz){
		String ret = null;
		
//		Method getMethod = paraclazz.getMethod("get"+upperFirstLetter(columnlabel),
//				new Class[]{});
//		Method setMethod = paraclazz.getMethod("set"+upperFirstLetter(columnlabel),
//				new Class[]{getMethod.getReturnType()});
		return ret;
	}
	/**
	 * read the file via bufferdreader
	 * high performance cause each read via the cached memory
	 * @param filename
	 * @return
	 * @throws Exception
	 */
	private static String bufferdReaderToString(String filename) throws Exception{
		String ret = null;
		InputStream is = FileUtil.loadFile(filename, JdbcSqlMaker.class);
		BufferedReader br = new BufferedReader(new InputStreamReader(is));
		long begin = System.currentTimeMillis();
		try {		
			StringBuffer sb = new StringBuffer();
			String s = null;
			while ((s=br.readLine())!=null) {
				s = s.trim();
		    	String tmp[] = s.split(NOTE_SYMBOL);
	    		sb.append(tmp[0]).append(" ");   	
			}
			ret = sb.toString();
			System.out.println(ret);
		} catch (Exception e){
			throw e;
		} finally {
			if (is!=null){is.close();}
			if (br!=null){br.close();}
		}
		long end = System.currentTimeMillis();
		System.out.println("t:"+(end-begin));
		return ret;
	}
	
	/**
	 * read the file via randmaccessfile
	 * low performance cause the each read via the file
	 * @param filename
	 * @return
	 * @throws Exception
	 */
	private static String randomAccessFileToString(String filename) throws Exception{
		String ret = null;
		URL url = JdbcSqlMaker.class.getClassLoader().getResource(filename);
		RandomAccessFile raf = new RandomAccessFile(new File(url.toURI()),"r");
		long begin = System.currentTimeMillis();
		try {
			StringBuffer sb = new StringBuffer();
			while (raf.getFilePointer() < raf.length()) {
			    String s = raf.readLine().trim();
		    	String tmp[] = s.split(NOTE_SYMBOL);
	    		sb.append(tmp[0]).append(" ");   	
			}
			ret = sb.toString();
			System.out.println(ret);
		} catch (Exception e){
			throw e;
		} finally {
			if (raf!=null){raf.close();}
		}
		long end = System.currentTimeMillis();
		System.out.println("t:"+(end-begin));
		return ret;
	}

	public static void main(String arg[]){
		try {
			JdbcSqlMaker.getSql("test",null,null);
//			System.out.println(SqlMaker.getSql("test",null).indexOf(NOTE_SYMBOL));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
