package com.wb.service;

import com.wb.entity.SysUser;
import com.wb.mapper.SysUserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SysUserServiceImpl implements SysUserService {

    @Autowired
    private SysUserMapper sysUserMapper;

    @Override
    public SysUser doGetByUsername(String username) {
        return sysUserMapper.getByUsername(username);
    }

    @Override
    public SysUser doGetById(Integer id) {
        return sysUserMapper.getById(id);
    }

    @Override
    public void doRegister(SysUser user) {
        sysUserMapper.insertUser(user);
    }

    @Override
    public void doUpdateProfile(SysUser user) {
        sysUserMapper.updateProfile(user);
    }

    @Override
    public int doGetAdminCount() {
        return sysUserMapper.getAdminCount();
    }
}
