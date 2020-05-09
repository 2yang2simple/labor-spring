package com.labor.common.util;

import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import org.apache.commons.codec.binary.Base64;
import org.apache.logging.log4j.LogManager;

public class RSAUtil {
	public static final String KEY_ALGORITHM = "RSA";
	public static final String SIGNATURE_ALGORITHM = "MD5withRSA";

	private static final String PUBLIC_KEY = "RSAPublicKey";
	private static final String PRIVATE_KEY = "RSAPrivateKey";
	
	private static final String DEFAULT_PUBLICKEY_1024="MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCZR/nUftLb/IbwJTdZKVY/BQWbx3hVIcIi2BiRQN9/2d0+yTKxBVAP87sDkC81p/6x/7j6VCjPF60pIdnW94otBKncpmWZzuKITL9L3/wfoA7M4GMP0AKm7loFOAJQIBZfZs0MmZjsx9iy/xFffVgf6OxgpvITJsKDb6rFvmkWLQIDAQAB";
	private static final String DEFAULT_PRIVATEKEY_1024="MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAJlH+dR+0tv8hvAlN1kpVj8FBZvHeFUhwiLYGJFA33/Z3T7JMrEFUA/zuwOQLzWn/rH/uPpUKM8XrSkh2db3ii0EqdymZZnO4ohMv0vf/B+gDszgYw/QAqbuWgU4AlAgFl9mzQyZmOzH2LL/EV99WB/o7GCm8hMmwoNvqsW+aRYtAgMBAAECgYBmAkeainiL3Mh0w1/NX7w9Hnt4C8ziYyAPDl9zM4hKTtR193Jb6tw4cY0xNuCkRdQz46rYNfAeunQMHrIZ5Q24FjkbmlOT6FIkCVjupHtGwXFvnCgmAdvZQCAz6n5u0XKtkeLbIKcHcr7GY2j6ayD5gCVEsXzQBAyhKxGBW362wQJBAM3l2aUSjuEDVYmG9WfxoIxYdPVZBpPgPwodoWfOmjl5Jqob0RWsKmhMHJMYcNpNekNHkdb1VoSdeDkG03xisrECQQC+lG8sIUAAjeivpTTAeLsYPhCC/cSMq9QnxidUV0WzDB1gNxfX2LtXxU3eBvMb3SAFOAdTaL7Dy/7oIyGB+SI9AkAKpBVidZoSwPRbvjDSpWcxuaSZxXcUj2NSp4Ne9Qt9auh3pCZjI1mxuDVF2N6EObrW3U0JOfa1tdmvwM2PQR6RAkBsHBXoqb/90IKTyRrgqFwyB1skg8JQrlS+b1j2Lj+6rv+nCfm7lxXHV4Sxk62rO8gjTcJSHZRBy+IBW5Rxf5tdAkEAkZX3djJ5cbz1Kn8pwLgF/FBzj6EiNi8+1CoBDs9SfGxx1a2licmkbg++akN4HEoyvIdlgPAxvLL61tUlDHOLGw==";
	
