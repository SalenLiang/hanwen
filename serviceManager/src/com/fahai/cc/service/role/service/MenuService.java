package com.fahai.cc.service.role.service;

import java.util.List;

import com.fahai.cc.service.role.entity.MenuMast;

public interface MenuService {

    List<String> findMenuList(Integer adminUserId);
}
