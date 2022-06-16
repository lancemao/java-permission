package cn.authing.permission.permission;

import cn.authing.permission.core.Client;
import cn.authing.permission.core.Role;
import cn.authing.permission.core.UserInfo;
import org.springframework.util.StringUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.List;

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
        if (userInfo != null) {
            String[] roleCodes = value.split(" ");

            Client client = new Client();
            List<Role> userRoles = client.getRoles(userInfo);
            if (userRoles == null || userRoles.size() == 0) {
                response.setStatus(403);
                response.getWriter().print("user has no role");
                return false;
            }

            boolean hasRole = false;
            outer:
            for (String code : roleCodes) {
                for (Role role : userRoles) {
                    if (code.trim().equals(role.getCode())) {
                        hasRole = true;
                        break outer;
                    }
                }
            }

            if (hasRole) {
                return true;
            } else {
                response.setStatus(403);
                response.getWriter().print("no role matched");
            }
        } else {
            response.setStatus(401);
            response.getWriter().print("Unauthorized. Please login first");
        }

        return false;
    }

    private boolean canAccessResource(String value, HttpServletRequest request, HttpServletResponse response) throws Exception {
        UserInfo userInfo = (UserInfo) request.getAttribute("UserInfo");
        if (userInfo == null) {
            response.setStatus(401);
            response.getWriter().print("Unauthorized. Please login first");
            return false;
        }

        Client client = new Client();
        List<Resource> resources = client.listResources(userInfo);
        if (resources == null || resources.size() == 0) {
            response.setStatus(403);
            response.getWriter().print("user has no authorized resource");
            return false;
        }

        String res;
        String action = "*";
        String[] split = value.split(":");
        res = split[0];
        if (split.length > 1) {
            action = split[1];
        }

        Resource resource = findResource(resources, res);
        if (resource != null) {
            for (String a : resource.getActions()) {
                String atomicAction = getAction(a);
                if (StringUtils.hasLength(atomicAction)) {
                    if (atomicAction.equals("*") || atomicAction.equals(action)) {
                        return true;
                    }
                }
            }
        }

        response.setStatus(403);
        response.getWriter().print("no permission to access this resource");
        return false;
    }

    private Resource findResource(List<Resource> resources, String res) {
        for (Resource r : resources) {
            String code = r.getCode();
            if (!StringUtils.hasLength(code)) {
                continue;
            }
            String[] split = code.split(":");
            if (split.length == 0) {
                continue;
            }
            String s = split[0];
            if (s.equals(res)) {
                return r;
            }
        }
        return null;
    }

    private String getAction(String raw) {
        if ("*".equals(raw)) {
            return raw;
        }

        String[] split = raw.split(":");
        if (split.length > 1) {
            return split[1];
        }
        return null;
    }
}
