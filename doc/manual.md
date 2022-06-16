# 手动实现

如果注解方式不能满足业务需求，可以先通过我们的 API 获取角色和资源信息，再手动处理权限控制逻辑。

## 获取角色

```java
public class Client {
    public List<Role> getRoles(UserInfo userInfo)
}
```

示例代码：

```java
@GetMapping("/your-API")
public String yourAPI(HttpServletRequest request, HttpServletResponse response) {
    UserInfo userInfo = (UserInfo) request.getAttribute("UserInfo");
    Client client = new Client();
    List<Role> userRoles = client.getRoles(userInfo);
}
```

<br>

## 判断角色

```java
public class PermissionUtil {
    public static boolean hasRole(UserInfo userInfo, String role)
}
```

示例代码：

```java
@GetMapping("/your-API")
public String yourAPI(HttpServletRequest request, HttpServletResponse response) {
    UserInfo userInfo = (UserInfo) request.getAttribute("UserInfo");
    String requiredRole = "admin";
    boolean hasRole = PermissionUtil.hasRole(userInfo, requiredRole);
}
```

<br>

## 获取用户授权资源

该函数返回的资源是当前用户被授权允许访问资源的合集。包括当前应用、当前用户池、default 权限组、system 权限组。

> **不包含**其他应用里面配置的授权信息。这里*应用*是指 authing 应用，在 Spring boot 启动时，通过 Authing.appId = {your authing app id} 设置

```java
public class Client {
    public List<Resource> listResources(UserInfo userInfo)
}
```

示例代码：


```java
@GetMapping("/your-API")
public String yourAPI(HttpServletRequest request, HttpServletResponse response) {
    UserInfo userInfo = (UserInfo) request.getAttribute("UserInfo");
    Client client = new Client();
    List<Resource> resources = client.listResources(userInfo);
}
```

<br>

## 判断是否有某资源访问权限

```java
public class PermissionUtil {
    public static boolean canAccessResource(UserInfo userInfo, String value)
}
```

示例代码：

```java
@GetMapping("/your-API")
public String yourAPI(HttpServletRequest request, HttpServletResponse response) {
    UserInfo userInfo = (UserInfo) request.getAttribute("UserInfo");
    String requiredResource = "order:read";
    boolean can = PermissionUtil.canAccessResource(userInfo, requiredResource);
}
```
