package com.fahai.cc.service.role.entity;

import org.apache.ibatis.type.Alias;

import java.util.Date;

@Alias("menuMast")
public class MenuMast implements java.io.Serializable{

    private static final long serialVersionUID = 1L;
    private String menuId;   //菜单id
    private String menuName;   //菜单名称
    private String menuUrl;   //菜单URL
    private String menuLv;   //菜单级别
    private String upperMenuId;   //上级菜单Id
    private String menuSel;   //是否有查询功能
    private String menuIns;   //是否有新增功能
    private String menuUpd;   //是否有修改功能
    private String menuDel;   //是否有删除功能
    private String menuState;   //菜单使用状态
    private Date registerDate;   //注册日期
    private String registerId;   //注册Id
    private String searchName; //搜索关键字

    public String getMenuId(){
       return menuId;
    }

    public void setMenuId(String menuId){
       this.menuId = menuId;
    }


    public String getMenuName(){
       return menuName;
    }

    public void setMenuName(String menuName){
       this.menuName = menuName;
    }


    public String getMenuUrl(){
       return menuUrl;
    }

    public void setMenuUrl(String menuUrl){
       this.menuUrl = menuUrl;
    }


    public String getMenuLv(){
       return menuLv;
    }

    public void setMenuLv(String menuLv){
       this.menuLv = menuLv;
    }


    public String getUpperMenuId(){
       return upperMenuId;
    }

    public void setUpperMenuId(String upperMenuId){
       this.upperMenuId = upperMenuId;
    }


    public String getMenuSel(){
       return menuSel;
    }

    public void setMenuSel(String menuSel){
       this.menuSel = menuSel;
    }


    public String getMenuIns(){
       return menuIns;
    }

    public void setMenuIns(String menuIns){
       this.menuIns = menuIns;
    }


    public String getMenuUpd(){
       return menuUpd;
    }

    public void setMenuUpd(String menuUpd){
       this.menuUpd = menuUpd;
    }


    public String getMenuDel(){
       return menuDel;
    }

    public void setMenuDel(String menuDel){
       this.menuDel = menuDel;
    }


    public String getMenuState(){
       return menuState;
    }

    public void setMenuState(String menuState){
       this.menuState = menuState;
    }


    public Date getRegisterDate(){
       return registerDate;
    }

    public void setRegisterDate(Date registerDate){
       this.registerDate = registerDate;
    }


    public String getRegisterId(){
       return registerId;
    }

    public void setRegisterId(String registerId){
       this.registerId = registerId;
    }


	public String getSearchName() {
		return searchName;
	}

	public void setSearchName(String searchName) {
		this.searchName = searchName;
	}
}
