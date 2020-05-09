package com.labor.common.util;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;

import javax.imageio.ImageIO;

import org.apache.logging.log4j.LogManager;

import com.drew.imaging.jpeg.JpegMetadataReader;
import com.drew.imaging.jpeg.JpegProcessingException;
import com.drew.imaging.png.PngMetadataReader;
import com.drew.imaging.png.PngProcessingException;
import com.drew.metadata.Directory;
import com.drew.metadata.Metadata;
import com.drew.metadata.MetadataException;
import com.drew.metadata.exif.ExifDirectoryBase;

import net.coobird.thumbnailator.Thumbnails;
import net.coobird.thumbnailator.Thumbnails.Builder;
import net.coobird.thumbnailator.geometry.Positions;

public class ImageUtil {
	
	public static final String IMAGE_COMPRESSED_SUFFIX = ".min";
	public static final int IMAGE_COMPRESSED_KBSIZE = 300;
	public static final String IMAGE_TYPES = "|.jpeg|.jpg|.png|.gif|.bmp|.tif|.psd|";
	
	public static void main(String[] args) {
		File file = new File("D:\\test\\test.jpeg");
		
		System.err.println(isImage("ere"));
		System.err.println(isImage("gif"));
		System.err.println(isImage(".jpg"));
		System.err.println(isImage(".jpeg"));
		
//		processRotateInfo(file,"jpg");
		
//		FileUtil.writeFile(resize(FileUtil.file2Bytes(file),"jpg", 300), "D:\\test\\test.min.jpeg");
//		FileUtil.writeFile(resizeThumbnails(FileUtil.file2Bytes(file),"jpg", null,null,null,null), "D:\\test\\test.thb.jpeg");
		
//		ImageUtil.getAccuracy(43433, 234);
	}
	public static byte[] processRotateInfo(File file, String fileType) {
//		InputStream in = null;
//		try {
//			in = new FileInputStream(file);
//			
//		} catch (IOException ioe) {
//			//allready closed in JpegMetadataReader.readMetadata(is);
//		}
		return processRotateInfo(FileUtil.file2Bytes(file),fileType);
		
	}
	public static byte[] processRotateInfo(byte[] bytes, String fileType) {
		byte[] ret = null;
        BufferedImage des = null;
        BufferedImage src = null;
        try {
			Metadata metadata = null;
			fileType = (fileType==null)?"":fileType.toLowerCase();
			LogManager.getLogger().debug("fileType: " + fileType);
			switch (fileType) {
				case "jpg":
					LogManager.getLogger().debug("metadata is read1: " + fileType);
					metadata = JpegMetadataReader.readMetadata(new ByteArrayInputStream(bytes));
					break;
				case "jpeg":
					LogManager.getLogger().debug("metadata is read2: " + fileType);
					metadata = JpegMetadataReader.readMetadata(new ByteArrayInputStream(bytes));
					break;
				case "png":
					LogManager.getLogger().debug("metadata is read3: " + fileType);
					metadata = PngMetadataReader.readMetadata(new ByteArrayInputStream(bytes));
					break;
				default:
					LogManager.getLogger().debug("metadata is read4: " + fileType);
//					metadata = JpegMetadataReader.readMetadata(new ByteArrayInputStream(bytes));
					break;
			}
			if (metadata!=null) {
				LogManager.getLogger().debug("metadata not null: " + fileType);
		        Directory directory = metadata.getFirstDirectoryOfType(ExifDirectoryBase.class);
		        int orientation = 0;
		        if (directory != null && directory.containsTag(ExifDirectoryBase.TAG_ORIENTATION)) { // Exif信息中有保存方向,把信息复制到缩略图
		            orientation = directory.getInt(ExifDirectoryBase.TAG_ORIENTATION); // 原图片的方向信息
		        }
		        LogManager.getLogger().debug("orientation:" + orientation);
		        if (orientation == 1) {
		            src = ImageIO.read(new ByteArrayInputStream(bytes));
		            des = ImageUtil.Rotate(src, 0);
		        } else if (6 == orientation) {
		            // 6旋转90
		            src = ImageIO.read(new ByteArrayInputStream(bytes));
		            des = ImageUtil.Rotate(src, 90);
		        } else if (3 == orientation) {
		            // 3旋转180
		            src = ImageIO.read(new ByteArrayInputStream(bytes));
		            des = ImageUtil.Rotate(src, 180);
		
		        } else if (8 == orientation) {
		            // 8旋转90
		            src = ImageIO.read(new ByteArrayInputStream(bytes));
		            des = ImageUtil.Rotate(src, 270);
		        }
			}
		} catch (IOException ioe) {
			LogManager.getLogger().error("", ioe);
		} catch (JpegProcessingException jpe) {
			LogManager.getLogger().error("", jpe);
		} catch (PngProcessingException jpe) {
			LogManager.getLogger().error("", jpe);
		} catch (MetadataException me) {
			LogManager.getLogger().error("", me);
		} finally {
			//allready closed in JpegMetadataReader.readMetadata(is);
//            try {
//                if (null != is) {
//                	is.close();
//                }
//            } catch (IOException ex) {
//            	LogManager.getLogger().error("", ex);
//            } 
		}

        if (des!=null){
            ret = FileUtil.bufferedImage2Bytes(des, fileType);
        } else {
        	ret = bytes;
        }
        return ret;
	}
	
