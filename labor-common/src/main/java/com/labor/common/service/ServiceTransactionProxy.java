package com.labor.common.service;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import org.apache.logging.log4j.LogManager;

/**
 * 
 * @author yang yang1
 *
 */
public class ServiceTransactionProxy implements InvocationHandler {
	/**
	 * the object to delegate
	 */
	private Object delegate;

	/**
	 * to bind the delegate object
	 */
	public Object bind(Object delegate) {
		this.delegate = delegate;
		return Proxy.newProxyInstance(this.delegate.getClass().getClassLoader(),
				this.delegate.getClass().getInterfaces(), this);
	}

	/**
	 * JVM execute the method through this
	 */
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		Object result = null;
		// get the method of the delegate
		Class clazz = this.delegate.getClass();
		Method open = clazz.getMethod("open", null);
		Method start = clazz.getMethod("startTransaction", null);
		Method commit = clazz.getMethod("commitTransaction", null);
		Method rollback = clazz.getMethod("rollbackTransaction", null);
		Method close = clazz.getMethod("closeTransaction", null);
		boolean needTransaction = false;

		LogManager.getLogger().info("needTransaction?"+method.getName());
		if (method.getName()!=null) {
			if(method.getName().toLowerCase().indexOf("update")>=0
					||method.getName().toLowerCase().indexOf("create")>=0) {
				needTransaction = true;
				System.out.println("needTransaction");
				LogManager.getLogger().info("needTransaction:");
			}
		}
		try {

			// open session
			open.invoke(this.delegate, null);
			// start transaction
			if(needTransaction) {
				start.invoke(this.delegate, null);
			}
			// JVM execute the method through this
			result = method.invoke(this.delegate, args);
			// commit transaction
			if(needTransaction) {
				commit.invoke(this.delegate, null);
			}
		} catch (InvocationTargetException ite) {
			if(needTransaction) {
				rollback.invoke(this.delegate, null);
			}
			LogManager.getLogger().error("ROLLBACK:",ite);
			throw ite.getTargetException();
		} catch (Throwable e) {
			if(needTransaction) {
				rollback.invoke(this.delegate, null);
			}
			LogManager.getLogger().error("ROLLBACK:",e);
			throw e;
		} finally {
			// close transaction
			close.invoke(this.delegate, null);
		}
		return result;
	}
}
