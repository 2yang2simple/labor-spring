package com.labor.spring.feign.client.auth;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.labor.spring.bean.Result;

/***
 * get url from application.porperties
 * feign.url.auth=http://localhost:8080/auth
 */
@FeignClient(name = "auth", url = "${feign.url.auth}")
public interface AuthFeignClient {

	@RequestMapping(value = { "/rest/foreign/logins/users" }, method = RequestMethod.GET)
  	public Result fetchLoginUser(
  			@RequestParam(value = "code", required = true) String code);
	
	@RequestMapping(value = { "/rest/foreign/logins/users/tokens" }, method = RequestMethod.POST)
	public Result fetchLoginUserToken(
			@RequestParam(value = "type", required = true) String type,
			@RequestParam(value = "code", required = true) String code);

	@RequestMapping(value = { "/rest/foreign/logins" }, method = RequestMethod.DELETE)
	public void deleteLogin(
			@RequestParam(value = "code", required = true) String code);
	
	@RequestMapping(value = { "/rest/foreign/logins" }, method = RequestMethod.POST)
	public Result createLogin(
			@RequestParam(value = "client-key", required = true)String clientKey,
			@RequestParam(value = "client-uuid", required = true)String clientUuid,
			@RequestParam(value = "type", required = true)String type,
			@RequestParam(value = "code", required = true)String code,
			@RequestParam(value = "name", required = true)String name);
}
