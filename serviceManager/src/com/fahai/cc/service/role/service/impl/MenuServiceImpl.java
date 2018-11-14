package com.fahai.cc.service.role.service.impl;

import com.fahai.cc.service.role.entity.MenuMast;
import com.fahai.cc.service.role.mapper.MenuMapper;
import com.fahai.cc.service.role.service.MenuService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MenuServiceImpl implements MenuService {

    @Autowired
    private MenuMapper menuMapper;

    @Override
    public List<String> findMenuList(Integer adminUserId) {
        return menuMapper.findMenuList(adminUserId);
    }
}
