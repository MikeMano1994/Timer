package com.tryking.EasyList.bean;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/7/4.
 */
public class BaseBean implements Serializable {
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
