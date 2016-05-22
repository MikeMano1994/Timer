package com.tryking.timer.db.dao;

import android.content.Context;

import com.j256.ormlite.dao.Dao;
import com.tryking.timer.db.BaseDao;
import com.tryking.timer.db.table.EventItemSource;

import java.sql.SQLException;

/**
 * Created by Tryking on 2016/5/18.
 */
public class EventItemSourceDao extends BaseDao<EventItemSource, Integer> {
    public EventItemSourceDao(Context mContext) {
        super(mContext);
    }

    @Override
    public Dao<EventItemSource, Integer> getDao() throws SQLException {
        return getHelper().getDao(EventItemSource.class);
    }
}
