package com.tryking.timer.db.dao;

import android.content.Context;

import com.j256.ormlite.dao.Dao;
import com.tryking.timer.db.BaseDao;
import com.tryking.timer.db.table.SpecificEventSource;

import java.sql.SQLException;

/**
 * Created by Tryking on 2016/5/27.
 */
public class SpecificEventSourceDao extends BaseDao<SpecificEventSource, Integer> {
    public SpecificEventSourceDao(Context mContext) {
        super(mContext);
    }

    @Override
    public Dao<SpecificEventSource, Integer> getDao() throws SQLException {
        return getHelper().getDao(SpecificEventSource.class);
    }
}
