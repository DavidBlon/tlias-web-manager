package com.wb.service;

import com.wb.entity.Dept;
import com.wb.mapper.DeptMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;

@Service
@SuppressWarnings("all")
public class DeptServiceImpl implements DeptService {
    @Autowired
    private DeptMapper deptMapper;

    @Override
    public List<Dept> doList(){
        return deptMapper.getList();
    }

    @Override
    public Dept doGetById(Integer id){
        return deptMapper.getById(id);
    }

    @Override
    public void doInsert(Dept dept){
        dept.setUpdateTime(LocalDateTime.now());
        deptMapper.insertDept(dept);
    }

    @Override
    public void doUpdate(Dept dept){
        dept.setUpdateTime(LocalDateTime.now());
        deptMapper.updateDept(dept);
    }

    @Override
    public void doDeleteById(Integer id){
        deptMapper.deleteDeptById(id);
    }

    @Override
    public int doCount(){
        return deptMapper.count();
    }
}
