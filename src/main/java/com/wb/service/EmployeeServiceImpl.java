package com.wb.service;

import com.wb.entity.Employee;
import com.wb.entity.SysUser;
import com.wb.mapper.EmployeeMapper;
import com.wb.mapper.SysUserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    @Autowired
    private EmployeeMapper employeeMapper;

    @Autowired
    private SysUserMapper sysUserMapper;

    @Override
    public List<Employee> doList() {
        return employeeMapper.getList();
    }

    @Override
    public Employee doGetById(Integer id) {
        return employeeMapper.getById(id);
    }

    @Override
    public Employee doGetByUsername(String username) {
        return employeeMapper.getByUsername(username);
    }

    @Override
    public void doInsert(Employee employee) {
        employeeMapper.insertEmployee(employee);
        if (sysUserMapper.getByUsername(employee.getUsername()) == null) {
            SysUser user = new SysUser();
            user.setUsername(employee.getUsername());
            user.setPassword(employee.getPassword() != null ? employee.getPassword() : "123456");
            user.setName(employee.getName());
            user.setRole("EMPLOYEE");
            sysUserMapper.insertUser(user);
        }
    }

    @Override
    public void doUpdate(Employee employee) {
        employeeMapper.updateEmployee(employee);
    }

    @Override
    public void doDeleteById(Integer id) {
        employeeMapper.deleteEmployeeById(id);
    }

    @Override
    public int doGetActiveCount() {
        return employeeMapper.getActiveCount();
    }

    @Override
    public int doCount() {
        return employeeMapper.count();
    }

    @Override
    public List<Employee> doGetByDeptId(Integer deptId) {
        return employeeMapper.getByDeptId(deptId);
    }

    @Override
    public List<Employee> doGetUnassigned() {
        return employeeMapper.getUnassigned();
    }

    @Override
    public void doUpdateStatus(Integer id, String status) {
        employeeMapper.updateStatus(id, status);
    }

    @Override
    public void doAssignDept(Integer id, Integer deptId) {
        employeeMapper.updateDept(id, deptId);
    }
}
