package com.labor.common.util;

import java.awt.image.BufferedImage;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.lang.reflect.Method;
import java.net.URL;

import javax.imageio.ImageIO;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class FileUtil {

	private static Logger log = LogManager.getLogger();

	public static InputStream loadFile(String filename, Class clazz) {
		ClassLoader classLoader = null;
		try {
			Method method = Thread.class.getMethod("getContextClassLoader", new Class[] {});
			classLoader = (ClassLoader) method.invoke(Thread.currentThread(), new Class[] {});
		} catch (Exception e) {
			log.error("", e);
		}
		if (classLoader == null) {
			classLoader = clazz.getClassLoader();
		}
		try {
			if (classLoader != null) {
				URL url = classLoader.getResource(filename);
				if (url == null) {
					System.out.println("Can not find file:" + filename);
					return null;
				}
				if (url.toString().startsWith("jar:file:")) {
					System.out.println("Get file \"" + filename + "\" from jar:\t" + url.toString());
					return clazz.getResourceAsStream(filename.startsWith("/") ? filename : "/" + filename);
				} else {
					System.out.println("Get file \"" + filename + "\" from:\t" + url.toString());
					return new FileInputStream(new File(url.toURI()));
				}
			}
		} catch (Exception e) {
			log.error("", e);
		}
		return null;
	}


	/**
	 * @param file
	 * @param desFilePath
	 */
	public static void saveFile(File file, String desFilePath) {
		File desFile = new File(desFilePath);
		saveFile(file, desFile);
	}

	/**
	 * save file to destination dir
	 * 
	 * @param file
	 * @param des
	 */
	public static void saveFile(File file, File desFile) {
		if (file == null || !file.exists()) {
			log.error("original file is not exist: " + desFile);
			return;
		}
		FileInputStream in = null;
		FileOutputStream out = null;
		try {
			if (desFile.getParentFile() != null && !desFile.getParentFile().exists()) {
				desFile.getParentFile().mkdirs();
			}
			in = new FileInputStream(file);
			out = new FileOutputStream(desFile);
			byte[] buffer = new byte[10];
			int len = 0;
			while ((len = in.read(buffer)) != -1) {
				out.write(buffer, 0, len);
				;
			}
		} catch (FileNotFoundException fnfe) {
			log.error("", fnfe);
		} catch (IOException ioe) {
			log.error("", ioe);
		} finally {
			try {
				if (in != null) {
					in.close();
				}
			} catch (IOException ioe) {
				log.error("", ioe);
			}
			try {
				if (out != null) {
					out.flush();
					out.close();
				}	
			} catch (IOException ioe) {
				log.error("", ioe);
			}
		}

	}

	public static boolean writeFile(String content, String path) {
		boolean ret = false;
		FileOutputStream fos = null;
		BufferedWriter bw = null;
		try {
			File file = new File(path);
			fos = new FileOutputStream(file);
			bw = new BufferedWriter(new OutputStreamWriter(fos, "GB2312"));
			bw.write(content);
			ret = true;
		} catch (FileNotFoundException fnfe) {
			log.error("", fnfe);
		} catch (IOException ioe) {
			log.error("", ioe);
		} finally {
            try {
                if (null != bw) {
                	bw.close();
                }
            } catch (IOException ex) {
            	log.error("", ex);
            } 
            try {
                if(null!=fos){
                	fos.close();
                }
            } catch (IOException ex) {
            	log.error("", ex);
            }
		}
		return ret;
	}
	public static boolean writeFile(byte[] content, String path) {
		return writeFile(content, new File(path));
	}
	
	public static boolean writeFile(byte[] content, File file) {
		boolean ret = false;
		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(file);
			fos.write(content);
			ret = true;
		} catch (FileNotFoundException fnfe) {
			log.error("", fnfe);
		} catch (IOException ioe) {
			log.error("", ioe);
		} 
		try {
			if (fos != null) {
				fos.close();
			}
		} catch (IOException ie) {
			log.error("", ie);
		}
		return ret;
	}

	public static boolean writeFile(InputStream is, File file) {
		boolean ret = false;
		if (is==null) {
			return ret;
		}
		OutputStream os = null;
		try {
			os = new FileOutputStream(file);
			int bytesRead = 0;
			byte[] buffer = new byte[8192];
			while ((bytesRead = is.read(buffer, 0, 8192)) != -1) {
				os.write(buffer, 0, bytesRead);
			}
			ret = true;
		} catch (IOException ioe) {
			log.error("", ioe);
		} finally {
            try {
                if (null != os) {
                	os.close();
                }
            } catch (IOException ex) {
            	log.error("", ex);
            } 
            try {
                if(null!=is){
                	is.close();
                }
            } catch (IOException ex) {
            	log.error("", ex);
            }
		}
		return ret;
	}
	
	public static byte[] stream2Bytes(InputStream is) {
		byte[] ret = null;
		if (is==null) {
			return ret;
		}
		ByteArrayOutputStream baos = null;
		try {
			baos = new ByteArrayOutputStream();  
			byte[] buffer = new byte[1024];  
			int len;  
			while ((len = is.read(buffer)) > -1 ) {  
			    baos.write(buffer, 0, len);  
			}  
			baos.flush();  
			ret = baos.toByteArray();
		} catch (IOException ioe) {
			log.error("", ioe);
		} finally {
            try {
                if (null != baos) {
                	baos.close();
                }
            } catch (IOException ex) {
            	log.error("", ex);
            } 
            try {
                if(null!=is){
                	is.close();
                }
            } catch (IOException ex) {
            	log.error("", ex);
            }
		}
		return ret;
		        
	}
	public static InputStream bytes2InputStream(byte[] bytes) {
		InputStream ret = new ByteArrayInputStream(bytes);
		return ret;
	}
	public static byte[] bufferedImage2Bytes(BufferedImage image, String format) {
		byte[] ret = null;
		if (image==null) {
			return ret;
		}
		ByteArrayOutputStream out = null;
		try {
			out = new ByteArrayOutputStream();
			ImageIO.write(image, format, out);
			ret = out.toByteArray();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
            try {
                if (null != out) {
                	out.close();
                }
            } catch (IOException ex) {
            	LogManager.getLogger().error("", ex);
            } 
		}
		return ret;
	}

	public static BufferedImage bytes2BufferedImage(byte[] bytes) {
		BufferedImage ret = null;
		if (bytes==null) {
			return ret;
		}
		ByteArrayInputStream is = null;
		try {
			is = new ByteArrayInputStream(bytes); 
			ret = ImageIO.read(is);
		} catch(IOException ioe) {
			LogManager.getLogger().error("", ioe);
		} finally {
            try {
                if (null != is) {
                	is.close();
                }
            } catch (IOException ex) {
            	LogManager.getLogger().error("", ex);
            } 
		}
		return ret;
	}
	/**
	 * get the file byte
	 * 
	 * @param fileName
	 * @return
	 */
	public static byte[] file2Bytes(String fullFileName) {
		 File file = new File(fullFileName);
		 return file2Bytes(file);
	}
	public static byte[] file2Bytes(File file) {
		byte[] ret = null;
        FileInputStream fis = null;
        ByteArrayOutputStream bos = null;

        try {
            fis = new FileInputStream(file);
            bos = new ByteArrayOutputStream();

            byte[] b = new byte[1024];

            int n;

            while ((n = fis.read(b)) != -1) {
                bos.write(b, 0, n);
            }
            
            ret = bos.toByteArray();
        } catch (FileNotFoundException ex) {
        	log.error("", ex);
        } catch (IOException ex) {
        	log.error("", ex);
        } finally {
            try {
                if (null != bos) {
                    bos.close();
                }
            } catch (IOException ex) {
            	log.error("", ex);
            } 
            try {
                if(null!=fis){
                    fis.close();
                }
            } catch (IOException ex) {
            	log.error("", ex);
            }
        }
        
        return ret;
	}
	/*
	 * return the file name
	 */
	public static String getFileName(String fileFullName) {
		if (fileFullName.lastIndexOf(".")<0) {
			return fileFullName;
		}
		return fileFullName.substring(0, fileFullName.lastIndexOf("."));
	}

	/*
	 * return file ext name;
	 */
	public static String getFileType(String fileFullName) {
		if (fileFullName.lastIndexOf(".")<0) {
			return "";
		}
		return fileFullName.substring(fileFullName.lastIndexOf(".") + 1);
	}

	public static void main(String[] args) {
		String fileName = "f025a6d7dfd340c68a6f6a67ff3084af.png";
		fileName = FileUtil.getFileName(fileName);
		System.err.println(fileName);
		fileName = FileUtil.getFileName(fileName);
		System.err.println(fileName);
		fileName = FileUtil.getFileType(fileName);
		fileName = FileUtil.getFileType(fileName);
		System.err.println("|"+fileName);
//		File file = new File("D:\\test\\69310321-5.jpg");
////		FileUtil.saveFile(file, "d:/opt/dm/labor/attachments/1223124324322242.doc");
//		System.out.println(file.getName() + "|" + file.getPath());
//		System.out.println("name:" + getFileName(file.getName()));
//		System.out.println(getFileType(file.getName()));
//
//		try {
//			InputStream in = new FileInputStream(file);
//			long size = file.length() / 1024;
//
//			double accuracy;
//			if (size < 200) {
//				accuracy = 1;
//			} else if (size < 900) {
//				accuracy = 0.85;
//			} else if (size < 2047) {
//				accuracy = 0.6;
//			} else if (size < 3275) {
//				accuracy = 0.44;
//			} else {
//				accuracy = 0.4;
//			}
//			System.out.println(size + "|" + file.length() + "|" + accuracy);
//
//		} catch (IOException ioe) {
//			log.error("", ioe);
//		}
	}
	
	public static void iterateFolder(String path, Integer parentid, String parentname) {
		File file = new File(path);
		File[] files = file.listFiles();
		if (files == null) {
			return;
		}

		for (File f : files) {
			if (f.isFile()) {
				//do some file thing; tag = parentid,

				
			} if (f.isDirectory()) {
				
				//do some insert get a new parentid
				Integer newparentid = 0;
				String newparentname = "newname";
				iterateFolder(f.getAbsolutePath(),newparentid,newparentname);

				
			}
		}
	}
}
