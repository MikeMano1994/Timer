package com.tryking.EasyList.db.dao;

import android.content.Context;

import com.j256.ormlite.dao.Dao;
import com.tryking.EasyList.db.BaseDao;
import com.tryking.EasyList.db.table.EverydayEventSource;

import java.sql.SQLException;

/**
 * Created by Tryking on 2016/5/18.
 */
public class EverydayEventSourceDao extends BaseDao<EverydayEventSource, Integer> {
    public EverydayEventSourceDao(Context mContext) {
        super(mContext);
    }

    @Override
    public Dao<EverydayEventSource, Integer> getDao() throws SQLException {
        return getHelper().getDao(EverydayEventSource.class);
    }
}
