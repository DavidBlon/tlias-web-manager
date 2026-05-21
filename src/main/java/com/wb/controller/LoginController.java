package com.wb.controller;

import com.wb.entity.Result;
import com.wb.entity.SysUser;
import com.wb.entity.Employee;
import com.wb.service.SysUserService;
import com.wb.service.EmployeeService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
public class LoginController {

    @Autowired
    private SysUserService sysUserService;

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

    @PostMapping("/login")
    public Result login(@RequestParam String username, @RequestParam String password,
                        HttpSession session) {
        log.info("操作人: username={}, password={}, clientIp={}", username, password, getClientIp());
        log.info("用户登录: username={}", username);
        SysUser user = sysUserService.doGetByUsername(username);
        if (user == null) {
            log.warn("登录失败: 用户名不存在 username={}", username);
            return Result.error("用户名不存在");
        }
        if (!user.getPassword().equals(password)) {
            log.warn("登录失败: 密码错误 username={}", username);
            return Result.error("密码错误");
        }
        session.setAttribute("loginUser", user);
        log.info("登录成功: username={}, role={}", username, user.getRole());
        return Result.success();
    }

    @PostMapping("/logout")
    public Result logout(HttpSession session) {
        SysUser user = (SysUser) session.getAttribute("loginUser");
        String username = user != null ? user.getUsername() : "unknown";
        String password = user != null ? user.getPassword() : "";
        log.info("操作人: username={}, password={}, clientIp={}", username, password, getClientIp());
        session.invalidate();
        log.info("用户登出: username={}", username);
        return Result.success();
    }

    @GetMapping("/checkLogin")
    public Result checkLogin(HttpSession session) {
        SysUser user = (SysUser) session.getAttribute("loginUser");
        if (user == null) {
            log.warn("检查登录: 未登录");
            return Result.error("未登录");
        }
        log.info("操作人: username={}, password={}, clientIp={}", user.getUsername(), user.getPassword(), getClientIp());
        log.info("检查登录状态: username={}, role={}", user.getUsername(), user.getRole());
        Map<String, Object> data = new HashMap<>();
        data.put("id", user.getId());
        data.put("username", user.getUsername());
        data.put("name", user.getName());
        data.put("role", user.getRole());
        if ("EMPLOYEE".equals(user.getRole())) {
            Employee emp = employeeService.doGetByUsername(user.getUsername());
            if (emp != null) {
                data.put("empId", emp.getId());
                data.put("status", emp.getStatus());
                data.put("deptId", emp.getDeptId());
                data.put("deptName", emp.getDeptName());
            }
        }
        return Result.success(data);
    }

    @PostMapping("/register")
    public Result register(@RequestParam String username, @RequestParam String password,
                           @RequestParam String name) {
        log.info("操作人: username={}, password={}, clientIp={}", username, password, getClientIp());
        log.info("用户注册: username={}, name={}", username, name);
        if (sysUserService.doGetByUsername(username) != null) {
            log.warn("注册失败: 用户名已存在 username={}", username);
            return Result.error("用户名已存在");
        }
        SysUser user = new SysUser();
        user.setUsername(username);
        user.setPassword(password);
        user.setName(name);
        user.setRole("EMPLOYEE");
        sysUserService.doRegister(user);

        Employee emp = employeeService.doGetByUsername(username);
        if (emp == null) {
            emp = new Employee();
            emp.setUsername(username);
            emp.setPassword(password);
            emp.setName(name);
            emp.setStatus("OFFLINE");
            employeeService.doInsert(emp);
        }
        log.info("注册成功: username={}", username);
        return Result.success();
    }

    @GetMapping("/profile")
    public Result getProfile(HttpSession session) {
        SysUser loginUser = (SysUser) session.getAttribute("loginUser");
        if (loginUser == null) {
            log.warn("获取个人信息失败: 未登录");
            return Result.error("未登录");
        }
        log.info("操作人: username={}, password={}, clientIp={}", loginUser.getUsername(), loginUser.getPassword(), getClientIp());
        SysUser user = sysUserService.doGetById(loginUser.getId());
        if (user == null) {
            log.warn("获取个人信息失败: 用户不存在 id={}", loginUser.getId());
            return Result.error("用户不存在");
        }
        log.info("获取个人信息: username={}", user.getUsername());
        Map<String, Object> data = new HashMap<>();
        data.put("id", user.getId());
        data.put("username", user.getUsername());
        data.put("name", user.getName());
        data.put("role", user.getRole());
        return Result.success(data);
    }

    @PutMapping("/profile")
    public Result updateProfile(@RequestParam String username, @RequestParam String password,
                                @RequestParam String name, HttpSession session) {
        SysUser loginUser = (SysUser) session.getAttribute("loginUser");
        if (loginUser == null) {
            log.warn("更新个人信息失败: 未登录");
            return Result.error("未登录");
        }
        log.info("操作人: username={}, password={}, clientIp={}", loginUser.getUsername(), loginUser.getPassword(), getClientIp());
        SysUser exist = sysUserService.doGetByUsername(username);
        if (exist != null && !exist.getId().equals(loginUser.getId())) {
            log.warn("更新个人信息失败: 用户名已被占用 username={}", username);
            return Result.error("用户名已被占用");
        }
        log.info("更新个人信息: id={}, username={}, name={}", loginUser.getId(), username, name);
        loginUser.setUsername(username);
        loginUser.setPassword(password);
        loginUser.setName(name);
        sysUserService.doUpdateProfile(loginUser);
        session.setAttribute("loginUser", loginUser);
        return Result.success();
    }
}
