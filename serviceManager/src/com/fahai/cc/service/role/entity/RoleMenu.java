package com.fahai.cc.service.role.entity;

import java.util.Date;

public class RoleMenu implements java.io.Serializable{

    private static final long serialVersionUID = 1L;
    private String roleId;   //角色Id
    private String menuId;   //菜单Id
    private String menuSel;   //是否有查询功能
    private String menuIns;   //是否有新增功能
    private String menuUpd;   //是否有修改功能
    private String menuDel;   //是否有删除功能
    private String useYn;   //角色与菜单状态
    private Date registerDate;   //注册日期
    private String registerId;   //注册Id

    public String getRoleId() {
        return roleId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }

    public String getMenuId() {
        return menuId;
    }

    public void setMenuId(String menuId) {
        this.menuId = menuId;
    }

    public String getMenuSel() {
        return menuSel;
    }

    public void setMenuSel(String menuSel) {
        this.menuSel = menuSel;
    }

    public String getMenuIns() {
        return menuIns;
    }

    public void setMenuIns(String menuIns) {
        this.menuIns = menuIns;
    }

    public String getMenuUpd() {
        return menuUpd;
    }

    public void setMenuUpd(String menuUpd) {
        this.menuUpd = menuUpd;
    }

    public String getMenuDel() {
        return menuDel;
    }

    public void setMenuDel(String menuDel) {
        this.menuDel = menuDel;
    }

    public String getUseYn() {
        return useYn;
    }

    public void setUseYn(String useYn) {
        this.useYn = useYn;
    }

    public Date getRegisterDate() {
        return registerDate;
    }

    public void setRegisterDate(Date registerDate) {
        this.registerDate = registerDate;
    }

    public String getRegisterId() {
        return registerId;
    }

    public void setRegisterId(String registerId) {
        this.registerId = registerId;
    }
}
