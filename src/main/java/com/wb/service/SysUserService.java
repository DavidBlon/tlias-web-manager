package com.wb.service;

import com.wb.entity.SysUser;

public interface SysUserService {
    SysUser doGetByUsername(String username);

    SysUser doGetById(Integer id);

    void doRegister(SysUser user);

    void doUpdateProfile(SysUser user);

    int doGetAdminCount();
}
