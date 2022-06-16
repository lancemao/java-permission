# ABAC (Attribute Based Access Control) 基于属性的权限控制

阅读本教程前，请确保已经完成了 [开发准备](./../README.md)，并添加了 [凭证校验](./auth.md) 逻辑

## 基本用法

在需要属性控制的端点函数前加上 RequiresResource 注解即可：

```java
import cn.authing.permission.permission.RequiresResource;

@RequiresResource("order:read")
@GetMapping("/your-API")
public String yourAPI(HttpServletRequest request, HttpServletResponse response) {
}
```

例子中，*order* 是资源名，*read* 是 action（也可以叫做 operation），用冒号 : 连接。资源名和 action 都可以在控制台自定义。如果用户被授予 order 资源的 read 权限，或者被授予 order 资源的**所有**权限，即 order:* ，权限拦截器就会放行。否则，拦截器会返回错误。

## 省略 action

省略 action 等同于需要所有 action 权限。即

```java
@RequiresResource("order")
```

等同于

```java
@RequiresResource("order:*")
```

## 基于角色、用户组、组织机构的权限控制

Authing 会自动根据当前用户信息，判断用户是否在资源要求的角色、用户组和组织机构里面。

所以只需要管理员在控制台给用户配置正确的角色、用户组、组织机构即可，业务应用不需要写代码控制。
