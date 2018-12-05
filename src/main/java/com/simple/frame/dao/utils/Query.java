package com.simple.frame.dao.utils;

import java.io.Serializable;

/**
 * 
 */
public class Query implements Serializable{

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 817880730448759944L;

	/**
	 * 自动排序属性
	 */
	private String order;

	/**
	 * 排序方式
	 */
	private boolean isAsc;

	/**
	 * 跳转
	 */
	private int page=1;
	
	//开始记录索引
	private int offset = 0;

	/**
	 * 每页显示记录数
	 */
	private int pageSize = 15;

	public Query() {

	}

	public Query(int pageSize) {
		this.pageSize = pageSize;
	}


	public String getOrder() {
		return order;
	}

	public void setOrder(String order) {
		this.order = order;
	}

	public boolean getIsAsc() {
		return isAsc;
	}

	public void setIsAsc(boolean isAsc) {
		this.isAsc = isAsc;
	}

	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public int getOffset() {
		int page = this.page == 0 ? 1 : this.page;
		offset = (page - 1 )* this.pageSize;
		return offset;
	}
}
