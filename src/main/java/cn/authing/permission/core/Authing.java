package cn.authing.permission.core;

public class Authing {
    private static String appId;
    private static String appSecret;
    private static String host = "authing.cn";

    public static String getAppId() {
        return appId;
    }

    public static void setAppId(String appId) {
        Authing.appId = appId;
    }

    public static String getAppSecret() {
        return appSecret;
    }

    public static void setAppSecret(String appSecret) {
        Authing.appSecret = appSecret;
    }

    public static String getHost() {
        return host;
    }

    public static void setHost(String host) {
        Authing.host = host;
    }
}
