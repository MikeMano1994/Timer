package com.tryking.EasyList.bean.thirdInfo;

/**
 * Created by Tryking on 2016/6/26.
 */
public class QQUserInfo {
    private String profile_image_url;
    private String screen_name;
    private String msg;
    private String city;
    private String gender;
    private String province;
    private String level;

    public String getProfile_image_url() {
        return profile_image_url;
    }

    public void setProfile_image_url(String profile_image_url) {
        this.profile_image_url = profile_image_url;
    }

    public String getScreen_name() {
        return screen_name;
    }

    public void setScreen_name(String screen_name) {
        this.screen_name = screen_name;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    @Override
    public String toString() {
        return "QQUserInfo{" +
                "profile_image_url='" + profile_image_url + '\'' +
                ", screen_name='" + screen_name + '\'' +
                ", msg='" + msg + '\'' +
                ", city='" + city + '\'' +
                ", gender='" + gender + '\'' +
                ", province='" + province + '\'' +
                ", level='" + level + '\'' +
                '}';
    }
}
