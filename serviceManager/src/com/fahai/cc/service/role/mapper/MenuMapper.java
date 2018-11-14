package com.fahai.cc.service.role.mapper;


import java.util.List;

public interface MenuMapper {
    List<String> findMenuList(Integer adminUserId);
}
