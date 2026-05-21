package com.wb.service;

import com.wb.entity.Employee;

import java.util.List;

public interface EmployeeService {
    List<Employee> doList();

    Employee doGetById(Integer id);

    Employee doGetByUsername(String username);

    void doInsert(Employee employee);

    void doUpdate(Employee employee);

    void doDeleteById(Integer id);

    int doGetActiveCount();

    int doCount();

    List<Employee> doGetByDeptId(Integer deptId);

    List<Employee> doGetUnassigned();

    void doUpdateStatus(Integer id, String status);

    void doAssignDept(Integer id, Integer deptId);
}
