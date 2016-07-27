package com.tryking.EasyList._bean;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/7/4.
 */
public class BaseBean implements Serializable {
    /**
     * 服务端返回的请求成功标识 0失败  1成功
     */
    private String result;
    private String msg;

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
