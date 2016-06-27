package com.tryking.timer.utils;

import android.app.Activity;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by Tryking on 2016/6/26.
 */
public class ActivityUtils {
    public LinkedList<Activity> activitiesList = new LinkedList<>();
    private static ActivityUtils instance;

    public static ActivityUtils getInstance() {
        if (instance == null) {
            synchronized (ActivityUtils.class) {
                if (instance == null) {
                    instance = new ActivityUtils();
                }
            }
        }
        return instance;
    }

    public void addActivity(Activity activity) {
        synchronized (activitiesList) {
            activitiesList.add(activity);
        }
    }

    public void clearActivity(Activity activity) {
        synchronized (activitiesList) {
            activitiesList.remove(activity);
        }
    }

    public void killAllActivities() {
        //复制一份
        List<Activity> copy;
        synchronized (activitiesList) {
            copy = new LinkedList<>(activitiesList);
        }
        for (Activity activity : copy
                ) {
            activity.finish();
        }
        System.exit(0);
        //杀死当前的进程
        android.os.Process.killProcess(android.os.Process.myPid());
    }
}
