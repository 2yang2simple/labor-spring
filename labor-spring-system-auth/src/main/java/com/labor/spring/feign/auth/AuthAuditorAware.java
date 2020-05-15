package com.labor.spring.feign.auth;

import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.labor.common.constants.CommonConstants;
import com.labor.common.util.StringUtil;
import com.labor.spring.bean.LoginCache;
import com.labor.spring.constants.WebConstants;
import com.labor.spring.core.entity.User;
import com.labor.spring.util.WebUtil;

@EnableJpaAuditing
@Component
public class AuthAuditorAware implements AuditorAware<String> {
	@Autowired
	@Lazy
	private AuthLoginService authLoginService;
	/**
	 * current userid from login cache(cache,redis or session), 
	 * save to db createdby and lastupdatedby;
	 */
	@Override
	public Optional<String> getCurrentAuditor() {
		String ret = "";
		LoginCache lc = authLoginService.getLoginCacheCurrent();
		if (lc!=null) {
			ret = String.valueOf(lc.getUserId());
		}
		return Optional.of(ret);
	}
 
}
