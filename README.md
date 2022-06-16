# Authing 鉴权示例

> 基于 Spring boot，Java 版本 >= 11

<br>

## 引入依赖

目前采用源码方式提供，请拷贝本仓库代码中的 cn.authing.permission 包到工程里面。

<br>

## 初始化

在标注了 @SpringBootApplication 的 Application 类里面调用：

```java
Authing.appId = "{authing app id}";
```

<br>

## 开始之前

由于权限控制需要用户信息，所以需要先添加认证相关处理逻辑。详细步骤参考这里：

[凭证校验 & 获取用户信息](doc/auth.md)

<br>

## 添加权限拦截器

添加以下配置类。可以根据需要配置需要进行权限控制的端点规则。

```java
import cn.authing.permission.permission.PermissionInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class PermissionConfig implements WebMvcConfigurer {
    @Override
    public void addInterceptors(InterceptorRegistry registry){
        registry.addInterceptor(new PermissionInterceptor()).addPathPatterns("/**");
    }
}
```

<br>

## 典型场景

* [通过注解实现 RBAC (Role Based Access Control) 基于角色的权限控制](./doc/rbac.md)
* [通过注解实现 ABAC (Attribute Based Access Control) 基于属性的权限控制](./doc/abac.md)
* [手动实现权限控制](./doc/manual.md)

<br>

## 测试

1. 在 https://developer-beta.authing.cn/ams/auth-tool/index.html 填入自己应用相关信息，点击 Login，拷贝 ID Token
2. 在 Postman 里面将 ID Token 作为 Authorization Bearer 的值传入
3. 在访问业务服务时，在 Postman 里面配置 http header，带上 x-authing-app-id 和 x-authing-userpool-id

<br>

## 私有化部署

在标注了 @SpringBootApplication 的 Application 类里面调用：

```java
// mycompany.com is your on premise host
Authing.host = "mycompany.com";
```