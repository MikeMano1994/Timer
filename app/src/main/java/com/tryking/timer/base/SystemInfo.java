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

    public String getQQ() {
        return (String) SPUtils.get(mContext, ApplicationGlobal.SHARED_PREFERENCE_QQ, "");
    }

    public void setQQ(String qq) {
        SPUtils.put(mContext, ApplicationGlobal.SHARED_PREFERENCE_QQ, qq);
    }

    public String getSina() {
        return (String) SPUtils.get(mContext, ApplicationGlobal.SHARED_PREFERENCE_SINA, "");
    }

    public void setSina(String sina) {
        SPUtils.put(mContext, ApplicationGlobal.SHARED_PREFERENCE_SINA, sina);
    }

    public String getPortraitUrl() {
        return (String) SPUtils.get(mContext, ApplicationGlobal.SHARED_PREFERENCE_PORTRAIT_URL, "");
    }

    public void setPortraitUrl(String url) {
        SPUtils.put(mContext, ApplicationGlobal.SHARED_PREFERENCE_PORTRAIT_URL, url);
    }

    // TODO: 2016/6/27 先暂时用account来判断，后期换
    public boolean isLogin() {
        String account = (String) SPUtils.get(mContext, ApplicationGlobal.SHARED_PREFERENCE_ACCOUNT, "");
        if (account == null || account.equals("")) {
            return false;
        }
        return true;
    }

    /**
     * 退出账户，所有的数据都设为空
     */
    public void logout() {
        setAccount("");
        setPortraitUrl("");
        setMemberId("");
        setPassword("");
        setPhone("");
        setToken("");
        setQQ("");
    }
}
