# RBAC (Role Based Access Control) 基于角色的权限控制

阅读本教程前，请确保已经完成了 [开发准备](./../README.md)，并添加了 [凭证校验](./auth.md) 逻辑

## 基本用法

在需要角色控制的端点函数前加上 RequiresRole 注解即可：

```java
import cn.authing.permission.permission.RequiresRole;

@RequiresRole("admin")
@GetMapping("/your-API")
public String yourAPI(HttpServletRequest request, HttpServletResponse response) {
}
```

上面例子中，若用户没有 admin 角色，权限拦截器会返回错误信息，controller 里面的 yourAPI 函数不会被执行

## 多角色

如果端点允许多个角色访问，只需将注解的 value 改为：

```java
@RequiresRole("admin manager user") // roles are separated by white space
```

上面例子中，只要用户有 admin，manager，user 三种角色中的一种，权限拦截器就会放行。