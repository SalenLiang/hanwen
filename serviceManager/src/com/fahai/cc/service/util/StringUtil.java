/**  
 * Copyright © 2017鼎泰智源科技有限公司. All rights reserved.
 *
 * @Title: StringUtil.java
 * @Prject: InterfaceWeb
 * @Package: com.fahai.cc.interf.util
 * @Description: TODO
 * @author: Aaron.ye  
 * @date: 2017年6月9日 下午2:53:47
 * @version: V1.0  
 */
package com.fahai.cc.service.util;

/**
 * @ClassName: StringUtil
 * @Description: TODO
 * @author: Aaron.ye
 * @date: 2017年6月9日 下午2:53:47
 */
public class StringUtil {

	public static String toEmpty(String str) {

		if (str == null || str.trim().length() == 0)
			return "";

		return str.trim();
	}
	
	/*
	 * 将int类型的数字格式化为3位以逗号隔开的形式，ls：  4646448 -->  4,646,448
	 */
	
	public static String number2KilobitStr(Integer num){
		
		if (num==null) {
			return "0";
		}
		String numStr = String.valueOf(num);
		if (numStr.length()>3) {
			int mod = numStr.length()%3;
			String output = (mod==0?"":(numStr.substring(0, mod)));
			for(int i=0;i<numStr.length()/3;i++){
				if ((mod==0)&&(i==0)) {
					output += numStr.substring(mod+3*i,mod+3*i+3);
				}else{
					output +=","+ numStr.substring(mod+3*i,mod+3*i+3);
				}
			}
			return output;
		}
		return numStr;
	}
	
}
