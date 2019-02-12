package com.simple.frame.dao.utils;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import java.io.Serializable;

/**
 * 
 */
@Getter
@Setter
@ToString
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
	 * 当面页码,从1开始
	 */
	private Integer pageIndex = 1;
	
	//开始记录索引
	private int offset = 0;

	/**
	 * 每页显示记录数
	 */
	private Integer pageSize = 15;

	private OrderType orderType;
	public Query() {
	}

	public Query(Integer pageSize) {
		this.pageSize = pageSize;
	}


	public String getOrder() {
		return order;
	}

	public int getOffset() {
		return this.pageIndex != null && this.pageIndex > 1 ? (this.pageIndex - 1) * this.pageSize : 0;

	}
}
