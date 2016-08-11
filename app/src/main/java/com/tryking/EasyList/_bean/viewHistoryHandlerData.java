package com.tryking.EasyList._bean;

import com.tryking.EasyList._bean.viewHistoryBean.ViewHistoryChildData;
import com.tryking.EasyList._bean.viewHistoryBean.ViewHistoryGroupData;

import java.util.List;

/**
 * Created by 26011 on 2016/8/9.
 */
public class ViewHistoryHandlerData {
    private ViewHistoryGroupData groupData;
    private List<ViewHistoryChildData> childData;

    public ViewHistoryGroupData getmGroupData() {
        return groupData;
    }

    public void setGroupData(ViewHistoryGroupData mGroupData) {
        this.groupData = mGroupData;
    }

    public List<ViewHistoryChildData> getmChildData() {
        return childData;
    }

    public void setChildData(List<ViewHistoryChildData> mChildData) {
        this.childData = mChildData;
    }

    @Override
    public String toString() {
        return "ViewHistoryHandlerData{" +
                "groupData=" + groupData +
                ", childData=" + childData +
                '}';
    }
}
