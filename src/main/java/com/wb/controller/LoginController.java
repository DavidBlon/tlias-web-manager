package com.wb.controller;

import com.wb.entity.Employee;
import com.wb.entity.Result;
import com.wb.entity.SysUser;
import com.wb.service.SysUserService;
import com.wb.service.EmployeeService;
import com.wb.util.IpUtils;
import com.wb.util.PasswordUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
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

    private final SysUserService sysUserService;
    private final EmployeeService employeeService;
    private final HttpServletRequest request;

    public LoginController(SysUserService sysUserService, EmployeeService employeeService,
                           HttpServletRequest request) {
        this.sysUserService = sysUserService;
        this.employeeService = employeeService;
        this.request = request;
    }

    @PostMapping("/login")
    public Result<Void> login(@RequestParam String username, @RequestParam String password,
                              HttpSession session) {
        log.info("用户登录: username={}, clientIp={}", username, IpUtils.getClientIp(request));
        SysUser user = sysUserService.doGetByUsername(username);
        if (user == null) {
            log.warn("登录失败: 用户名不存在 username={}", username);
            return Result.error("用户名不存在");
        }
        if (!PasswordUtils.matches(password, user.getPassword())) {
            log.warn("登录失败: 密码错误 username={}", username);
            return Result.error("密码错误");
        }
        if (!PasswordUtils.isBcrypt(user.getPassword())) {
            user.setPassword(PasswordUtils.encode(password));
            sysUserService.doUpdateProfile(user);
        }
        session.setAttribute("loginUser", user);
        log.info("登录成功: username={}, role={}", username, user.getRole());
        return Result.success();
    }

    @PostMapping("/logout")
    public Result<Void> logout(HttpSession session) {
        SysUser user = (SysUser) session.getAttribute("loginUser");
        String username = user != null ? user.getUsername() : "unknown";
        log.info("用户登出: username={}, clientIp={}", username, IpUtils.getClientIp(request));
        session.invalidate();
        return Result.success();
    }

    @GetMapping("/checkLogin")
    public Result<Map<String, Object>> checkLogin(HttpSession session) {
        SysUser user = (SysUser) session.getAttribute("loginUser");
        if (user == null) {
            return Result.error("未登录");
        }
        log.info("检查登录状态: username={}, clientIp={}", user.getUsername(), IpUtils.getClientIp(request));
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
    public Result<Void> register(@RequestParam String username, @RequestParam String password,
                                 @RequestParam String name) {
        log.info("用户注册: username={}, name={}, clientIp={}", username, name, IpUtils.getClientIp(request));
        if (sysUserService.doGetByUsername(username) != null) {
            return Result.error("用户名已存在");
        }
        SysUser user = new SysUser();
        user.setUsername(username);
        user.setPassword(PasswordUtils.encode(password));
        user.setName(name);
        user.setRole("EMPLOYEE");
        sysUserService.doRegister(user);

        Employee emp = employeeService.doGetByUsername(username);
        if (emp == null) {
            emp = new Employee();
            emp.setUsername(username);
            emp.setPassword(PasswordUtils.encode(password));
            emp.setName(name);
            emp.setStatus("OFFLINE");
            employeeService.doInsert(emp);
        }
        return Result.success();
    }

    @GetMapping("/profile")
    public Result<Map<String, Object>> getProfile(HttpSession session) {
        SysUser loginUser = (SysUser) session.getAttribute("loginUser");
        if (loginUser == null) {
            return Result.error("未登录");
        }
        SysUser user = sysUserService.doGetById(loginUser.getId());
        if (user == null) {
            return Result.error("用户不存在");
        }
        log.info("获取个人信息: username={}, clientIp={}", user.getUsername(), IpUtils.getClientIp(request));
        Map<String, Object> data = new HashMap<>();
        data.put("id", user.getId());
        data.put("username", user.getUsername());
        data.put("name", user.getName());
        data.put("role", user.getRole());
        return Result.success(data);
    }

    @PutMapping("/profile")
    public Result<Void> updateProfile(@RequestParam String username, @RequestParam String password,
                                      @RequestParam String name, HttpSession session) {
        SysUser loginUser = (SysUser) session.getAttribute("loginUser");
        if (loginUser == null) {
            return Result.error("未登录");
        }
        log.info("更新个人信息: id={}, username={}, clientIp={}", loginUser.getId(), username, IpUtils.getClientIp(request));
        SysUser exist = sysUserService.doGetByUsername(username);
        if (exist != null && !exist.getId().equals(loginUser.getId())) {
            return Result.error("用户名已被占用");
        }
        loginUser.setUsername(username);
        loginUser.setPassword(PasswordUtils.encode(password));
        loginUser.setName(name);
        sysUserService.doUpdateProfile(loginUser);
        session.setAttribute("loginUser", loginUser);
        return Result.success();
    }
}
