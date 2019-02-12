package com.simple.frame.dao.utils;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.util.List;

/**
 * 分页对象. 包含当前页数据及分页信息
 * 
 */
@SuppressWarnings("serial")
@Getter
@Setter
@ToString
public class Pager<T> implements Serializable {

	private static int DEFAULT_PAGE_SIZE = 20;

	/**
	 * 每页的记录数
	 */
	private int pageSize = DEFAULT_PAGE_SIZE;

	/**
	 * 当前页中存放的数据
	 */
	private List<T> data;

	/**
	 * 总记录数
	 */
	private int rowCount;

	/**
	 * 页数
	 */
	private int pageCount;

	/**
	 * 跳转页数
	 */
	private int pageIndex;

	/**
	 * 是否有上一页
	 */
	private boolean hasPrevious = false;
	
	/**
	 * 是否有下一页
	 */
	private boolean hasNext = false;
	
	/**
	 * 当前记录条数
	 */
	private int currentNumber;

	public Pager(int pageIndex, int rowCount) {
		this.pageIndex = pageIndex;
		this.rowCount = rowCount;
		this.pageCount = getTotalPageCount();
		refresh();
	}

	/**
	 * 构造方法
	 */
	public Pager(int pageIndex, int pageSize, int rowCount) {
		this.pageIndex = pageIndex;
		this.pageSize = pageSize;
		this.rowCount = rowCount;
		this.pageCount = getTotalPageCount();
		refresh();
	}

	/**
	 * 
	 */
	public Pager(int pageIndex, int pageSize, int rowCount, List<T> data) {
		this.pageIndex = pageIndex;
		this.pageSize = pageSize;
		this.rowCount = rowCount;
		this.pageCount = getTotalPageCount();
		this.data = data;
		refresh();
	}

	/**
	 * 取总页数
	 */
	private final int getTotalPageCount() {
		if (rowCount % pageSize == 0) {
			return rowCount / pageSize;
		}  else {
			return rowCount / pageSize + 1;
		}
	}

	/**
	 * 刷新当前分页对象数据
	 */
	private void refresh() {
		if (pageCount <= 1) {
			hasPrevious = false;
			hasNext = false;
		} else if (pageIndex == 1) {
			hasPrevious = false;
			hasNext = true;
		} else if (pageIndex == pageCount) {
			hasPrevious = true;
			hasNext = false;
		} else {
			hasPrevious = true;
			hasNext = true;
		}
	}


	/**
	 * 取当前页中的记录.
	 */
	public Object getResult() {
		return data;
	}








	public int getCurrentNumber() {
		if(data != null){
			this.currentNumber = data.size();
		}
		return currentNumber;
	}

	public void setCurrentNumber(int currentNumber) {
		this.currentNumber = currentNumber;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	/**
	 * 获取跳转页第�?��数据在数据集的位�?
	 */
	public int getStartOfPage() {
		return (pageIndex - 1) * pageSize;
	}
}