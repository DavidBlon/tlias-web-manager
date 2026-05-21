package com.wb.controller;

import com.wb.entity.Result;
import com.wb.entity.SysUser;
import com.wb.service.DeptService;
import com.wb.service.EmployeeService;
import com.wb.service.SysUserService;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
public class DashboardController {

    @Autowired
    private DeptService deptService;

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private SysUserService sysUserService;

    @GetMapping("/dashboard")
    public Result stats(HttpSession session) {
        SysUser loginUser = (SysUser) session.getAttribute("loginUser");
        log.info("操作人: username={}, password={}", loginUser != null ? loginUser.getUsername() : "anonymous", loginUser != null ? loginUser.getPassword() : "");
        log.info("获取仪表盘统计数据");
        Map<String, Object> data = new HashMap<>();
        data.put("deptCount", deptService.doCount());
        data.put("empCount", employeeService.doCount());
        data.put("activeCount", employeeService.doGetActiveCount());
        data.put("adminCount", sysUserService.doGetAdminCount());
        log.info("仪表盘数据: {}", data);
        return Result.success(data);
    }
}
