/**  
 * Copyright © 2017鼎泰智源科技有限公司. All rights reserved.
 *
 * @Title: Field.java
 * @Prject: InterfaceWeb
 * @Package: com.fahai.cc.interf.mysql.entity
 * @Description: TODO
 * @author: Aaron.ye  
 * @date: 2017年6月16日 下午1:23:47
 * @version: V1.0  
 */
package com.fahai.cc.interf.mysql.entity;

import java.io.Serializable;

/**
 * @ClassName: Field
 * @Description: TODO
 * @author: Aaron.ye
 * @date: 2017年6月16日 下午1:23:47
 */
public class Field implements Serializable{

	/**
	 * @fieldName: serialVersionUID
	 * @fieldType: long
	 * @Description: TODO
	 */
	private static final long serialVersionUID = 1133660529105178139L;
	
	private String fieldCode;
	private String fieldName;
	private String fieldType;
	private String dimensionCode;
	public String getFieldCode() {
		return fieldCode;
	}
	public void setFieldCode(String fieldCode) {
		this.fieldCode = fieldCode;
	}
	public String getFieldName() {
		return fieldName;
	}
	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}
	public String getFieldType() {
		return fieldType;
	}
	public void setFieldType(String fieldType) {
		this.fieldType = fieldType;
	}
	public String getDimensionCode() {
		return dimensionCode;
	}
	public void setDimensionCode(String dimensionCode) {
		this.dimensionCode = dimensionCode;
	}
	
}
