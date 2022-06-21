package cn.authing.permission.core;

import cn.authing.permission.permission.Resource;
import cn.authing.permission.permission.ResourceResponse;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Client {

    private String appId;
    private String appSecret;

    private Config config;

    public Client() {
        appId = Authing.getAppId();
        appSecret = Authing.getAppSecret();
        config = getApplicationConfig(appId);
    }

    public Client(String appId) {
        this.appId = appId;
        config = getApplicationConfig(appId);
    }

    public Client(String appId, String appSecret) {
        this.appId = appId;
        this.appSecret = appSecret;
        config = getApplicationConfig(appId);
    }

    public Config getApplicationConfig(String appId) {
        this.appId = appId;

        String url = "https://core." + Authing.getHost() + "/api/v2/applications/" + appId + "/public-config";
        HttpClient client = HttpClient.newHttpClient();

        HttpRequest req = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .GET()
                .build();
        try {
            HttpResponse<String> res = client.send(req, HttpResponse.BodyHandlers.ofString());
            BaseResponse<Config> result = JSON.parseObject(res.body(), new TypeReference<>(){});
            return result.getData();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Role> getRoles(UserInfo userInfo) {
        String idToken = userInfo.getToken();
        String userPoolId = config.getUserPoolId();
        String authorization = "Bearer " + idToken;
        String url = "https://" + config.getIdentifier() + "." + Authing.getHost() + "/api/v2/users/me/roles";
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest req = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .GET()
                .header("Authorization", authorization)
                .header("x-authing-app-id", appId)
                .header("x-authing-userpool-id", userPoolId)
                .build();
        try {
            HttpResponse<String> res = client.send(req, HttpResponse.BodyHandlers.ofString());
            BaseResponse<List<Role>> resp = JSON.parseObject(res.body(), new TypeReference<>() {
            });
            return resp.getData();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Resource> listResources(UserInfo userInfo) {
        List<Resource> list = new ArrayList<>();
        list.addAll(listResources(userInfo, "system"));
        list.addAll(listResources(userInfo, "default"));
        list.addAll(listResources(userInfo, config.getUserPoolId()));
        list.addAll(listResources(userInfo, appId));
        return list;
    }

    public List<Resource> listResources(UserInfo userInfo, String namespace) {
        Map<String, String> map = new HashMap<>();
        map.put("namespace", namespace);
        String body = JSON.toJSONString(map);
        String idToken = userInfo.getToken();
        String userPoolId = config.getUserPoolId();
        String authorization = "Bearer " + idToken;
        String url = "https://" + config.getIdentifier() + "." + Authing.getHost() + "/api/v2/users/resource/authorized";
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest req = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .POST(HttpRequest.BodyPublishers.ofString(body))
                .header("content-type", "application/json; charset=UTF-8")
                .header("Authorization", authorization)
                .header("x-authing-app-id", appId)
                .header("x-authing-userpool-id", userPoolId)
                .build();
        try {
            HttpResponse<String> res = client.send(req, HttpResponse.BodyHandlers.ofString());
            ResourceResponse resp = JSON.parseObject(res.body(), ResourceResponse.class);
            if (resp.getList() != null) {
                return resp.getList();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }
}
