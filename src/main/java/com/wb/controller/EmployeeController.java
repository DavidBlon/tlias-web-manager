package com.wb.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.wb.entity.Employee;
import com.wb.entity.PageResult;
import com.wb.entity.Result;
import com.wb.entity.SysUser;
import com.wb.service.EmployeeService;
import com.wb.util.IpUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
public class EmployeeController {

    private final EmployeeService employeeService;
    private final HttpServletRequest request;

    public EmployeeController(EmployeeService employeeService, HttpServletRequest request) {
        this.employeeService = employeeService;
        this.request = request;
    }

    @GetMapping("/employees")
    public Result<PageResult<Employee>> getAll(@RequestParam(defaultValue = "1") int pageNum,
                                               @RequestParam(defaultValue = "10") int pageSize,
                                               HttpSession session) {
        SysUser loginUser = (SysUser) session.getAttribute("loginUser");
        log.info("操作人: username={}, clientIp={}", loginUser != null ? loginUser.getUsername() : "anonymous", IpUtils.getClientIp(request));
        PageHelper.startPage(pageNum, pageSize);
        PageInfo<Employee> pageInfo = new PageInfo<>(employeeService.doList());
        return Result.success(new PageResult<>(pageInfo.getList(), pageInfo.getTotal(), pageNum, pageSize));
    }

    @GetMapping(value = "/employees", params = "id")
    public Result<Employee> getById(@RequestParam Integer id, HttpSession session) {
        SysUser loginUser = (SysUser) session.getAttribute("loginUser");
        log.info("操作人: username={}, clientIp={}", loginUser != null ? loginUser.getUsername() : "anonymous", IpUtils.getClientIp(request));
        return Result.success(employeeService.doGetById(id));
    }

    @GetMapping(value = "/employees", params = "deptId")
    public Result<List<Employee>> getByDeptId(@RequestParam Integer deptId, HttpSession session) {
        SysUser loginUser = (SysUser) session.getAttribute("loginUser");
        log.info("操作人: username={}, clientIp={}", loginUser != null ? loginUser.getUsername() : "anonymous", IpUtils.getClientIp(request));
        return Result.success(employeeService.doGetByDeptId(deptId));
    }

    @GetMapping(value = "/employees", params = "noDept")
    public Result<List<Employee>> getUnassigned(HttpSession session) {
        SysUser loginUser = (SysUser) session.getAttribute("loginUser");
        log.info("操作人: username={}, clientIp={}", loginUser != null ? loginUser.getUsername() : "anonymous", IpUtils.getClientIp(request));
        return Result.success(employeeService.doGetUnassigned());
    }

    @PostMapping("/employees")
    public Result<Void> add(@Valid @RequestBody Employee employee, HttpSession session) {
        SysUser loginUser = (SysUser) session.getAttribute("loginUser");
        log.info("操作人: username={}, clientIp={}", loginUser != null ? loginUser.getUsername() : "anonymous", IpUtils.getClientIp(request));
        log.info("新增员工: username={}, name={}, deptId={}", employee.getUsername(), employee.getName(), employee.getDeptId());
        employeeService.doInsert(employee);
        return Result.success();
    }

    @PutMapping("/employees")
    public Result<Void> update(@RequestParam Integer id, @Valid @RequestBody Employee employee, HttpSession session) {
        SysUser loginUser = (SysUser) session.getAttribute("loginUser");
        log.info("操作人: username={}, clientIp={}", loginUser != null ? loginUser.getUsername() : "anonymous", IpUtils.getClientIp(request));
        log.info("修改员工: id={}, name={}, deptId={}", id, employee.getName(), employee.getDeptId());
        employee.setId(id);
        employeeService.doUpdate(employee);
        return Result.success();
    }

    @DeleteMapping("/employees")
    public Result<Void> delete(@RequestParam Integer id, HttpSession session) {
        SysUser loginUser = (SysUser) session.getAttribute("loginUser");
        log.info("操作人: username={}, clientIp={}", loginUser != null ? loginUser.getUsername() : "anonymous", IpUtils.getClientIp(request));
        log.info("删除员工: id={}", id);
        employeeService.doDeleteById(id);
        return Result.success();
    }

    @PutMapping("/employees/status")
    public Result<Void> updateStatus(@RequestParam String status, HttpSession session) {
        SysUser loginUser = (SysUser) session.getAttribute("loginUser");
        log.info("操作人: username={}, clientIp={}", loginUser != null ? loginUser.getUsername() : "anonymous", IpUtils.getClientIp(request));
        if (loginUser == null) {
            return Result.error("未登录");
        }
        Employee emp = employeeService.doGetByUsername(loginUser.getUsername());
        if (emp == null) {
            return Result.error("员工记录不存在");
        }
        if (!"ONLINE".equals(status) && !"OFFLINE".equals(status)) {
            return Result.error("无效的状态值");
        }
        log.info("更新员工状态: empId={}, status={}", emp.getId(), status);
        employeeService.doUpdateStatus(emp.getId(), status);
        return Result.success();
    }

    @PutMapping("/employees/dept")
    public Result<Void> assignDept(@RequestParam Integer empId,
                                   @RequestParam(required = false) Integer deptId,
                                   HttpSession session) {
        SysUser loginUser = (SysUser) session.getAttribute("loginUser");
        log.info("操作人: username={}, clientIp={}", loginUser != null ? loginUser.getUsername() : "anonymous", IpUtils.getClientIp(request));
        log.info("分配员工部门: empId={}, deptId={}", empId, deptId);
        employeeService.doAssignDept(empId, deptId);
        return Result.success();
    }
}
