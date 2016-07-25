package com.tryking.EasyList.bean;

/**
 * Created by 26011 on 2016/7/25.
 */
public class LoginReturnBean extends BaseBean {
    private boolean isNewAccount;
    private UserInfo userInfo;
    private DayEvent dayEvent;

    public boolean isNewAccount() {
        return isNewAccount;
    }

    public void setNewAccount(boolean newAccount) {
        isNewAccount = newAccount;
    }

    public UserInfo getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(UserInfo userInfo) {
        this.userInfo = userInfo;
    }

    public DayEvent getDayEvent() {
        return dayEvent;
    }

    public void setDayEvent(DayEvent dayEvent) {
        this.dayEvent = dayEvent;
    }
}
