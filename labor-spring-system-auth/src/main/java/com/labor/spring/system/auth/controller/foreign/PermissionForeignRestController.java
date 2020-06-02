package com.labor.spring.system.auth.controller.foreign;

import java.util.Set;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.labor.common.util.StringUtil;
import com.labor.spring.base.BaseRestController;
import com.labor.spring.bean.ClientRegisted;
import com.labor.spring.bean.Result;
import com.labor.spring.bean.ResultStatus;
import com.labor.spring.core.entity.User;
import com.labor.spring.core.service.PermissionServiceIntf;
import com.labor.spring.core.service.SysconfigServiceIntf;
import com.labor.spring.core.service.UserServiceIntf;
import com.labor.spring.system.auth.service.AuthService;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/rest/foreign/permissions")
public class PermissionForeignRestController extends BaseRestController{
	@Autowired
	private PermissionServiceIntf perService;
	@Autowired
	private UserServiceIntf userService;
	@Autowired
	private AuthService authService;
//	@Autowired
//	private SysconfigServiceIntf sysconfigService;
//	
//	private String PREFIX_PERMISSION_TYPE = "PER_TYPE_";
	
	@ApiOperation("refresh permissions by type")
	@ApiImplicitParams({
	@ApiImplicitParam(name="client-key",value="client name",dataType="String", paramType = "query"),
	@ApiImplicitParam(name="permissions",value="permissions list",dataType="String", paramType = "query")})
	@RequestMapping(value = { "/{client-key}" }, method = RequestMethod.PUT)
	public Result refreshPermissions(
				@PathVariable(value="client-key") String clientKey, 
				@RequestBody Set<String> permissions) {
		
		if (StringUtil.isEmpty(clientKey)) {
			return Result.failure(ResultStatus.FAILURE_PARAM_NULL.code(),"client key is null");
		}
		
//		String perType = sysconfigService.findValueByKey(PREFIX_PERMISSION_TYPE+type);
		String perType = ClientRegisted.getName(clientKey);
		if (StringUtil.isEmpty(perType)) {
			return Result.failure(ResultStatus.FAILURE_PARAM_NULL.code(),"permission type not exist");
		}
		
		if (permissions==null||permissions.size()==0) {
			return Result.failure(ResultStatus.FAILURE_PARAM_NULL.code(),"permissions is null");
		}
		
		perService.refreshByType(permissions, perType);
		
		return Result.success();
	}
	
	@ApiOperation("fetch user permissions")
	@ApiImplicitParams({
	@ApiImplicitParam(name="client-key",value="client name",dataType="String", paramType = "query"),
	@ApiImplicitParam(name="uuid",value="user uuid",dataType="String", paramType = "query")})
	@RequestMapping(value = { "/{client-key}/{uuid}" }, method = RequestMethod.GET)
	public Result fetchUserPermissions(
				@PathVariable(value="client-key") String clientKey, 
				@PathVariable(value="uuid") String uuid) {
		
		if (StringUtil.isEmpty(clientKey)) {
			return Result.failure(ResultStatus.FAILURE_PARAM_NULL.code(),"client key is null");
		}
		
//		String perType = sysconfigService.findValueByKey(PREFIX_PERMISSION_TYPE+type);
		String perType = ClientRegisted.getName(clientKey);
		if (StringUtil.isEmpty(perType)) {
			return Result.failure(ResultStatus.FAILURE_PARAM_NULL.code(),"permission type not exist");
		}
		
		if (StringUtil.isEmpty(uuid)) {
			return Result.failure(ResultStatus.FAILURE_PARAM_NULL.code(),"uuid is null");
		}
		User user = userService.findByUuid(uuid);
		if (user==null) {
			return Result.failure(ResultStatus.FAILURE_PARAM_NULL.code(),"user not exist");
		}
		
		Set<String> permissions  = authService.findUserPermissions(user.getId(), user.getName(), perType);
		
		return Result.success(permissions);
	}
	
	
}
