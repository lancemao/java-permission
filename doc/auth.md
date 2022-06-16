# 凭证校验

>注意：本方法要求客户端在发送业务网络请求时，在 http header 里面携带以下信息

* authorization *用户认证后获取的凭证。用 Bearer 方式传至服务端*
* x-authing-app-id *Authing app id。可在 authing 控制台获取*
* x-authing-userpool-id *Authing user pool id。可在 authing 控制台获取*

## 添加过滤器

```java
import cn.authing.permission.auth.AuthFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AuthFilterConfig {
    @Bean
    public FilterRegistrationBean<AuthFilter> registerFilter() {
        FilterRegistrationBean<AuthFilter> registration = new FilterRegistrationBean<>();
        registration.setFilter(new AuthFilter());
        registration.addUrlPatterns("/*");
        registration.setName("AuthFilter");
        registration.setOrder(1);
        return registration;
    }
}
```

可以通过

```java
registration.addUrlPatterns("/*");
```

配置需要用户信息的端点规则

<br>

## 获取用户信息

如果凭证非法，过滤器会返回错误，Spring controller 不会被执行。

如果凭证合法，过滤器会在 request 里面添加一个 UserInfo 属性。然后在 controller 里面通过以下方式获取用户信息：

```java
@GetMapping("/your-API")
public String yourAPI(HttpServletRequest request, HttpServletResponse response) {
    UserInfo userInfo = (UserInfo) request.getAttribute("UserInfo");
}
```