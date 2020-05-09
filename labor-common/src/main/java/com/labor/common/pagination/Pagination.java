package com.labor.common.pagination;

public class Pagination {
	
	private String MYSQL_START = " ";
	private String MYSQL_END = " LIMIT <STA>,<END> ";
	
	private String DB2_START = " select * from (SELECT a.*, row_number() over() AS rn FROM ( ";
	private String DB2_END = " ) AS a ) AS b WHERE b.rn BETWEEN <STA> AND <END> ";

	private int DBTYPE=1;//1-mysql;2-db2;
	private String sqlPageStart;
	private String sqlPageEnd;
	
	private int pageSize;
	private int pageNumber=1;//start from 1
//	private int currentPageNumber=1;
	
	public static final int PAGE_SIZE_DEFUALT=5;
	
	public void setPage(int pageNumber){
		setPage(pageNumber,PAGE_SIZE_DEFUALT);
	}
	
	public void setPage(int pageNumber,int pageSize){
		if (pageNumber<=0){
			pageNumber=1;
		}
		if (pageSize<=0){
			pageSize=PAGE_SIZE_DEFUALT;
		}
		
		switch (DBTYPE){
			case 1:
				sqlPageStart = MYSQL_START;
				sqlPageEnd = MYSQL_END.replaceAll("<STA>", String.valueOf((pageNumber-1)*pageSize));
				sqlPageEnd = sqlPageEnd.replaceAll("<END>", String.valueOf(pageSize));
				break;
			case 2:
				sqlPageStart = DB2_START;
				sqlPageEnd = DB2_END.replaceAll("<STA>", String.valueOf((pageNumber-1)*pageSize));
				sqlPageEnd = sqlPageEnd.replaceAll("<END>", String.valueOf((pageNumber)*pageSize));
				break;
			default:
				sqlPageStart = MYSQL_START;
				sqlPageEnd = MYSQL_END.replaceAll("<STA>", String.valueOf((pageNumber-1)*pageSize));
				sqlPageEnd = sqlPageEnd.replaceAll("<END>", String.valueOf(pageSize));
		}

		this.pageSize=pageSize;
		this.pageNumber=pageNumber;
	}
	

	public void setPageOff(){
		sqlPageStart = "";
		sqlPageEnd = "";
		this.pageSize=0;
		this.pageNumber=0;
	}

	public String getSqlPageStart() {
		return sqlPageStart;
	}

	public String getSqlPageEnd() {
		return sqlPageEnd;
	}

	public int getPageSize() {
		return pageSize;
	}

	public int getPageNumber() {
		return pageNumber;
	}

}
