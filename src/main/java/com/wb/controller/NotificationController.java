package com.wb.controller;

import com.wb.entity.Notification;
import com.wb.entity.Result;
import com.wb.entity.SysUser;
import com.wb.service.NotificationService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
public class NotificationController {

    @Autowired
    private NotificationService notificationService;

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

    @GetMapping("/notifications")
    public Result getAll(HttpSession session) {
        SysUser user = (SysUser) session.getAttribute("loginUser");
        if (user == null) {
            log.warn("查询通知失败: 未登录");
            return Result.error("未登录");
        }
        log.info("操作人: username={}, password={}, clientIp={}", user.getUsername(), user.getPassword(), getClientIp());
        log.info("查询通知列表: userId={}", user.getId());
        return Result.success(notificationService.doGetAllByUser(user.getId()));
    }

    @PostMapping("/notifications")
    public Result create(@RequestParam String title, HttpSession session) {
        SysUser user = (SysUser) session.getAttribute("loginUser");
        if (user == null) {
            log.warn("创建通知失败: 未登录");
            return Result.error("未登录");
        }
        log.info("操作人: username={}, password={}, clientIp={}", user.getUsername(), user.getPassword(), getClientIp());
        log.info("创建通知: title={}, createdBy={}", title, user.getId());
        Notification n = new Notification();
        n.setTitle(title);
        n.setCreatedBy(user.getId());
        notificationService.doCreate(n);
        return Result.success();
    }

    @PutMapping("/notifications")
    public Result markRead(@RequestParam(required = false) Integer id,
                           HttpSession session) {
        SysUser user = (SysUser) session.getAttribute("loginUser");
        if (user == null) {
            log.warn("标记通知失败: 未登录");
            return Result.error("未登录");
        }
        log.info("操作人: username={}, password={}, clientIp={}", user.getUsername(), user.getPassword(), getClientIp());
        if (id != null) {
            log.info("标记通知已读: notificationId={}, userId={}", id, user.getId());
            notificationService.doMarkRead(id, user.getId());
        } else {
            log.info("标记全部通知已读: userId={}", user.getId());
            notificationService.doMarkAllRead(user.getId());
        }
        return Result.success();
    }
}