	private static final String DEFAULT_PUBLICKEY_512="MFwwDQYJKoZIhvcNAQEBBQADSwAwSAJBAJuUERa40BTJlWYw6OYgj57FYzhNaSGylkM7vxTv01WL4wt+OhoMgjBOHkrTqgqnPLdc2R3UKGA5R8w+Z+89lZ8CAwEAAQ==";
	private static final String DEFAULT_PRIVATEKEY_512="MIIBVQIBADANBgkqhkiG9w0BAQEFAASCAT8wggE7AgEAAkEAm5QRFrjQFMmVZjDo5iCPnsVjOE1pIbKWQzu/FO/TVYvjC346GgyCME4eStOqCqc8t1zZHdQoYDlHzD5n7z2VnwIDAQABAkBfUvLBhE1iXz0d1xvsXMkC1Bh+F9gxoHW3DjV/A47uW4eZT3ZNAKgDmYJgKPq8PsfJL/EMZrV72ibMUf/bQB7xAiEA5kZGQ4cziaaEQQZjrJlBl3QftpLdtbOxjCjS1E6taLkCIQCs9YYZ7ZS+CXGxs18o7w0EGGrtW6CujBHMcM0516IVFwIhAKJLuU3yxmIn2GK0DCAjy9Tg5VEaZeSgh2xPgXhdE18RAiAfi5ROzE8xreVHpNB5VchI8+8kpW6UB0YDxJsZrqOINwIhAJ/mB1S33SysQ7x/mh9p2Jp7Zi/7lYRfqliXpFPE2vKC";
//	publickey:MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCZR/nUftLb/IbwJTdZKVY/BQWbx3hVIcIi2BiRQN9/2d0+yTKxBVAP87sDkC81p/6x/7j6VCjPF60pIdnW94otBKncpmWZzuKITL9L3/wfoA7M4GMP0AKm7loFOAJQIBZfZs0MmZjsx9iy/xFffVgf6OxgpvITJsKDb6rFvmkWLQIDAQAB
//	private:MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAJlH+dR+0tv8hvAlN1kpVj8FBZvHeFUhwiLYGJFA33/Z3T7JMrEFUA/zuwOQLzWn/rH/uPpUKM8XrSkh2db3ii0EqdymZZnO4ohMv0vf/B+gDszgYw/QAqbuWgU4AlAgFl9mzQyZmOzH2LL/EV99WB/o7GCm8hMmwoNvqsW+aRYtAgMBAAECgYBmAkeainiL3Mh0w1/NX7w9Hnt4C8ziYyAPDl9zM4hKTtR193Jb6tw4cY0xNuCkRdQz46rYNfAeunQMHrIZ5Q24FjkbmlOT6FIkCVjupHtGwXFvnCgmAdvZQCAz6n5u0XKtkeLbIKcHcr7GY2j6ayD5gCVEsXzQBAyhKxGBW362wQJBAM3l2aUSjuEDVYmG9WfxoIxYdPVZBpPgPwodoWfOmjl5Jqob0RWsKmhMHJMYcNpNekNHkdb1VoSdeDkG03xisrECQQC+lG8sIUAAjeivpTTAeLsYPhCC/cSMq9QnxidUV0WzDB1gNxfX2LtXxU3eBvMb3SAFOAdTaL7Dy/7oIyGB+SI9AkAKpBVidZoSwPRbvjDSpWcxuaSZxXcUj2NSp4Ne9Qt9auh3pCZjI1mxuDVF2N6EObrW3U0JOfa1tdmvwM2PQR6RAkBsHBXoqb/90IKTyRrgqFwyB1skg8JQrlS+b1j2Lj+6rv+nCfm7lxXHV4Sxk62rO8gjTcJSHZRBy+IBW5Rxf5tdAkEAkZX3djJ5cbz1Kn8pwLgF/FBzj6EiNi8+1CoBDs9SfGxx1a2licmkbg++akN4HEoyvIdlgPAxvLL61tUlDHOLGw==
//	publickey:MFwwDQYJKoZIhvcNAQEBBQADSwAwSAJBAJuUERa40BTJlWYw6OYgj57FYzhNaSGylkM7vxTv01WL4wt+OhoMgjBOHkrTqgqnPLdc2R3UKGA5R8w+Z+89lZ8CAwEAAQ==
//	private:MIIBVQIBADANBgkqhkiG9w0BAQEFAASCAT8wggE7AgEAAkEAm5QRFrjQFMmVZjDo5iCPnsVjOE1pIbKWQzu/FO/TVYvjC346GgyCME4eStOqCqc8t1zZHdQoYDlHzD5n7z2VnwIDAQABAkBfUvLBhE1iXz0d1xvsXMkC1Bh+F9gxoHW3DjV/A47uW4eZT3ZNAKgDmYJgKPq8PsfJL/EMZrV72ibMUf/bQB7xAiEA5kZGQ4cziaaEQQZjrJlBl3QftpLdtbOxjCjS1E6taLkCIQCs9YYZ7ZS+CXGxs18o7w0EGGrtW6CujBHMcM0516IVFwIhAKJLuU3yxmIn2GK0DCAjy9Tg5VEaZeSgh2xPgXhdE18RAiAfi5ROzE8xreVHpNB5VchI8+8kpW6UB0YDxJsZrqOINwIhAJ/mB1S33SysQ7x/mh9p2Jp7Zi/7lYRfqliXpFPE2vKC

