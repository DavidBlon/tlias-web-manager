package com.wb.config;

import com.wb.entity.SysUser;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class LoginInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
                             Object handler) throws Exception {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("loginUser") == null) {
            writeError(response, "未登录");
            return false;
        }

        if ("GET".equalsIgnoreCase(request.getMethod())) {
            return true;
        }

        SysUser user = (SysUser) session.getAttribute("loginUser");
        String path = request.getRequestURI();

        if (path.equals("/employees/status")) {
            return true;
        }

        if (path.startsWith("/notifications")) {
            if ("POST".equalsIgnoreCase(request.getMethod())
                    && !"ADMIN".equals(user.getRole())) {
                writeError(response, "无操作权限，仅管理员可发布通知");
                return false;
            }
            return true;
        }

        if (!"ADMIN".equals(user.getRole())) {
            writeError(response, "无操作权限，仅管理员可执行此操作");
            return false;
        }

        return true;
    }

    private void writeError(HttpServletResponse response, String msg) throws Exception {
        response.setContentType("application/json;charset=utf-8");
        response.getWriter().write("{\"code\":0,\"msg\":\"" + msg + "\"}");
    }
}
