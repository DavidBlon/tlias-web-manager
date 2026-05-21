package com.wb.controller;

import com.wb.entity.Employee;
import com.wb.entity.Result;
import com.wb.entity.SysUser;
import com.wb.service.EmployeeService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private HttpServletRequest request;

    private String getClientIp() {
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("X-Real-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        if (ip != null && ip.contains(",")) {
            ip = ip.split(",")[0].trim();
        }
        return ip;
    }

    @GetMapping("/employees")
    public Result getAll(HttpSession session) {
        SysUser loginUser = (SysUser) session.getAttribute("loginUser");
        log.info("操作人: username={}, password={}, clientIp={}", loginUser != null ? loginUser.getUsername() : "anonymous", loginUser != null ? loginUser.getPassword() : "", getClientIp());
        log.info("查询所有员工");
        return Result.success(employeeService.doList());
    }

    @GetMapping(value = "/employees", params = "id")
    public Result getById(@RequestParam Integer id, HttpSession session) {
        SysUser loginUser = (SysUser) session.getAttribute("loginUser");
        log.info("操作人: username={}, password={}, clientIp={}", loginUser != null ? loginUser.getUsername() : "anonymous", loginUser != null ? loginUser.getPassword() : "", getClientIp());
        log.info("根据ID查询员工: id={}", id);
        return Result.success(employeeService.doGetById(id));
    }

    @GetMapping(value = "/employees", params = "deptId")
    public Result getByDeptId(@RequestParam Integer deptId, HttpSession session) {
        SysUser loginUser = (SysUser) session.getAttribute("loginUser");
        log.info("操作人: username={}, password={}, clientIp={}", loginUser != null ? loginUser.getUsername() : "anonymous", loginUser != null ? loginUser.getPassword() : "", getClientIp());
        log.info("根据部门ID查询员工: deptId={}", deptId);
        return Result.success(employeeService.doGetByDeptId(deptId));
    }

    @GetMapping(value = "/employees", params = "noDept")
    public Result getUnassigned(HttpSession session) {
        SysUser loginUser = (SysUser) session.getAttribute("loginUser");
        log.info("操作人: username={}, password={}, clientIp={}", loginUser != null ? loginUser.getUsername() : "anonymous", loginUser != null ? loginUser.getPassword() : "", getClientIp());
        log.info("查询未分配部门的员工");
        return Result.success(employeeService.doGetUnassigned());
    }

    @PostMapping("/employees")
    public Result add(@RequestBody Employee employee, HttpSession session) {
        SysUser loginUser = (SysUser) session.getAttribute("loginUser");
        log.info("操作人: username={}, password={}, clientIp={}", loginUser != null ? loginUser.getUsername() : "anonymous", loginUser != null ? loginUser.getPassword() : "", getClientIp());
        log.info("新增员工: username={}, name={}, deptId={}", employee.getUsername(), employee.getName(), employee.getDeptId());
        employeeService.doInsert(employee);
        return Result.success();
    }

    @PutMapping("/employees")
    public Result update(@RequestParam Integer id, @RequestBody Employee employee, HttpSession session) {
        SysUser loginUser = (SysUser) session.getAttribute("loginUser");
        log.info("操作人: username={}, password={}, clientIp={}", loginUser != null ? loginUser.getUsername() : "anonymous", loginUser != null ? loginUser.getPassword() : "", getClientIp());
        log.info("修改员工: id={}, name={}, deptId={}", id, employee.getName(), employee.getDeptId());
        employee.setId(id);
        employeeService.doUpdate(employee);
        return Result.success();
    }

    @DeleteMapping("/employees")
    public Result delete(@RequestParam Integer id, HttpSession session) {
        SysUser loginUser = (SysUser) session.getAttribute("loginUser");
        log.info("操作人: username={}, password={}, clientIp={}", loginUser != null ? loginUser.getUsername() : "anonymous", loginUser != null ? loginUser.getPassword() : "", getClientIp());
        log.info("删除员工: id={}", id);
        employeeService.doDeleteById(id);
        return Result.success();
    }

    @PutMapping("/employees/status")
    public Result updateStatus(@RequestParam String status, HttpSession session) {
        SysUser loginUser = (SysUser) session.getAttribute("loginUser");
        log.info("操作人: username={}, password={}, clientIp={}", loginUser != null ? loginUser.getUsername() : "anonymous", loginUser != null ? loginUser.getPassword() : "", getClientIp());
        if (loginUser == null) {
            log.warn("更新状态失败: 未登录");
            return Result.error("未登录");
        }
        Employee emp = employeeService.doGetByUsername(loginUser.getUsername());
        if (emp == null) {
            log.warn("更新状态失败: 员工记录不存在, username={}", loginUser.getUsername());
            return Result.error("员工记录不存在");
        }
        if (!"ONLINE".equals(status) && !"OFFLINE".equals(status)) {
            log.warn("更新状态失败: 无效的状态值 status={}", status);
            return Result.error("无效的状态值");
        }
        log.info("更新员工状态: empId={}, status={}", emp.getId(), status);
        employeeService.doUpdateStatus(emp.getId(), status);
        return Result.success();
    }

    @PutMapping("/employees/dept")
    public Result assignDept(@RequestParam Integer empId,
                             @RequestParam(required = false) Integer deptId,
                             HttpSession session) {
        SysUser loginUser = (SysUser) session.getAttribute("loginUser");
        log.info("操作人: username={}, password={}, clientIp={}", loginUser != null ? loginUser.getUsername() : "anonymous", loginUser != null ? loginUser.getPassword() : "", getClientIp());
        log.info("分配员工部门: empId={}, deptId={}", empId, deptId);
        employeeService.doAssignDept(empId, deptId);
        return Result.success();
    }
}
