package com.tryking.EasyList.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import com.tryking.EasyList.db.table.SpecificEventSource;
import com.tryking.EasyList.db.table.EverydayEventSource;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Tryking on 2016/5/14.
 */
public class DatabaseHelper extends OrmLiteSqliteOpenHelper {
    private static final String TAB_NAME = "timer.db";
    private static final int DB_VERSION = 1;
    private static DatabaseHelper instance;
    private Map<String, Dao> daos = new HashMap<>();

    private DatabaseHelper(Context context) {
        super(context, TAB_NAME, null, DB_VERSION);
    }

    public static synchronized DatabaseHelper getHelper(Context context) {
        if (instance == null) {
            synchronized (DatabaseHelper.class) {
                instance = new DatabaseHelper(context);
            }
        }
        return instance;
    }

    @Override
    public void onCreate(SQLiteDatabase database, ConnectionSource connectionSource) {
        try {
            TableUtils.createTable(connectionSource, EverydayEventSource.class);
            TableUtils.createTable(connectionSource, SpecificEventSource.class);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, ConnectionSource connectionSource, int oldVersion, int newVersion) {
        try {
            TableUtils.dropTable(connectionSource, EverydayEventSource.class, true);
            TableUtils.dropTable(connectionSource, SpecificEventSource.class, true);
            onCreate(database, connectionSource);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //    public Dao<User, Integer> getUserDao() throws SQLException {
//        if (userDao == null) {
//            userDao = getDao(User.class);
//        }
//        return userDao;
//    }
    public synchronized Dao getDao(Class clazz) throws SQLException {
        Dao dao = null;
        String className = clazz.getSimpleName();
        if (daos.containsKey(className)) {
            dao = daos.get(className);
        }
        if (dao == null) {
            dao = super.getDao(clazz);
            daos.put(className, dao);
        }
        return dao;
    }

    @Override
    public void close() {
        super.close();
        for (String key : daos.keySet()
                ) {
            Dao dao = daos.get(key);
            dao = null;
        }
    }
}

