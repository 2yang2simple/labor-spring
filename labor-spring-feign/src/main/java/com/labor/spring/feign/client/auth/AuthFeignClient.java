package com.labor.spring.feign.client.auth;

import java.util.Set;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
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
	
	@RequestMapping(value = { "/rest/foreign/permissions/{client-key}" }, method = RequestMethod.POST)
	public Result refreshPermissions(
			@PathVariable(value="client-key") String clientKey, 
			@RequestBody Set<String> permissions);
	
	@RequestMapping(value = { "/rest/foreign/permissions/{client-key}/{uuid}" }, method = RequestMethod.GET)
	public Result findUserPermissions(
			@PathVariable(value="client-key") String clientKey, 
			@PathVariable(value="uuid") String uuid);

}
