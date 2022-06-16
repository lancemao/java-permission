package cn.authing.permission.permission;

import cn.authing.permission.core.UserInfo;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

public class PermissionInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HandlerMethod hm;
        try {
            hm = (HandlerMethod) handler;
        } catch (ClassCastException e) {
            return HandlerInterceptor.super.preHandle(request, response, handler);
        }

        Method method = hm.getMethod();
        if (method.isAnnotationPresent(RequiresRole.class)) {
            String value = method.getAnnotation(RequiresRole.class).value();
            boolean hasRole = hasRole(value, request, response);
            if (!hasRole) {
                return false;
            }
        }

        if (method.isAnnotationPresent(RequiresResource.class)) {
            String value = method.getAnnotation(RequiresResource.class).value();
            boolean hasAccess = canAccessResource(value, request, response);
            if (!hasAccess) {
                return false;
            }
        }

        return HandlerInterceptor.super.preHandle(request, response, handler);
    }

    private boolean hasRole(String value, HttpServletRequest request, HttpServletResponse response) throws Exception {
        UserInfo userInfo = (UserInfo) request.getAttribute("UserInfo");
        if (userInfo == null) {
            response.setStatus(401);
            response.getWriter().print("Unauthorized. Please login first");
            return false;
        }

        boolean hasRole = PermissionUtil.hasRole(userInfo, value);
        if (hasRole) {
            return true;
        } else {
            response.setStatus(403);
            response.getWriter().print("no role matched");
            return false;
        }
    }

    private boolean canAccessResource(String value, HttpServletRequest request, HttpServletResponse response) throws Exception {
        UserInfo userInfo = (UserInfo) request.getAttribute("UserInfo");
        if (userInfo == null) {
            response.setStatus(401);
            response.getWriter().print("Unauthorized. Please login first");
            return false;
        }

        boolean can = PermissionUtil.canAccessResource(userInfo, value);
        if (can) {
            return true;
        } else {
            response.setStatus(403);
            response.getWriter().print("user has no authorized resource");
            return false;
        }
    }
}