	/**
	 * resize image to certain accuracy or height or width, and add watarmark;
	 * @param inputBytes
	 * @param fileType
	 * @param accuracy
	 * @param height
	 * @param width
	 * @param waterMarkFileFullPath
	 * @return
	 */
	public static byte[] resizeThumbnails(byte[] inputBytes, String fileType, Double accuracy, Integer height, Integer width, String waterMarkFileFullPath) {
		byte[] ret = inputBytes;
		BufferedImage bufferedImage = FileUtil.bytes2BufferedImage(inputBytes);
		if (bufferedImage != null) {
			LogManager.getLogger().debug("accuracy"+accuracy+"|height"+height+"|width"+width);
			ByteArrayOutputStream os = null;
			try{
				os = new ByteArrayOutputStream(); 
				Builder<BufferedImage> builder = Thumbnails.of(bufferedImage);
				
				if (height!=null&&height>0) {
					builder.height(height);
				} else if (width!=null&&width>0) {
					builder.width(width);
				} else if (accuracy!=null&&accuracy>0) {
					builder.scale(accuracy);
					builder.outputQuality(accuracy);
				} else {
					long size = inputBytes.length / 1024;
					if (size < 100) {
						accuracy = 0.5;
					} else if (size < 900) {
						accuracy = 0.3;
					} else if (size < 2047) {
						accuracy = 0.2;
					} else if (size < 3275) {
						accuracy = 0.1;
					} else {
						accuracy = 0.1;
					}
					builder.scale(accuracy);
					builder.outputQuality(accuracy);
				}
				LogManager.getLogger().debug("xxxxxxxxwatermark:"+waterMarkFileFullPath);
				//WebUtil.getClassPath() + properties.IMG_DIR + File.separator + properties.IMG_WATERMARK_FILE
				if (!StringUtil.isEmpty(waterMarkFileFullPath)) {
					builder.watermark(Positions.CENTER, ImageIO.read(
							new File(waterMarkFileFullPath)), 0.1f);
				}
				
				builder.outputFormat(fileType);
				builder.toOutputStream(os);
							
				ret = os.toByteArray();
				LogManager.getLogger().debug("xxxxxxxxinputBytes"+inputBytes.length);
				LogManager.getLogger().debug("xxxxxxxoutputBytes"+ret.length);
				
			} catch(Exception ioe) {
				LogManager.getLogger().error("", ioe);
			} finally {
	            try {
	                if (null != os) {
	                	os.close();
	                }
	            } catch (IOException ex) {
	            	LogManager.getLogger().error("", ex);
	            } 
			}		
		}
		return ret;
	}
	
