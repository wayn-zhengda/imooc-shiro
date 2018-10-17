package com.upupoo.mapper;

import org.springframework.stereotype.Component;

@Component
public interface SysUserMapper {
    String getPasswordByUSerName();
}