	public static void main(String[] args) throws Exception {
//		RSAUtil.initKey();
		String publicKey =  "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCwjDm1HXDw8QH5ZtGMQIl2h/I8E+chOQA8aQ8xCR/+aHnROaN/ZU5Vmd2Zz7g6cAacR9BSm60+iSCYtvEGJKl0WqvbPGJkc8tedjNF1QqgWqkkuE6Udgw2OkEKJCxDg6PrAniR7Cc0io9G8bW4P8JDJjSbbafvMPDDFbVVUWJxxwIDAQAB";
//		String publicKey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCjSVUNB0A8lEmtvhzdEuvmxOfeEV3cc90qyttUvUPPmygHCoL2uwTqVRIW3fFQaYWIz0WR1QnIIsJNYxN9lziJrufSQq50XRU/WSdWT+NKYFRlMtM4iaB5fwe/dggQiZmaAgz7VLBZENQJh3WhcluHhaeypEtNqecwiooTmQ1dHQIDAQAB";
		String privateKey = "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBALCMObUdcPDxAflm0YxAiXaH8jwT5yE5ADxpDzEJH/5oedE5o39lTlWZ3ZnPuDpwBpxH0FKbrT6JIJi28QYkqXRaq9s8YmRzy152M0XVCqBaqSS4TpR2DDY6QQokLEODo+sCeJHsJzSKj0bxtbg/wkMmNJttp+8w8MMVtVVRYnHHAgMBAAECgYAOLuW/8CKPqL0A3Uq+WrzwYdGLFApAeATV1Zbb2KDSXnBS56+D346gf+D2p2Jkh3VwfrB0wn7zhC6zNhc86BsY1K6Q7xU8b7asDBqki48H3ExuxjEosEqUdLf9p9pPBCPI3I4CN/EZPEoFjNRNi5ZX/CY4iaOgsXPHEkcxuW3GQQJBAOBo9UpXesZYCsmuuGN5SOy6tXI613NUBvXKXkxBil3ZOqozlaSjv5NSQb2zLeh2DcYfecumfgn04rNM/pLeDmECQQDJZnXWg4VrXdjc9hqu/8rkmxdhsr3ERkX1uKJrDUB+AOdFs6mS9gEHnJ70zqQ2ucbhQ4htulbLc9pBVL5gy+EnAkEArdhhfa/7SsBVyxvxeA4zMkEJ4242Df/gTHTzTDvRxxZL3iKMILlB5gzpJN40CEu8K+miXuOh7HCrVp+k733awQJBAMDkERhS/wXF7F40l3nkIz6wC8TWnEnPxFGDdItzNcF4vAhV+qN2WaYgq11sTHrdk01MkO4G+foCC5dmwq+SlSECQGm58ReqUTRDAKl32VX0vEdVvOj2getVxW6jQjJFiGj8iNDfK+DpiLfns3YjwSlWHGxHz1S6/lQ+58+M+fEyvUs=";
//		String privateKey = "MIICeAIBADANBgkqhkiG9w0BAQEFAASCAmIwggJeAgEAAoGBAKNJVQ0HQDyUSa2+HN0S6+bE594RXdxz3SrK21S9Q8+bKAcKgva7BOpVEhbd8VBphYjPRZHVCcgiwk1jE32XOImu59JCrnRdFT9ZJ1ZP40pgVGUy0ziJoHl/B792CBCJmZoCDPtUsFkQ1AmHdaFyW4eFp7KkS02p5zCKihOZDV0dAgMBAAECgYEAj/RsySmlVkMdmdFLvRObaj4adUs2Rs7XI4zoRRL/H56YcWyw/TjwIm+RnRtaThxZn72shd8d2pXaW0OHwWd6bW3858U2ZnjQHzfnTG7GG1P3OgVp9a6VcnV0h5vZHsu0VmFH+9Wf93YQPlZPcIwSI7MzxTgX9B4aBB6Vv5d5nHkCQQDgQLrngaxEZMyExlp6liVN94ZwAtAnNxXVZ7/EZJV/YE3xZy+aw/vd8hHgVWSg7ty2y/o4u9arQv4VaOnXYLmLAkEAumcV7c40RbZAsOvyxsQecWin8N4O+CzKKyGqG5nqjKj++wGYlpfmdpzK72tIJ3LsqSnuHxkgmohKjuP5QIoI9wJBANDF0nt28L7XrMOruk5H/EDiwUkEhW8MzInZxeF71uY37Thh3SLTdGeDVnsoZL+iwN55deFdlpPokY2zF4jI5tMCQDZXizsS3IZeRanOf66WylFBs4QzGKYdWKsOW2tEie6L7hn7MyLkEdoOh9MYsaZeiMQgZz1FtA2ZIjNaHvZDx60CQQDHX5xhkYxA2YVSe9B1uXseXKUeId0Juu8T5JoJx9JrLy82I2jXrtoV6HY4Gu+ET9+a8HD6fO5tyPaC8e63Btha";

		String enstr2 = "EbXV3RBdF+tsurjOVdEt6guHxtIQKNlcniWl+hIkR6r/baQUVXblXDQwApy+iQKAnZJjaOzrS8Ntnhc7VuQKkg==";
		String str = "Milwaukee and Wisconsin news, sports, business.";
//		byte[] enbytes= RSAUtil.encryptByPublicKey(str, publicKey);
		String enstr = RSAUtil.encrypt2StringByPublicKey(str, DEFAULT_PUBLICKEY_1024);
		String destr = RSAUtil.decrypt2StringByPrivateKey(enstr, DEFAULT_PRIVATEKEY_1024);
		
		System.err.println("原始数据：" + str);
		System.err.println("加密数据：" + enstr);
		System.err.println("解密数据：" + destr);

		enstr = RSAUtil.encrypt2StringByPublicKey(str, DEFAULT_PUBLICKEY_512);
		destr = RSAUtil.decrypt2StringByPrivateKey(enstr2);
		
		System.err.println("加密数据：" + enstr);
		System.err.println("解密数据：" + destr);
		
		String inputStr = "hello world;";
		
//		byte[] encodedData = RSAUtil.encryptByPublicKey(inputStr, publicKey);
//		System.err.println("加密数据：" + encryptBASE64(encodedData));
//		byte[] encodedData2 = RSAUtil.encryptByPublicKey(inputStr, publicKey);
//		System.err.println("加密数据：" + encryptBASE64(encodedData2));
//		
//		byte[] decodedData = RSAUtil.decryptByPrivateKey(encodedData2,privateKey);
//		String outputStr = new String(decodedData);
//		System.err.println("加密前: " + inputStr);
//		System.err.println("解密后: " + outputStr);
//
//		System.err.println("私钥签名——公钥验证签名");
//		// 产生签名
//		String sign = RSAUtil.sign(encodedData, privateKey);
//		System.err.println("签名:" + sign);
//		// 验证签名
//		boolean status = RSAUtil.verify(encodedData, publicKey, sign);
//		System.err.println("状态:" + status);
	}


