package com.tryking.timer.base;

import android.content.Context;

import com.tryking.timer.global.ApplicationGlobal;
import com.tryking.timer.utils.SPUtils;

/**
 * Created by Tryking on 2016/6/26.
 */
public class SystemInfo {
    private static SystemInfo instance;
    private Context mContext;

    private SystemInfo(Context context) {
        mContext = context;
    }

    public static SystemInfo getInstance(Context context) {
        if (instance == null) {
            synchronized (SystemInfo.class) {
                if (instance == null) {
                    instance = new SystemInfo(context);
                }
            }
        }
        return instance;
    }

    public String getToken() {
        return (String) SPUtils.get(mContext, ApplicationGlobal.SHARED_PREFERENCE_TOKEN, "");
    }

    public void setToken(String token) {
        SPUtils.put(mContext, ApplicationGlobal.SHARED_PREFERENCE_TOKEN, token);
    }

    public String getMemberId() {
        return (String) SPUtils.get(mContext, ApplicationGlobal.SHARED_PREFERENCE_MEMBER_ID, "");
    }

    public void setMemberId(String memberId) {
        SPUtils.put(mContext, ApplicationGlobal.SHARED_PREFERENCE_MEMBER_ID, memberId);
    }

    public String getAccount() {
        return (String) SPUtils.get(mContext, ApplicationGlobal.SHARED_PREFERENCE_ACCOUNT, "");
    }

    public void setAccount(String account) {
        SPUtils.put(mContext, ApplicationGlobal.SHARED_PREFERENCE_ACCOUNT, account);
    }

    public String getPassword() {
        return (String) SPUtils.get(mContext, ApplicationGlobal.SHARED_PREFERENCE_PASSWORD, "");
    }

    public void setPassword(String password) {
        SPUtils.put(mContext, ApplicationGlobal.SHARED_PREFERENCE_PASSWORD, password);
    }

    public String getPhone() {
        return (String) SPUtils.get(mContext, ApplicationGlobal.SHARED_PREFERENCE_PHONE, "");
    }

    public void setPhone(String phone) {
        SPUtils.put(mContext, ApplicationGlobal.SHARED_PREFERENCE_PHONE, phone);
    }
}
