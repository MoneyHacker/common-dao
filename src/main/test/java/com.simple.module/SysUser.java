package com.simple.module;

import java.io.Serializable;
import java.util.Date;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * SysUser Entity.
 */
@Setter
@Getter
@ToString
public class SysUser implements Serializable{
	//date formats
	//列信息
	private Long id;
	private Long companyId;
	private Long officeId;
	private String password;
	private String salt;
	private String userName;
	private String realName;
	private String userCode;
	private Byte sex;
	private Date userBorn;
	private String fromSource;
	private String imgUrl;
	private String cellPhone;
	private String email;
	private Byte active;
	private String createUserId;
	private Date createTime;
	private String updateUserId;
	private Date updateTime;
	private String remark;
}




