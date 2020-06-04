package com.labor.spring.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import com.labor.common.util.StringUtil;
import com.labor.common.util.TokenUtil;
import com.labor.spring.base.AbstractInterceptor;
import com.labor.spring.bean.ClientRegisted;
import com.labor.spring.constants.WebConstants;
import com.labor.spring.util.WebUtil;

public class TimestampTokenInterceptor extends AbstractInterceptor{
	
	public static void main(String[] args) {
		String tk = "20200604231402.bc5087931d0e0e10abdc735c65658b25";
    	
		String[] str = tk.split("\\.");
		
		System.err.println(str.length);
		System.err.println(TokenUtil.isValidatedDateSaltingToken(tk,"de07c085bfe741caaef26e7b4adf0096"));
	}

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		String redirecturl =  request.getContextPath() + "/public/noprivilege/0";
		
		String timestamp = WebUtil.getRequest(WebConstants.KEY_TIMESTAMP);
		String timestamptoken = WebUtil.getRequest(WebConstants.KEY_TIMESTAMPTOKEN);
		
		String token = ClientRegisted.getSecret(ClientRegisted.CLIENTKEY_DEFAULT);
		
		LogManager.getLogger().debug("|timestamp:"+timestamp+"|timestamptoken:"+timestamptoken);
		
		boolean validated = false;
		if (StringUtil.isEmpty(timestamp)) {
			validated = TokenUtil.isValidatedDateSaltingToken(timestamptoken,token);
		} else {
			validated = TokenUtil.isValidatedDateSaltingToken(timestamp,timestamptoken,token);
		}
		//old 
		if (!validated) {
			String g = request.getParameter("g");
			String t = request.getParameter("t");
			LogManager.getLogger().debug("g:"+g +"|t:"+t);
			if (!TokenUtil.isValidatedDateSaltingToken(g,t,token)) {
				redirect(request,response,redirecturl);
				return false;
			}
		}
		return true;
	}
	
	
}
