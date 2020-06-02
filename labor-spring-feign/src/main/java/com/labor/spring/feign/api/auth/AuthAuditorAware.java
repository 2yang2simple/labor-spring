package com.labor.spring.feign.api.auth;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.stereotype.Component;

import com.labor.spring.feign.api.auth.AuthLoginService;

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
		UserVO lc = authLoginService.getLoginCacheCurrent();
		if (lc!=null) {
			ret = String.valueOf(lc.getUserId());
		}
		return Optional.of(ret);
	}
 
}
