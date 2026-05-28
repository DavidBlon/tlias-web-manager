package com.wb.controller;

import com.wb.entity.Result;
import com.wb.entity.SysUser;
import com.wb.service.DeptService;
import com.wb.service.EmployeeService;
import com.wb.service.SysUserService;
import com.wb.util.IpUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
public class DashboardController {

    private final DeptService deptService;
    private final EmployeeService employeeService;
    private final SysUserService sysUserService;
    private final HttpServletRequest request;

    public DashboardController(DeptService deptService, EmployeeService employeeService,
                               SysUserService sysUserService, HttpServletRequest request) {
        this.deptService = deptService;
        this.employeeService = employeeService;
        this.sysUserService = sysUserService;
        this.request = request;
    }

    @GetMapping("/dashboard")
    public Result<Map<String, Object>> stats(HttpSession session) {
        SysUser loginUser = (SysUser) session.getAttribute("loginUser");
        log.info("操作人: username={}, clientIp={}", loginUser != null ? loginUser.getUsername() : "anonymous", IpUtils.getClientIp(request));
        Map<String, Object> data = new HashMap<>();
        data.put("deptCount", deptService.doCount());
        data.put("empCount", employeeService.doCount());
        data.put("activeCount", employeeService.doGetActiveCount());
        data.put("adminCount", sysUserService.doGetAdminCount());
        return Result.success(data);
    }
}
