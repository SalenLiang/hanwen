/**  
 * Copyright © 2017鼎泰智源科技有限公司. All rights reserved.
 *
 * @Title: Dimension.java
 * @Prject: InterfaceWeb
 * @Package: com.fahai.cc.interf.mysql.entity
 * @Description: TODO
 * @author: Aaron.ye  
 * @date: 2017年6月16日 下午3:50:27
 * @version: V1.0  
 */
package com.fahai.cc.interf.mysql.entity;

import java.io.Serializable;

/**
 * @ClassName: Dimension
 * @Description: TODO
 * @author: Aaron.ye
 * @date: 2017年6月16日 下午3:50:27
 */
public class Dimension implements Serializable{
	
	/**
	 * @fieldName: serialVersionUID
	 * @fieldType: long
	 * @Description: TODO
	 */
	private static final long serialVersionUID = -3139434556133003697L;

	private int dimensionId;
	
	private String dimensionCode;
	
	private int domainId;
	
	private String domainCode;
	
	private String dimensionName;

	public String getDimensionName() {
		return dimensionName;
	}

	public void setDimensionName(String dimensionName) {
		this.dimensionName = dimensionName;
	}

	public int getDimensionId() {
		return dimensionId;
	}

	public void setDimensionId(int dimensionId) {
		this.dimensionId = dimensionId;
	}

	public String getDimensionCode() {
		return dimensionCode;
	}

	public void setDimensionCode(String dimensionCode) {
		this.dimensionCode = dimensionCode;
	}

	public int getDomainId() {
		return domainId;
	}

	public void setDomainId(int domainId) {
		this.domainId = domainId;
	}

	public String getDomainCode() {
		return domainCode;
	}

	public void setDomainCode(String domainCode) {
		this.domainCode = domainCode;
	}
	
	

}
