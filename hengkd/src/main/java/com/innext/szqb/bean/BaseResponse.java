package com.innext.szqb.bean;

import java.io.Serializable;

/**
 * 封装服务器返回数据
 */
public class BaseResponse<T> implements Serializable {
    public String code;
    public String message;
    public String time;
    public T data;

    public boolean success() {
        return "0".equals(code);
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
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

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    @Override
    public String toString() {
        return "BaseResponse{" +
                "code='" + code + '\'' +
                ", message='" + message + '\'' +
                ", time='" + time + '\'' +
                ", data=" + data +
                '}';
    }
}
