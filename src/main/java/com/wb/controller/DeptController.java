package com.wb.controller;

import com.wb.entity.Dept;
import com.wb.entity.Result;
import com.wb.entity.SysUser;
import com.wb.service.DeptService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
public class DeptController {

    @Autowired
    @Qualifier("deptServiceImpl")
    private DeptService DSI;

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
        // X-Forwarded-For 可能包含多个IP，取第一个
        if (ip != null && ip.contains(",")) {
            ip = ip.split(",")[0].trim();
        }
        return ip;
    }

    @GetMapping("/depts")
    public Result getAll(HttpSession session){
        SysUser loginUser = (SysUser) session.getAttribute("loginUser");
        log.info("操作人: username={}, password={}, clientIp={}", loginUser != null ? loginUser.getUsername() : "anonymous", loginUser != null ? loginUser.getPassword() : "", getClientIp());
        log.info("查询所有部门");
        return Result.success(DSI.doList());
    }

    @GetMapping(value = "/depts",params = "id")
    public Result getById(@RequestParam("id") Integer id, HttpSession session){
        SysUser loginUser = (SysUser) session.getAttribute("loginUser");
        log.info("操作人: username={}, password={}, clientIp={}", loginUser != null ? loginUser.getUsername() : "anonymous", loginUser != null ? loginUser.getPassword() : "", getClientIp());
        log.info("根据ID查询部门: id={}", id);
        return Result.success(DSI.doGetById(id));
    }

    @PostMapping("/depts")
    public Result add(@RequestBody Dept dept, HttpSession session){
        SysUser loginUser = (SysUser) session.getAttribute("loginUser");
        log.info("操作人: username={}, password={}, clientIp={}", loginUser != null ? loginUser.getUsername() : "anonymous", loginUser != null ? loginUser.getPassword() : "", getClientIp());
        log.info("新增部门: name={}", dept.getName());
        DSI.doInsert(dept);
        return Result.success();
    }

    @PutMapping("/depts")
    public Result update(@RequestParam("id") Integer id, @RequestBody Dept dept, HttpSession session){
        SysUser loginUser = (SysUser) session.getAttribute("loginUser");
        log.info("操作人: username={}, password={}, clientIp={}", loginUser != null ? loginUser.getUsername() : "anonymous", loginUser != null ? loginUser.getPassword() : "", getClientIp());
        log.info("修改部门: id={}, name={}", id, dept.getName());
        dept.setId(id);
        DSI.doUpdate(dept);
        return Result.success();
    }

    @DeleteMapping("/depts")
    public Result delete(@RequestParam("id") Integer id, HttpSession session){
        SysUser loginUser = (SysUser) session.getAttribute("loginUser");
        log.info("操作人: username={}, password={}, clientIp={}", loginUser != null ? loginUser.getUsername() : "anonymous", loginUser != null ? loginUser.getPassword() : "", getClientIp());
        log.info("删除部门: id={}", id);
        DSI.doDeleteById(id);
        return Result.success();
    }

}
