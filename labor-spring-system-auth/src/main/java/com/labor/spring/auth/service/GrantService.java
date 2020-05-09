package com.labor.spring.auth.service;

import java.util.Set;


/***
 * grant permissions
 * @author Administrator
 *
 */
public interface GrantService {
	
	//grant permissions to the user;
	public Set<String> create(String authValue,Integer Userid, Set<String> permissions);
	//find user permissions by token
	public Set<String> find(String authValue);
	//delete permissions from the user
	public void delete(String authValue);
    


}