	public static String decrypt2StringByPrivateKey(String data) {
		String ret = null;
		try {
			ret = decrypt2StringByPrivateKey(data, DEFAULT_PRIVATEKEY_512);
		} catch (Exception e){
			LogManager.getLogger().error("",e);
		}
		return ret;
	}
	
	public synchronized static String decrypt2StringByPrivateKey(String data, String key) 
			throws NoSuchAlgorithmException, InvalidKeySpecException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
		byte[] keyBytes = decryptBASE64(key);
		PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(keyBytes);
		KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
		Key privateKey = keyFactory.generatePrivate(pkcs8KeySpec);
		Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
		cipher.init(Cipher.DECRYPT_MODE, privateKey);
		return new String(cipher.doFinal(decryptBASE64(data)));
	}
	
	public synchronized static String encrypt2StringByPublicKey(String data, String key) 
				throws Exception {
		byte[] keyBytes = decryptBASE64(key);
		X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(keyBytes);
		KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
		Key publicKey = keyFactory.generatePublic(x509KeySpec);
		Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
		cipher.init(Cipher.ENCRYPT_MODE, publicKey);
		return encryptBASE64(cipher.doFinal(data.getBytes()));
	}
	
	
//************************************************************************************************************************

	public static byte[] decryptByPrivateKey(byte[] data, String key)
			throws Exception {
		// 对密钥解密
		byte[] keyBytes = decryptBASE64(key);
		// 取得私钥
		PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(keyBytes);
		KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
		Key privateKey = keyFactory.generatePrivate(pkcs8KeySpec);
		// 对数据解密
		Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
		cipher.init(Cipher.DECRYPT_MODE, privateKey);
		return cipher.doFinal(data);
	}

	/**
	 * 解密<br>
	 * 用私钥解密
	 * 
	 * @param data
	 * @param key
	 * @return
	 * @throws Exception
	 */
	public static byte[] decryptByPrivateKey(String data, String key)
			throws Exception {
		return decryptByPrivateKey(decryptBASE64(data), key);
	}

	/**
	 * 解密<br>
	 * 用公钥解密
	 * 
	 * @param data
	 * @param key
	 * @return
	 * @throws Exception
	 */
	public static byte[] decryptByPublicKey(byte[] data, String key)
			throws Exception {
		// 对密钥解密
		byte[] keyBytes = decryptBASE64(key);
		// 取得公钥
		X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(keyBytes);
		KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
		Key publicKey = keyFactory.generatePublic(x509KeySpec);
		// 对数据解密
		Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
		cipher.init(Cipher.DECRYPT_MODE, publicKey);
		return cipher.doFinal(data);
	}

	/**
	 * 加密<br>
	 * 用公钥加密
	 * 
	 * @param data
	 * @param key
	 * @return
	 * @throws Exception
	 */
	public static byte[] encryptByPublicKey(String data, String key)
			throws Exception {
		// 对公钥解密
		byte[] keyBytes = decryptBASE64(key);
		// 取得公钥
		X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(keyBytes);
		KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
		Key publicKey = keyFactory.generatePublic(x509KeySpec);
		// 对数据加密
		Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
		cipher.init(Cipher.ENCRYPT_MODE, publicKey);
		return cipher.doFinal(data.getBytes());
	}
	/**
	 * 加密<br>
	 * 用私钥加密
	 * 
	 * @param data
	 * @param key
	 * @return
	 * @throws Exception
	 */
	public static byte[] encryptByPrivateKey(byte[] data, String key)
			throws Exception {
		// 对密钥解密
		byte[] keyBytes = decryptBASE64(key);
		// 取得私钥
		PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(keyBytes);
		KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
		Key privateKey = keyFactory.generatePrivate(pkcs8KeySpec);
		// 对数据加密
		Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
		cipher.init(Cipher.ENCRYPT_MODE, privateKey);
		return cipher.doFinal(data);
	}

	
	
	public static byte[] decryptBASE64(String key) {
		return Base64.decodeBase64(key);
	}

	public static String encryptBASE64(byte[] bytes) {
		return Base64.encodeBase64String(bytes);
	}

	/**
	 * 用私钥对信息生成数字签名
	 * 
	 * @param data
	 *            加密数据
	 * @param privateKey
	 *            私钥
	 * @return
	 * @throws Exception
	 */
	public static String sign(byte[] data, String privateKey) throws Exception {
		// 解密由base64编码的私钥
		byte[] keyBytes = decryptBASE64(privateKey);
		// 构造PKCS8EncodedKeySpec对象
		PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(keyBytes);
		// KEY_ALGORITHM 指定的加密算法
		KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
		// 取私钥匙对象
		PrivateKey priKey = keyFactory.generatePrivate(pkcs8KeySpec);
		// 用私钥对信息生成数字签名
		Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);
		signature.initSign(priKey);
		signature.update(data);
		return encryptBASE64(signature.sign());
	}

	/**
	 * 校验数字签名
	 * 
	 * @param data
	 *            加密数据
	 * @param publicKey
	 *            公钥
	 * @param sign
	 *            数字签名
	 * @return 校验成功返回true 失败返回false
	 * @throws Exception
	 */
	public static boolean verify(byte[] data, String publicKey, String sign)
			throws Exception {
		// 解密由base64编码的公钥
		byte[] keyBytes = decryptBASE64(publicKey);
		// 构造X509EncodedKeySpec对象
		X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
		// KEY_ALGORITHM 指定的加密算法
		KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
		// 取公钥匙对象
		PublicKey pubKey = keyFactory.generatePublic(keySpec);
		Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);
		signature.initVerify(pubKey);
		signature.update(data);
		// 验证签名是否正常
		return signature.verify(decryptBASE64(sign));
	}
	/**
	 * 取得私钥
	 * 
	 * @param keyMap
	 * @return
	 * @throws Exception
	 */
	public static String getPrivateKey(Map<String, Key> keyMap)
			throws Exception {
		Key key = (Key) keyMap.get(PRIVATE_KEY);
		return encryptBASE64(key.getEncoded());
	}

	/**
	 * 取得公钥
	 * 
	 * @param keyMap
	 * @return
	 * @throws Exception
	 */
	public static String getPublicKey(Map<String, Key> keyMap) throws Exception {
		Key key = keyMap.get(PUBLIC_KEY);
		return encryptBASE64(key.getEncoded());
	}

	/**
	 * 初始化密钥
	 * 
	 * @return
	 * @throws Exception
	 */
	public static Map<String, Key> initKey() throws Exception {
		KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance(KEY_ALGORITHM);
		keyPairGen.initialize(1024);
		KeyPair keyPair = keyPairGen.generateKeyPair();
		Map<String, Key> keyMap = new HashMap(2);
		keyMap.put(PUBLIC_KEY, keyPair.getPublic());// 公钥
		keyMap.put(PRIVATE_KEY, keyPair.getPrivate());// 私钥
		
		System.err.println("publickey:"+getPublicKey(keyMap));
		System.err.println("private:"+getPrivateKey(keyMap));
		return keyMap;
	}

}