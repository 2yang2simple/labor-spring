package com.labor.spring.system.oss;

import java.io.ByteArrayInputStream;
import java.net.URL;
import java.util.Date;
import java.util.List;

import org.apache.logging.log4j.LogManager;

import com.aliyun.oss.HttpMethod;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.model.Bucket;
import com.aliyun.oss.model.GeneratePresignedUrlRequest;
import com.aliyun.oss.model.PutObjectRequest;

public class AliyunOssUtil {

	public static String SYSCONFIG_KEY = "ALIYUNOSS_ACCESSKEY";
	public static String protocol = "https://";
	public static String domainName = "oss-cn-shenzhen.aliyuncs.com";
	public static String bucketName = "labor-oss-2020";
	public static String endpoint = protocol + domainName;

	public static String ossUrl = protocol + bucketName + "." + domainName;
	
	public static String process_style_watermark = "?x-oss-process=style/watermark-yy";
	
	public static String accessKeyId = "";
	public static String accessKeySecret = "";

	public static void main(String[] args) {
		test();
	}
	
	public static void test() {
		OSS ossClient = null;
		try {
			ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);
			// 列举存储空间。
			List<Bucket> buckets = ossClient.listBuckets();
			for (Bucket bucket : buckets) {
			    System.out.println(" - " + bucket.getName());
			}
		} catch (Exception e) {
			LogManager.getLogger().error(e);
		} finally {
			// 关闭OSSClient。
			if (ossClient!=null) {
				ossClient.shutdown();
			}
		}
	}

	public static void download() {
		// 创建OSSClient实例。
		OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);
		// 设置图片处理样式。
		String style = "image/resize,m_fixed,w_100,h_100/rotate,90";
		// 指定过期时间为10分钟。
		Date expiration = new Date(new Date().getTime() + 1000 * 60 * 10 );
		GeneratePresignedUrlRequest req = new GeneratePresignedUrlRequest(bucketName, "no_picture.gif", HttpMethod.GET);
		req.setExpiration(expiration);
		req.setProcess(style);
		URL signedUrl = ossClient.generatePresignedUrl(req);
		System.out.println(signedUrl);
		// 关闭OSSClient。
		ossClient.shutdown();
	}
	
	public static void upload(String fileFullPath, byte[] fileBytes) {
		OSS ossClient = null;
		//abc/efg/123.jpg。
		if (fileFullPath==null||fileBytes==null) {
			return;
		}
		try {
			ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);
			ossClient.putObject(bucketName, fileFullPath, new ByteArrayInputStream(fileBytes));
		} catch (Exception e) {
			LogManager.getLogger().error(e);
		} finally {
			// 关闭OSSClient。
			if (ossClient!=null) {
				ossClient.shutdown();
			}
		}
	}
}
