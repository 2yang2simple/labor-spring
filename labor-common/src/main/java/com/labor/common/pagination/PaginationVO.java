package com.labor.common.pagination;

import com.labor.common.pagination.Pagination;

/***
 * 
 * $pagination sql$
 * @author yang yang1
 *
 */
public class PaginationVO {
	public Pagination getPagination() {
		return pagination;
	}

	public void setPagination(Pagination pagination) {
		this.pagination = pagination;
	}

	private Pagination pagination = new Pagination();
	
}
