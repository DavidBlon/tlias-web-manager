package com.wb.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.wb.entity.Dept;
import com.wb.entity.PageResult;
import com.wb.entity.Result;
import com.wb.entity.SysUser;
import com.wb.service.DeptService;
import com.wb.util.IpUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
public class DeptController {

    private final DeptService deptService;
    private final HttpServletRequest request;

    public DeptController(DeptService deptService, HttpServletRequest request) {
        this.deptService = deptService;
        this.request = request;
    }

    @GetMapping("/depts")
    public Result<PageResult<Dept>> getAll(@RequestParam(defaultValue = "1") int pageNum,
                                           @RequestParam(defaultValue = "10") int pageSize,
                                           HttpSession session) {
        SysUser loginUser = (SysUser) session.getAttribute("loginUser");
        log.info("操作人: username={}, clientIp={}", loginUser != null ? loginUser.getUsername() : "anonymous", IpUtils.getClientIp(request));
        PageHelper.startPage(pageNum, pageSize);
        PageInfo<Dept> pageInfo = new PageInfo<>(deptService.doList());
        return Result.success(new PageResult<>(pageInfo.getList(), pageInfo.getTotal(), pageNum, pageSize));
    }

    @GetMapping(value = "/depts", params = "id")
    public Result<Dept> getById(@RequestParam("id") Integer id, HttpSession session) {
        SysUser loginUser = (SysUser) session.getAttribute("loginUser");
        log.info("操作人: username={}, clientIp={}", loginUser != null ? loginUser.getUsername() : "anonymous", IpUtils.getClientIp(request));
        return Result.success(deptService.doGetById(id));
    }

    @PostMapping("/depts")
    public Result<Void> add(@Valid @RequestBody Dept dept, HttpSession session) {
        SysUser loginUser = (SysUser) session.getAttribute("loginUser");
        log.info("操作人: username={}, clientIp={}", loginUser != null ? loginUser.getUsername() : "anonymous", IpUtils.getClientIp(request));
        log.info("新增部门: name={}", dept.getName());
        deptService.doInsert(dept);
        return Result.success();
    }

    @PutMapping("/depts")
    public Result<Void> update(@RequestParam("id") Integer id, @Valid @RequestBody Dept dept, HttpSession session) {
        SysUser loginUser = (SysUser) session.getAttribute("loginUser");
        log.info("操作人: username={}, clientIp={}", loginUser != null ? loginUser.getUsername() : "anonymous", IpUtils.getClientIp(request));
        log.info("修改部门: id={}, name={}", id, dept.getName());
        dept.setId(id);
        deptService.doUpdate(dept);
        return Result.success();
    }

    @DeleteMapping("/depts")
    public Result<Void> delete(@RequestParam("id") Integer id, HttpSession session) {
        SysUser loginUser = (SysUser) session.getAttribute("loginUser");
        log.info("操作人: username={}, clientIp={}", loginUser != null ? loginUser.getUsername() : "anonymous", IpUtils.getClientIp(request));
        log.info("删除部门: id={}", id);
        deptService.doDeleteById(id);
        return Result.success();
    }
}
