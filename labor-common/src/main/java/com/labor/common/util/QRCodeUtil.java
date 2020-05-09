package com.labor.common.util;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import org.apache.logging.log4j.LogManager;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;



/**
 * @Description: (普通二维码生成)
 * @author：Relieved
 * @date：2014-11-7 下午04:42:35
 */
public class QRCodeUtil {
	public static void main(String[] args) throws IOException, WriterException {

//			生成二维码
		createCode();


	}
	
	public static byte[] createImageBytes(String content, int width, int height) {
		byte[] ret = null;
		String format = "png";
		HashMap hints = new HashMap();
		hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
		try {
			BitMatrix bitMatrix = new MultiFormatWriter().encode(content, BarcodeFormat.QR_CODE, width, height, hints);
			BufferedImage image = MatrixToImageWriter.toBufferedImage(bitMatrix);
			ret = FileUtil.bufferedImage2Bytes(image, format);
		} catch (WriterException e) {
			LogManager.getLogger().error("", e);
		}
		return ret;
	}

	/**
	 * 二维码的生成
	 *
	 */
	public static void createCode() {
		String text = "http://blog.csdn.net/gao36951";
		int width = 300;
		int height = 300;
		// 二维码的图片格式
		String format = "png";
		/**
		 * 设置二维码的参数
		 */
		HashMap hints = new HashMap();
		// 内容所使用编码
		hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
		try {
			BitMatrix bitMatrix = new MultiFormatWriter().encode(text, BarcodeFormat.QR_CODE, width, height, hints);
			// 生成二维码
			File outputFile = new File("D:" + File.separator + "test" + File.separator + "TDC-test.png");
			//MatrixToImageWriter.writeToFile(bitMatrix, format, outputFile);
			BufferedImage  bi = MatrixToImageWriter.toBufferedImage(bitMatrix);
			

		} catch (Exception e) {
			e.printStackTrace();
		}

	}


}
