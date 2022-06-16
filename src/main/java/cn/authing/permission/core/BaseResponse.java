package cn.authing.permission.core;

public class BaseResponse<T> {
    private int code;
    private String message;
    private T data;

    public BaseResponse() {
        code = 200;
        message = "ok";
    }

    public BaseResponse(int code, String msg) {
        this.code = code;
        message = msg;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
