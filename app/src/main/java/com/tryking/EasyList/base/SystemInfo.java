package com.tryking.EasyList.base;

import android.content.Context;

import com.orhanobut.logger.Logger;
import com.tryking.EasyList.global.ApplicationGlobal;
import com.tryking.EasyList.global.Constants;
import com.tryking.EasyList.utils.CommonUtils;
import com.tryking.EasyList.utils.SPUtils;

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

    public String getQQName() {
        return (String) SPUtils.get(mContext, ApplicationGlobal.SHARED_PREFERENCE_QQ_NAME, "");
    }

    public void setQQName(String qqName) {
        SPUtils.put(mContext, ApplicationGlobal.SHARED_PREFERENCE_QQ_NAME, qqName);
    }

    public String getSina() {
        return (String) SPUtils.get(mContext, ApplicationGlobal.SHARED_PREFERENCE_SINA, "");
    }

    public void setSina(String sina) {
        SPUtils.put(mContext, ApplicationGlobal.SHARED_PREFERENCE_SINA, sina);
    }

    public String getSinaName() {
        return (String) SPUtils.get(mContext, ApplicationGlobal.SHARED_PREFERENCE_SINA_NAME, "");
    }

    public void setSinaName(String sinaName) {
        SPUtils.put(mContext, ApplicationGlobal.SHARED_PREFERENCE_SINA_NAME, sinaName);
    }

    public String getPortraitUrl() {
        return (String) SPUtils.get(mContext, ApplicationGlobal.SHARED_PREFERENCE_PORTRAIT_URL, "");
    }

    public void setPortraitUrl(String url) {
        SPUtils.put(mContext, ApplicationGlobal.SHARED_PREFERENCE_PORTRAIT_URL, url);
    }

    public String getSignature() {
        return (String) SPUtils.get(mContext, ApplicationGlobal.SHARED_PREFERENCE_SIGNATURE, "");
    }

    public void setSignature(String signature) {
        SPUtils.put(mContext, ApplicationGlobal.SHARED_PREFERENCE_SIGNATURE, signature);
    }

    // TODO: 2016/6/27 先暂时用account来判断，后期换
    public boolean isLogin() {
        String memberId = (String) SPUtils.get(mContext, ApplicationGlobal.SHARED_PREFERENCE_MEMBER_ID, "");
        if (memberId == null || memberId.equals("") || memberId.equals(Constants.TRY_OUT_ACCOUNT)) {
            return false;
        }
        return true;
    }

    /**
     * 退出账户，所有的数据都设为空
     */
    public void logout() {
        setMemberId("");
        setAccount("");
        setPortraitUrl("");
        setPassword("");
        setPhone("");
        setToken("");
        setQQ("");
        setQQName("");
        setSina("");
        setSinaName("");
        setSignature("");
        //账户登出，要把本地的信息置为空。用户登录的时候从服务端拿到数据写入本地
        String startTimes = (String) SPUtils.get(mContext, "startTimes", "");
        String[] starts = CommonUtils.convertStrToArray(startTimes);
        
        //把存储的事件的key删除
        for (int i = 0; i < starts.length; i++) {
            SPUtils.remove(mContext, starts[i]);
        }

        SPUtils.put(mContext, "startTimes", "");
        SPUtils.put(mContext, "endTimes", "");
        SPUtils.put(mContext, "eventTypes", "");
    }
}
