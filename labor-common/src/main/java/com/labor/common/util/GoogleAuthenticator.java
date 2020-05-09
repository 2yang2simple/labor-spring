package com.labor.common.util;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base32;
import org.apache.commons.codec.binary.Base64;
import org.apache.logging.log4j.LogManager;

/***
 * if SecureRandom is very slow on high concurrency
 * 
 * vi
 * jvm_home\jre\lib\security\java.security
 * securerandom.source=file:/dev/urandom
 * 
 * or add to tomcat/catalina.sh
 * -Djava.security.egd=file:/dev/./urandom
 * java -Djava.security.egd=file:/dev/./urandom
 * 
 * @author Administrator
 *
 */
public class GoogleAuthenticator {

	// taken from Google pam docs - we probably don't need to mess with these
	private static final int SECRET_SIZE = 10;

	private static final String SEED = "42735b77c3d043dd979e816efb20491e82de77749216";

	private static final String SALT = "a91237af221342b98ffb087ccd9efb69";

	private static final String RANDOM_NUMBER_ALGORITHM = "SHA1PRNG";

	private static final int WINDOW_SIZE = 3; // default 3 - max 17 (from google docs)最多可偏移的时间
	
	public static void main(String[] args) {
//		String sk = generateSecretKey();
//		String saltsk = saltSecretKey(sk);
//		
//		System.err.println(sk);
//		System.err.println(saltsk);
//		System.err.println(validateSecretKey(sk,saltsk));
//		
//		//IBYACA3CFYNKER3L 5O6BZC2GT6UYW4IC
//		//TEQH45GMINEZX5DA
//		System.err.println(getQRBarcodeURL("test","test",sk));
//		System.err.println(validateCode("679705", "5O6BZC2GT6UYW4IC"));
		

		String secretKey = GoogleAuthenticator.generateSecretKey() ;
		String saltkey = GoogleAuthenticator.saltSecretKey(""+secretKey);
		String qrcodeurl = GoogleAuthenticator.getQRBarcodeURL("", "", secretKey);
		System.err.println(secretKey);
		System.err.println(saltkey);
		System.err.println(qrcodeurl);
	}

	public static String saltSecretKey(String secretKey) {
		LogManager.getLogger().debug("xxxxxxxxxxsaltSecretKey");
		String ret = md5(SALT+secretKey);
		LogManager.getLogger().debug("xxxxxxxxxxsaltSecretKey end");
		return ret;
	}
	
	public synchronized static String generateSecretKey() {
		LogManager.getLogger().debug("xxxxxxxxxxxxgenerateSecretKey");
		SecureRandom sr = null;
		try {
			LogManager.getLogger().debug("xxxxxxxxxxxxgenerateSecretKey111");
			sr = SecureRandom.getInstance(RANDOM_NUMBER_ALGORITHM);
			LogManager.getLogger().debug("xxxxxxxxxxxxgenerateSecretKey2222");
			sr.setSeed(Base64.decodeBase64(SEED));
			LogManager.getLogger().debug("xxxxxxxxxxxxgenerateSecretKey333");
			byte[] buffer = sr.generateSeed(SECRET_SIZE);
			LogManager.getLogger().debug("xxxxxxxxxxxxgenerateSecretKey444");
			Base32 codec = new Base32();
			LogManager.getLogger().debug("xxxxxxxxxxxxgenerateSecretKey555");
			byte[] bEncodedKey = codec.encode(buffer);
			LogManager.getLogger().debug("xxxxxxxxxxxxgenerateSecretKey666");
			String encodedKey = new String(bEncodedKey);
			LogManager.getLogger().debug("xxxxxxxxxxxxgenerateSecretKey end");
			return encodedKey;
		} catch (NoSuchAlgorithmException e) {
			// should never occur... configuration error
		}
		return null;
	}

	public static String getQRBarcodeURL(String user, String host, String secretKey) {
		LogManager.getLogger().debug("xxxxxxxxxxxgetQRBarcodeURL");
		//otpauth://totp/kisexu@gmail.com?secret=DPI45HCEBCJK6HG7
		//otpauth://totp/test@test?IBYACA3CFYNKER3L
//		String format = "https://www.google.com/chart?chs=200x200&chld=M%%7C0&cht=qr&chl=otpauth://totp/%s@%s%%3Fsecret%%3D%s";
		if (host == null || "".equals(host.trim())) {
			host = "labor";
		}
		String format = "otpauth://totp/%s@%s?secret=%s";
		String ret = String.format(format, user, host, secretKey);
		LogManager.getLogger().debug("xxxxxxxxxxxxxxgetQRBarcodeURL end");
		return ret;
	}
	
	public static Boolean validateSecretKey(String secretKey, String saltSecretKey) {
		String theRightSaltSecretKey = md5(SALT+secretKey);
		return (theRightSaltSecretKey.equals(saltSecretKey));
		
	}
	
	public static Boolean validateCode(String codes, String secretKey) {
		// enter the code shown on device. Edit this and run it fast before the
		// code expires!
		long code = 0;
		try {
			code = Long.parseLong(codes);
		} catch (NumberFormatException e) {
			return false;
		}
		long t = System.currentTimeMillis();
		return validateCode(code, secretKey, t);
	}

	private static boolean validateCode(long code, String secretKey, long timeMsec) {
		Base32 codec = new Base32();
		byte[] decodedKey = codec.decode(secretKey);
		// convert unix msec time into a 30 second "window"
		// this is per the TOTP spec (see the RFC for details)
		long t = (timeMsec / 1000L) / 30L;
		// Window is used to check codes generated in the near past.
		// You can use this value to tune how far you're willing to go.
		for (int i = -WINDOW_SIZE; i <= WINDOW_SIZE; ++i) {
			long hash;
			try {
				hash = verifyCode(decodedKey, t + i);
			} catch (Exception e) {
				// Yes, this is bad form - but
				// the exceptions thrown would be rare and a static configuration problem
				e.printStackTrace();
				throw new RuntimeException(e.getMessage());
				// return false;
			}
			if (hash == code) {
				return true;
			}
		}
		// The validation code is invalid.
		return false;
	}

	private static int verifyCode(byte[] key, long t) throws NoSuchAlgorithmException, InvalidKeyException {
		byte[] data = new byte[8];
		long value = t;
		for (int i = 8; i-- > 0; value >>>= 8) {
			data[i] = (byte) value;
		}
		SecretKeySpec signKey = new SecretKeySpec(key, "HmacSHA1");
		Mac mac = Mac.getInstance("HmacSHA1");
		mac.init(signKey);
		byte[] hash = mac.doFinal(data);
		int offset = hash[20 - 1] & 0xF;
		// We're using a long because Java hasn't got unsigned int.
		long truncatedHash = 0;
		for (int i = 0; i < 4; ++i) {
			truncatedHash <<= 8;
			// We are dealing with signed bytes:
			// we just keep the first byte.
			truncatedHash |= (hash[offset + i] & 0xFF);
		}
		truncatedHash &= 0x7FFFFFFF;
		truncatedHash %= 1000000;
		return (int) truncatedHash;
	}

    private static String md5(String text) {
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
}