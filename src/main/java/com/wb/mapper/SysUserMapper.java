package com.wb.mapper;

import com.wb.entity.SysUser;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface SysUserMapper {
    SysUser getByUsername(String username);

    SysUser getById(Integer id);

    int insertUser(SysUser user);

    int updateProfile(SysUser user);

    int getAdminCount();
}
