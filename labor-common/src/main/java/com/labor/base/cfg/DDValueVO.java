package com.labor.base.cfg;

import java.util.Date;

import com.labor.common.pagination.PaginationVO;


public class DDValueVO extends PaginationVO{
	private String ddid;
	private String ddvalue;
	private String ddtype;
	private String ddstatus;
	private String ddorder;
	private String dddesc;
	private String createby;
	private String updateby;
	private Date createdate;
	private Date updatedate;
	
	
	public String getDdid() {
		return ddid;
	}
	public void setDdid(String ddid) {
		this.ddid = ddid;
	}
	public String getDdvalue() {
		return ddvalue;
	}
	public void setDdvalue(String ddvalue) {
		this.ddvalue = ddvalue;
	}
	public String getDdtype() {
		return ddtype;
	}
	public void setDdtype(String ddtype) {
		this.ddtype = ddtype;
	}
	public String getDdstatus() {
		return ddstatus;
	}
	public void setDdstatus(String ddstatus) {
		this.ddstatus = ddstatus;
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
	public String getDdorder() {
		return ddorder;
	}
	public void setDdorder(String ddorder) {
		this.ddorder = ddorder;
	}
	public String getDddesc() {
		return dddesc;
	}
	public void setDddesc(String dddesc) {
		this.dddesc = dddesc;
	}
	
	
	
}
