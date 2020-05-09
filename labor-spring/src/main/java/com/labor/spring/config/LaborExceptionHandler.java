package com.labor.spring.config;

import org.apache.logging.log4j.LogManager;
//import org.apache.shiro.ShiroException;
//import org.apache.shiro.authc.IncorrectCredentialsException;
//import org.apache.shiro.authc.LockedAccountException;
//import org.apache.shiro.authc.UnknownAccountException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.thymeleaf.exceptions.TemplateInputException;

import com.labor.common.exception.ParameterException;
import com.labor.common.exception.PermissionException;
import com.labor.common.exception.ServiceException;
import com.labor.spring.bean.Result;
import com.labor.spring.bean.ResultStatus;

@ControllerAdvice
public class LaborExceptionHandler {

	@ExceptionHandler(RuntimeException.class)
	@ResponseBody
	public Result doHandleRuntimeException(RuntimeException e) {
		LogManager.getLogger().error("",e);
		return Result.failure(ResultStatus.FAILURE_EXCEPTION_RUNTIME.code(), e.toString());
	}

//	@ExceptionHandler(ShiroException.class)
//	@ResponseBody
//	public Result doHandleShiroException(ShiroException e) {
//		String msg = "";
//		if (e instanceof IncorrectCredentialsException) {
//			msg = e.getMessage();
//		} else if (e instanceof UnknownAccountException) {
//			msg = e.getMessage();
//		} else if (e instanceof LockedAccountException) {
//			msg = e.getMessage();
//		} else {
//			msg = e.getMessage();
//		}
//		LogManager.getLogger().error("",e);
//		return  Result.failure(ResultStatus.FAILURE_EXCEPTION_SHIRO.code(), e.toString());
//
//	}
	@ExceptionHandler(ServiceException.class)
	@ResponseBody
	public Result doHandleServiceException(ServiceException e) {
		LogManager.getLogger().error("",e);
		return  Result.failure(ResultStatus.FAILURE_EXCEPTION_SEVICE.code(),e.getMessage());

	}
	@ExceptionHandler(ParameterException.class)
	@ResponseBody
	public Result doHandleParameterException(ParameterException e) {
		LogManager.getLogger().error("",e);
		return  Result.failure(ResultStatus.FAILURE_PARAM_INVALID.code(),e.getMessage());

	}
	@ExceptionHandler(PermissionException.class)
	@ResponseBody
	public Result doHandlePermissionException(PermissionException e) {
		LogManager.getLogger().error("",e);
		return  Result.failure(ResultStatus.FAILURE_PERMISSION_NOACCESS);

	}
	@ExceptionHandler(TemplateInputException.class)
	@ResponseBody
	public Result doHandleThymeleafException(TemplateInputException e) {
		LogManager.getLogger().error("",e);
		return  Result.failure(ResultStatus.FAILURE_EXCEPTION_TEMPLATE.code(), e.toString());

	}
	
}