	/**
	 * resize image to certain size;
	 * @param inputBytes
	 * @param fileType
	 * @param toBeKBSize
	 * @return
	 */
	public static byte[] resize(byte[] inputBytes, String fileType, int toBeKBSize) {
		byte[] ret = inputBytes;
		if (inputBytes == null || inputBytes.length <= 0 || inputBytes.length < toBeKBSize * 1024) {
            return inputBytes;
        }
        int srcSize = inputBytes.length;
        
        double accuracy = getAccuracy(srcSize);
        ByteArrayInputStream inputStream = null;
        ByteArrayOutputStream outputStream = null;
        
        try {
        	while (inputBytes.length > toBeKBSize * 1024) {
	            inputStream = new ByteArrayInputStream(inputBytes);
	            outputStream = new ByteArrayOutputStream(inputBytes.length);
	            Thumbnails.of(inputStream)
	                    .scale(accuracy)
	                    .outputQuality(accuracy)
	                    .toOutputStream(outputStream);
	            inputBytes = outputStream.toByteArray();
        	}
			ret = inputBytes;
			LogManager.getLogger().debug("xxxxxxxxinputBytes"+srcSize);
			LogManager.getLogger().debug("xxxxxxxoutputBytes"+ret.length);
        } catch (IOException e) {
        	LogManager.getLogger().error("", e);
        }  finally {
            try {
                if (null != inputStream) {
                	inputStream.close();
                }
            } catch (IOException ex) {
            	LogManager.getLogger().error("", ex);
            } 
            try {
                if (null != outputStream) {
                	outputStream.close();
                }
            } catch (IOException ex) {
            	LogManager.getLogger().error("", ex);
            } 
		}
        return ret;
	}

	
	public static boolean isImage(String filetype) {
		return (IMAGE_TYPES.indexOf(StringUtil.safeToString(filetype).toLowerCase())>0)?true:false;
	}
	public static boolean isImage(byte[] bytes) {
		try {
		    Image image = ImageIO.read(new ByteArrayInputStream(bytes));
		    return image != null;
		} catch(IOException ex) {
		    return false;
		}
	}
	
	public static boolean isImage(File file) {
		try {
		    Image image = ImageIO.read(file);
		    return image != null;
		} catch(IOException ex) {
		    return false;
		}
	}
	
	private static double getAccuracy(int size, int toSize) {

        System.err.println("size:"+size);
        System.err.println("toSize:"+toSize);

        DecimalFormat df=new DecimalFormat("0.00");
		if (size<=0) {
			return 0;
		}
		double ret = (double)toSize/size;
		System.err.println(df.format(ret));
		ret = (double) Math.round(ret * 130) / 100;
		System.err.println(ret);
		return ret;
	}
	/**
	 * default accuracy
	 * @param kbsize
	 * @return
	 */
    private static double getAccuracy(int kbsize) {
        double accuracy;
        if (kbsize < 900) {
            accuracy = 0.85;
        } else if (kbsize < 2047) {
            accuracy = 0.6;
        } else if (kbsize < 3275) {
            accuracy = 0.44;
        } else {
            accuracy = 0.4;
        }
        return accuracy;
    }
	private static BufferedImage Rotate(Image src, int angel) {
		int src_width = src.getWidth(null);
		int src_height = src.getHeight(null);
		// calculate the new image size
		Rectangle rect_des = CalcRotatedSize(new Rectangle(new Dimension(src_width, src_height)), angel);

		BufferedImage res = null;
		res = new BufferedImage(rect_des.width, rect_des.height, BufferedImage.TYPE_INT_RGB);
		Graphics2D g2 = res.createGraphics();
		// transform
		g2.translate((rect_des.width - src_width) / 2, (rect_des.height - src_height) / 2);
		g2.rotate(Math.toRadians(angel), src_width / 2, src_height / 2);

		g2.drawImage(src, null, null);

		return res;
	}

	private static Rectangle CalcRotatedSize(Rectangle src, int angel) {
		// if angel is greater than 90 degree, we need to do some conversion
		if (angel >= 90) {
			if (angel / 90 % 2 == 1) {
				int temp = src.height;
				src.height = src.width;
				src.width = temp;
			}
			angel = angel % 90;
		}

		double r = Math.sqrt(src.height * src.height + src.width * src.width) / 2;
		double len = 2 * Math.sin(Math.toRadians(angel) / 2) * r;
		double angel_alpha = (Math.PI - Math.toRadians(angel)) / 2;
		double angel_dalta_width = Math.atan((double) src.height / src.width);
		double angel_dalta_height = Math.atan((double) src.width / src.height);

		int len_dalta_width = (int) (len * Math.cos(Math.PI - angel_alpha - angel_dalta_width));
		int len_dalta_height = (int) (len * Math.cos(Math.PI - angel_alpha - angel_dalta_height));
		int des_width = src.width + len_dalta_width * 2;
		int des_height = src.height + len_dalta_height * 2;

		return new Rectangle(new Dimension(des_width, des_height));
	}
}
