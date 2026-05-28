package com.wb.service;

import com.wb.entity.Dept;
import com.wb.mapper.DeptMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class DeptServiceImpl implements DeptService {

    private final DeptMapper deptMapper;

    public DeptServiceImpl(DeptMapper deptMapper) {
        this.deptMapper = deptMapper;
    }

    @Override
    public List<Dept> doList() {
        return deptMapper.getList();
    }

    @Override
    public Dept doGetById(Integer id) {
        return deptMapper.getById(id);
    }

    @Override
    public void doInsert(Dept dept) {
        dept.setUpdateTime(LocalDateTime.now());
        deptMapper.insertDept(dept);
    }

    @Override
    public void doUpdate(Dept dept) {
        dept.setUpdateTime(LocalDateTime.now());
        deptMapper.updateDept(dept);
    }

    @Override
    public void doDeleteById(Integer id) {
        deptMapper.deleteDeptById(id);
    }

    @Override
    public int doCount() {
        return deptMapper.count();
    }
}
