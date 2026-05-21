package com.wb.mapper;

import com.wb.entity.Employee;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface EmployeeMapper {
    List<Employee> getList();

    Employee getById(Integer id);

    Employee getByUsername(String username);

    int insertEmployee(Employee employee);

    int updateEmployee(Employee employee);

    int deleteEmployeeById(Integer id);

    int getActiveCount();

    int count();

    List<Employee> getByDeptId(Integer deptId);

    List<Employee> getUnassigned();

    int updateStatus(@Param("id") Integer id, @Param("status") String status);

    int updateDept(@Param("id") Integer id, @Param("deptId") Integer deptId);
}
