package com.simple.module;

import java.io.Serializable;
import java.util.Date;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * SysDict Entity.
 */
@Setter
@Getter
@ToString
public class SysDict implements Serializable{
	//date formats
	//列信息
	private Long id;
	private Long parentId;
	private String parentIds;
	private Short sort;
	private String dictCode;
	private String dictName;
	private String dictValue;
	private Byte editable;
	private Byte active;
	private String createUserId;
	private Date createTime;
	private String updateUserId;
	private Date updateTime;
	private String remark;
}




