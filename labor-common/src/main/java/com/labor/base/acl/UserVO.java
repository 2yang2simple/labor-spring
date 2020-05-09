package com.labor.base.acl;

import java.util.Date;
import java.util.List;

import com.labor.common.pagination.PaginationVO;

public class UserVO extends PaginationVO{
	private String userid;
	private String usersno;
	private String username;
	private String userpwd;
	private String userpwdconfirm;
	private String useremail;
	private String userphone;
	private String usercellphone;
	private String useraddress;
	private String userstatus;
	private String roleid;
	private String rolename;
	private String createby;
	private String updateby;
	private Date createdate;
	private Date updatedate;
	
	private List roleList;
	
	

	public String getUseremail() {
		return useremail;
	}
	public void setUseremail(String useremail) {
		this.useremail = useremail;
	}
	public String getUserphone() {
		return userphone;
	}
	public void setUserphone(String userphone) {
		this.userphone = userphone;
	}
	public String getUsercellphone() {
		return usercellphone;
	}
	public void setUsercellphone(String usercellphone) {
		this.usercellphone = usercellphone;
	}
	public String getUseraddress() {
		return useraddress;
	}
	public void setUseraddress(String useraddress) {
		this.useraddress = useraddress;
	}
	public String getUserpwdconfirm() {
		return userpwdconfirm;
	}
	public void setUserpwdconfirm(String userpwdconfirm) {
		this.userpwdconfirm = userpwdconfirm;
	}
	public String getUserstatus() {
		return userstatus;
	}
	public void setUserstatus(String userstatus) {
		this.userstatus = userstatus;
	}
	public List getRoleList() {
		return roleList;
	}
	public void setRoleList(List roleList) {
		this.roleList = roleList;
	}
	public String getUserid() {
		return userid;
	}
	public void setUserid(String userid) {
		this.userid = userid;
	}
	public String getUsersno() {
		return usersno;
	}
	public void setUsersno(String usersno) {
		this.usersno = usersno;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getUserpwd() {
		return userpwd;
	}
	public void setUserpwd(String userpwd) {
		this.userpwd = userpwd;
	}
	public String getRoleid() {
		return roleid;
	}
	public void setRoleid(String roleid) {
		this.roleid = roleid;
	}
	public String getRolename() {
		return rolename;
	}
	public void setRolename(String rolename) {
		this.rolename = rolename;
	}
	public String getCreateby() {
		return createby;
	}
	public void setCreateby(String createby) {
		this.createby = createby;
	}
	public String getUpdateby() {
		return updateby;
	}
	public void setUpdateby(String updateby) {
		this.updateby = updateby;
	}
	public Date getCreatedate() {
		return createdate;
	}
	public void setCreatedate(Date createdate) {
		this.createdate = createdate;
	}
	public Date getUpdatedate() {
		return updatedate;
	}
	public void setUpdatedate(Date updatedate) {
		this.updatedate = updatedate;
	}

}
