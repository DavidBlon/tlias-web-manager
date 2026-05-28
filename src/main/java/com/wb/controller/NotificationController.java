package com.wb.controller;

import com.wb.entity.Notification;
import com.wb.entity.Result;
import com.wb.entity.SysUser;
import com.wb.service.NotificationService;
import com.wb.util.IpUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
public class NotificationController {

    private final NotificationService notificationService;
    private final HttpServletRequest request;

    public NotificationController(NotificationService notificationService, HttpServletRequest request) {
        this.notificationService = notificationService;
        this.request = request;
    }

    @GetMapping("/notifications")
    public Result<List<Notification>> getAll(HttpSession session) {
        SysUser user = (SysUser) session.getAttribute("loginUser");
        if (user == null) {
            return Result.error("未登录");
        }
        log.info("操作人: username={}, clientIp={}", user.getUsername(), IpUtils.getClientIp(request));
        return Result.success(notificationService.doGetAllByUser(user.getId()));
    }

    @PostMapping("/notifications")
    public Result<Void> create(@RequestParam String title, HttpSession session) {
        SysUser user = (SysUser) session.getAttribute("loginUser");
        if (user == null) {
            return Result.error("未登录");
        }
        log.info("操作人: username={}, clientIp={}", user.getUsername(), IpUtils.getClientIp(request));
        log.info("创建通知: title={}, createdBy={}", title, user.getId());
        Notification n = new Notification();
        n.setTitle(title);
        n.setCreatedBy(user.getId());
        notificationService.doCreate(n);
        return Result.success();
    }

    @PutMapping("/notifications")
    public Result<Void> markRead(@RequestParam(required = false) Integer id,
                                 HttpSession session) {
        SysUser user = (SysUser) session.getAttribute("loginUser");
        if (user == null) {
            return Result.error("未登录");
        }
        log.info("操作人: username={}, clientIp={}", user.getUsername(), IpUtils.getClientIp(request));
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
