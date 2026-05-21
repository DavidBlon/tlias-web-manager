package com.wb.mapper;

import com.wb.entity.Dept;
import org.apache.ibatis.annotations.Mapper;
import java.util.List;

@Mapper
public interface DeptMapper {
    List<Dept> getList();

    Dept getById(Integer id);

    int insertDept(Dept dept);

    int updateDept(Dept dept);

    int deleteDeptById(Integer id);

    int count();
}
