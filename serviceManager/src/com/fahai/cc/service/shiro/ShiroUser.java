/**
 *
 */
package com.fahai.cc.service.shiro;

import java.io.Serializable;
import java.util.List;

public class ShiroUser implements Serializable {

    private static final long serialVersionUID = -1373760761780840081L;
    public Integer id;
    public String loginName;
    public String name;
    public Integer userType;
    public List<Long> roleList;

    public ShiroUser(Integer id, String loginName, String name,Integer userType,List<Long> roleList) {
        this.id = id;
        this.loginName = loginName;
        this.name = name;
        this.userType = userType;
        this.roleList = roleList;
    }

    public String getName() {
        return name;
    }

    /**
     * 本函数输出将作为默认的<shiro:principal/>输出.
     */
    @Override
    public String toString() {
    	if("".equals(name) || null==name){
    		return loginName;
    	}else{
    		return name;
    	}
    }
}