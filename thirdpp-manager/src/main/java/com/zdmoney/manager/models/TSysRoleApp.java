package com.zdmoney.manager.models;

import java.io.Serializable;
import java.util.Date;

public class TSysRoleApp implements Serializable {
    /**
	 * 
	 */
	private static final long serialVersionUID = -3258574920560511648L;

	private Long id;

    private Long roleId;

    private Long appId;

    private String creator;

    private String updator;

    private Date createTime;

    private Date updateTime;

    private String resvFld1;

    private String resvFld2;

    private String resvFld3;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getRoleId() {
		return roleId;
	}

	public void setRoleId(Long roleId) {
		this.roleId = roleId;
	}

	public Long getAppId() {
		return appId;
	}

	public void setAppId(Long appId) {
		this.appId = appId;
	}

	public String getCreator() {
		return creator;
	}

	public void setCreator(String creator) {
		this.creator = creator;
	}

	public String getUpdator() {
		return updator;
	}

	public void setUpdator(String updator) {
		this.updator = updator;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public String getResvFld1() {
		return resvFld1;
	}

	public void setResvFld1(String resvFld1) {
		this.resvFld1 = resvFld1;
	}

	public String getResvFld2() {
		return resvFld2;
	}

	public void setResvFld2(String resvFld2) {
		this.resvFld2 = resvFld2;
	}

	public String getResvFld3() {
		return resvFld3;
	}

	public void setResvFld3(String resvFld3) {
		this.resvFld3 = resvFld3;
	}

    
}