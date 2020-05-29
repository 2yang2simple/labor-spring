package com.labor.spring.core.controller.local;

import java.util.HashMap;
import java.util.List;

import javax.validation.Valid;

import org.apache.logging.log4j.LogManager;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.labor.common.constants.CommonConstants;
import com.labor.common.exception.ServiceException;
import com.labor.common.util.GoogleAuthenticator;
import com.labor.common.util.QRCodeUtil;
import com.labor.common.util.StringUtil;
import com.labor.spring.base.BaseRestController;
import com.labor.spring.bean.Result;
import com.labor.spring.bean.ResultStatus;
import com.labor.spring.core.entity.Role;
import com.labor.spring.core.entity.RolePermission;
import com.labor.spring.core.entity.User;
import com.labor.spring.core.entity.UserRole;
import com.labor.spring.core.service.PermissionServiceIntf;
import com.labor.spring.core.service.RoleServiceIntf;
import com.labor.spring.core.service.SysconfigConstants;
import com.labor.spring.core.service.SysconfigServiceIntf;
import com.labor.spring.core.service.UserServiceImpl;
import com.labor.spring.feign.auth.AuthLoginService;
import com.labor.spring.util.WebUtil;

@RestController
@RequestMapping("/rest/core/users")
public class UserRestController extends BaseRestController {
	
	@Autowired
	private UserServiceImpl userService;
	@Autowired
	private RoleServiceIntf roleService;
	@Autowired
	private AuthLoginService authLoginService;

	@RequiresPermissions("user")
	@RequestMapping(value = {"/{id}/roles" }, method = RequestMethod.POST)
	public Result createRoles(
				@PathVariable(value="id") Long id,
				@RequestBody List<UserRole> list) {
		if (id==null||id<=0) {
	    	return Result.failure(ResultStatus.FAILURE_PARAM_INVALID.code(), "the id is null."); 
	    }
		LogManager.getLogger().debug("xxdddddxx:"+list.size());
		return Result.success(userService.createUserRoles(id,list));	
	}
	
	@RequiresPermissions("user")
	@RequestMapping(value = { "/{id}" }, method = RequestMethod.PUT)
	public Result update(
				@PathVariable(value="id") Long id, 
				@RequestBody @Valid User user, 
				BindingResult bindingResult) {

		if (id!=null&&id>0) {
			user.setId(id);
	    } else {
	    	return Result.failure(ResultStatus.FAILURE_PARAM_INVALID.code(), "the id is null."); 
	    }
		if (bindingResult.hasErrors()) {
	        String message = bindingResult.getFieldError().getDefaultMessage();
	        return Result.failure(ResultStatus.FAILURE_PARAM_INVALID.code(),message);
	    }
		String checkmsg = userService.checkExisted(user);
		if (!"none".equals(checkmsg)) {
			return Result.failure(ResultStatus.FAILURE_DATA_NOT_UNIQUE.code(), checkmsg); 
		}
		return Result.success(userService.update(user));
	}
	
	@RequiresPermissions("user")
	@RequestMapping(value = {"/{uuid}/status/{status}"}, method = RequestMethod.PATCH)
	public Result updateStatus(
				@PathVariable(value="uuid") String uuid,
				@PathVariable(value="status") String status) {
		return Result.success(userService.updateStatusByUuid(uuid,status));
	}
	

	/*forget password, create a password modify url by uuid;
	 */
	@RequiresPermissions("user:pwdmodify")
	@RequestMapping(value = {"/{uuid}/pwdmodify"}, method = RequestMethod.PATCH)
	public Result createPwdmodifyByUuid(
				@PathVariable(value="uuid") String uuid) {
		String ret = null;
//		User user = userService.findByUuid(uuid);
		if(authLoginService.isCurrentUserOrSuperUser(null,uuid)) {
			ret =userService.createPasswordModifyCode(uuid);
		}
		return Result.success(ret);
	}

	@RequiresPermissions("user")
	@RequestMapping(value = {"/uuid-{uuid}"}, method = RequestMethod.GET)
	public Result findByUuid(
				@PathVariable(value="uuid") String uuid) {
		return Result.success(userService.findByUuid(uuid));
	}

	@RequiresPermissions("user")
	@RequestMapping(value = {"/{id}/roles" }, method = RequestMethod.GET)
	public Result findRoleList(
				@PathVariable(value="id") Long id) {
		return Result.success(roleService.findListByUserid(id));
	}

	@RequiresPermissions("user")
	@RequestMapping(value = {"/page-list"}, method = RequestMethod.GET)
	public Result findList(
				@RequestParam(value="sortby", defaultValue="id") String sortby,
				@RequestParam(value="page", defaultValue="0") Integer page,
				@RequestParam(value="pagesize", defaultValue="0" ) Integer pagesize) {
		Sort sort = new Sort(Sort.Direction.DESC, sortby);
		Pageable pageable = PageRequest.of(page, (pagesize==0)?SysconfigConstants.DEFAULT_PAGE_SIZE:pagesize, sort);
		return Result.success(userService.findList(pageable));
	}
	
	@RequiresPermissions("user")
	@RequestMapping(value = { "/namestart-page-list"}, method = RequestMethod.GET)
	public Result findListByName(
				@RequestParam(value="namestart") String namestart,
				@RequestParam(value="sortby", defaultValue="id") String sortby,
				@RequestParam(value="page", defaultValue="0") Integer page,
				@RequestParam(value="pagesize", defaultValue="0") Integer pagesize) {
		Sort sort = new Sort(Sort.Direction.DESC, sortby);
		Pageable pageable = PageRequest.of(page, (pagesize==0)?SysconfigConstants.DEFAULT_PAGE_SIZE:pagesize, sort);
		//return Result.success(userService.findListByNameStartingWith(namestart,pageable));
		return Result.success(userService.findListByNameOrPhoneLike(namestart,pageable));
		
	}
	
	//@RequiresPermissions("user")
	@RequestMapping(value = { "/{uuid}/qr-code-url"}, method = RequestMethod.GET)
	public Result findQRUrlByUuid(
				@PathVariable(value="uuid") String uuid) {
		User user = userService.findByUuid(uuid);
		//super user or current user can get the QR code url
		if(authLoginService.isCurrentUserOrSuperUser(user.getId(),null)) {
			String sk = userService.findSecretKey(user.getUuid());
			if(!StringUtil.isEmpty(sk)) {
				return Result.success(GoogleAuthenticator.getQRBarcodeURL(user.getSno(), StringUtil.safeToString(baseProperties.CONTEXT_PATH).replace("/", ""), sk));
			}
		}
		return Result.failure(ResultStatus.FAILURE_DATA_NOT_FOUND);
	}
	

	@RequiresPermissions("user:create")
	@RequestMapping(value = { ""}, method = RequestMethod.POST)
	public Result create(
				@RequestBody HashMap<String, String> hm) {
		User ret = null;
		String phone = (String)hm.get("phone");
		String name = (String)hm.get("name");
		if (!StringUtil.isEmpty(phone)&&StringUtil.isNumeric(phone)) {
			User user = new User();
			user.setName(name);
			user.setCellPhone(phone);
			String checkmsg = userService.checkExisted(user);
			if (!"none".equals(checkmsg)) {
				return Result.failure(ResultStatus.FAILURE_DATA_NOT_UNIQUE.code(), checkmsg); 
			}
			
			ret = userService.create(name, null, null, phone, null, null, null, null, CommonConstants.ACTIVE);
			if (ret!=null) {
				userService.createPasswordModifyCode(ret.getUuid());
			}
		}
		return Result.success(ret);
	}

}
