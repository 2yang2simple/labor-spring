package com.labor.base.acl;

import java.util.ArrayList;
import java.util.List;

import com.labor.common.util.StringUtil;

public class AclConstants {
	
	public static final String SUPER_USER_SNO = "3P00000";
	public static final String ROLE_ADMIN = "ADMIN";
	public static final String ROLE_OPERATOR = "OPERATOR";
	public static final String ROLE_OPERATORLEAD = "OPERATOR-LEADER";
	public static final String ROLE_TECHNICIAN = "TECHNICIAN";
	
	private static List ACTIONLIST_DEFAULT = new ArrayList();
	private static List ACTIONLIST_ADMIN = new ArrayList();
	private static List ACTIONLIST_OPERATOR = new ArrayList();
	private static List ACTIONLIST_OPERATORLEAD = new ArrayList();
	private static List ACTIONLIST_TECHNICIAN = new ArrayList();
	
	static {
		
		//the action everybody can do.
		ACTIONLIST_DEFAULT.add("welcome");
		ACTIONLIST_DEFAULT.add("public");
		ACTIONLIST_DEFAULT.add("login");
		
		//the action admin can do.
		ACTIONLIST_ADMIN.add("user");
		ACTIONLIST_ADMIN.add("useredit");
		ACTIONLIST_ADMIN.add("role");
//		ACTIONLIST_ADMIN.add("ddv");
//		ACTIONLIST_ADMIN.add("ddvedit");
//		ACTIONLIST_ADMIN.add("operequip");
//		ACTIONLIST_ADMIN.add("opermtrl");
//		ACTIONLIST_ADMIN.add("operlead");
//		ACTIONLIST_ADMIN.add("tech");
		
		//the action operator can do.
		ACTIONLIST_OPERATOR.add("operequip");
		ACTIONLIST_OPERATOR.add("opermtrl");
		
		//the action operator leader can do.
		ACTIONLIST_OPERATORLEAD.add("operequip");
		ACTIONLIST_OPERATORLEAD.add("opermtrl");
		ACTIONLIST_OPERATORLEAD.add("operlead");
		
		//the action technician can do.
		ACTIONLIST_TECHNICIAN.add("tech");
		
	}
	
	
	public static boolean verifyAction(String rolename, String sno, String action){
		boolean ret = false;
		//super user pass.
		if (sno!=null){
			if (StringUtil.isEqualedTrimLower(sno, SUPER_USER_SNO)){
				return true;
			}
		}
		if (rolename==null){
			return ret;
		} else {
			rolename = rolename.toUpperCase();
		}
		if (ACTIONLIST_DEFAULT.contains(action)){
			return true;
		}
		if (ROLE_ADMIN.equals(rolename)){
			ret = ACTIONLIST_ADMIN.contains(action);
		} else if (ROLE_OPERATOR.equals(rolename)){
			ret = ACTIONLIST_OPERATOR.contains(action);
		} else if (ROLE_OPERATORLEAD.equals(rolename)){
			ret = ACTIONLIST_OPERATORLEAD.contains(action);
		} else if (ROLE_TECHNICIAN.equals(rolename)){
			ret = ACTIONLIST_TECHNICIAN.contains(action);
		}
		return ret;
	}
}
