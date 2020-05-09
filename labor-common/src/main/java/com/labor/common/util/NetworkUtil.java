package com.labor.common.util;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;

public class NetworkUtil {

	public static String getLocalHostIP(){
		String ret = "";
		Enumeration netInterfaces = null;  
		try {  
		     netInterfaces = NetworkInterface.getNetworkInterfaces();  
		     while (netInterfaces.hasMoreElements()) {  
		         NetworkInterface ni = (NetworkInterface)netInterfaces.nextElement();  
//		         System.out.println("DisplayName:" + ni.getDisplayName());  
//		         System.out.println("Name:" + ni.getName());  
		         Enumeration ips = ni.getInetAddresses();
		         
		         while (ips.hasMoreElements()) {  
		             System.out.println("IP:"  
		             + ((InetAddress)ips.nextElement()).getHostAddress());  
		         }  
		     }  
		} catch (Exception e) {  
			e.printStackTrace();  
		} 
		
		return ret;
				
	}
	
	public static void main(String[] args){
		System.out.println("1111"); 
		NetworkUtil.getLocalHostIP();
		System.out.println("2222");
	}
		
}
