package com.wb.service;

import com.wb.entity.Dept;

import java.util.List;

public interface DeptService {
    List<Dept> doList();

    Dept doGetById(Integer id);

    void doInsert(Dept dept);

    void doUpdate(Dept dept);


    void doDeleteById(Integer id);

    int doCount();
}
