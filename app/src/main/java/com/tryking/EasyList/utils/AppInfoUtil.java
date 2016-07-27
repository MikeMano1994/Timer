package com.tryking.EasyList.utils;

import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;

import com.tryking.EasyList.base.EasyListApplication;

/**
 * Created by 26011 on 2016/7/26.
 */
public class AppInfoUtil {
    /**
     * 是否调试模式
     *
     * @return 是-yes 否-no
     */
    public static String isDebug() {

        String msg = "no";
        try {
            ApplicationInfo appInfo = EasyListApplication.getInstance().getPackageManager()
                    .getApplicationInfo(EasyListApplication.getInstance().getPackageName(),
                            PackageManager.GET_META_DATA);
            msg = appInfo.metaData.getString("IS_DEBUG");

        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return msg;
    }
}
