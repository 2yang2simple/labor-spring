package com.labor.common.util;

public class StringUtil {

	public static boolean isNumeric(String str) {
		for (int i = 0; i < str.length(); i++) {
			if (!Character.isDigit(str.charAt(i))) {
				return false;
			}
		}
		return true;
	}

	public static String substring(String str, String chr, int index) {
		String s[] = str.split(chr);
		int len = s.length;
		if (len == 0) {
			return "";
		}
		if (index > len) {
			index = len;
		}
		return s[index - 1];
	}

	public static String string2Json(String s) {
		if (s == null)
			return "";
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < s.length(); i++) {
			char ch = s.charAt(i);
			switch (ch) {
			case '"':
				sb.append("\\\"");
				break;
			case '\\':
				sb.append("\\\\");
				break;
			case '\b':
				sb.append("\\b");
				break;
			case '\f':
				sb.append("\\f");
				break;
			case '\n':
				sb.append("\\n");
				break;
			case '\r':
				sb.append("\\r");
				break;
			case '\t':
				sb.append("\\t");
				break;
			case '/':
				sb.append("\\/");
				break;
			default:
				if (ch >= '\u0000' && ch <= '\u001F') {
					String ss = Integer.toHexString(ch);
					sb.append("\\u");
					for (int k = 0; k < 4 - ss.length(); k++) {
						sb.append('0');
					}
					sb.append(ss.toUpperCase());
				} else {
					sb.append(ch);
				}
			}
		}
		return sb.toString();
	}

	public static String escape(String dangerous) {

		// that already contain &amp; and such. respect those?

		if (dangerous == null)
			return "";
		if (dangerous.indexOf("&") == -1 && dangerous.indexOf("\"") == -1 && dangerous.indexOf("'") == -1
				&& dangerous.indexOf("<") == -1 && dangerous.indexOf(">") == -1) {
			return dangerous;
		} else {
			dangerous = dangerous.replaceAll("&", "&amp;");
			dangerous = dangerous.replaceAll("\"", "&quot;");
			dangerous = dangerous.replaceAll("'", "&apos;");
			dangerous = dangerous.replaceAll("<", "&lt;");
			dangerous = dangerous.replaceAll(">", "&gt;");
			return dangerous;
		}
	}

	public static String defaultString(String str, String defaults) {
		return (str == null ? defaults : str);
	}

	public static int defaultInt(String str, int defaults) {
		if (str == null)
			return defaults;
		try {
			return Integer.parseInt(str);
		} catch (Exception e) {
			return defaults;
		}
	}

	public static String safeToString(Object s) {
		if (null == s) {
			return "";
		} else {
			return s.toString();
		}
	}

	public static String safeSqlPara(String para) {
		String ret = "";
		ret = para.replace("'", "''");
		return ret;

	}

	public static boolean isEmpty(String s) {
		boolean ret = false;
		if (s == null || "".equals(s.trim())) {
			ret = true;
		}
		return ret;
	}

	public static String upper(String s) {
		String ret = "";
		if (s == null) {
			return ret;
		} else {
			ret = s.trim().toUpperCase();
		}
		return ret;
	}

	public static String lower(String s) {
		String ret = "";
		if (s == null) {
			return ret;
		} else {
			ret = s.trim().toLowerCase();
		}
		return ret;
	}

	public static boolean isEqualedTrimLower(String a, String b) {
		boolean ret = false;
		if (a == null & b == null) {
			ret = true;
		} else if (a != null && b != null) {
			ret = a.trim().toLowerCase().equals(b.trim().toLowerCase());
		}
		return ret;
	}
	
	public static String prefixZero(String str, int length) {
		String ret = str;
		if(!StringUtil.isEmpty(ret)){
			int len = str.length();
			for (int i = 0; i < length - len; i++) {
				ret = "0" + ret;
	        }
		}
		return ret;
	}
	
	public static String postfix(String str, String postfix, int length) {
		String ret = str;
		if(!StringUtil.isEmpty(ret)){
			int len = str.length();
			for (int i = 0; i < length - len; i++) {
				ret = ret + postfix;
	        }
		}
		return ret;
	}
}
