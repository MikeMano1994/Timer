package com.tryking.EasyList._bean.loginBean;

import com.tryking.EasyList._bean.BaseBean;

/**
 * Created by 26011 on 2016/7/25.
 */
public class LoginReturnBean extends BaseBean {
    private boolean isNewAccount;
    private User user;
    private DayEvent dayEvent;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public boolean isNewAccount() {
        return isNewAccount;
    }

    public void setNewAccount(boolean isNewAccount) {
        this.isNewAccount = isNewAccount;
    }

    public DayEvent getDayEvent() {
        return dayEvent;
    }

    public void setDayEvent(DayEvent dayEvent) {
        this.dayEvent = dayEvent;
    }

    @Override
    public String toString() {
        return "LoginReturnBean{" +
                "isNewAccount=" + isNewAccount +
                ", user=" + user +
                ", dayEvent=" + dayEvent +
                '}';
    }
}
