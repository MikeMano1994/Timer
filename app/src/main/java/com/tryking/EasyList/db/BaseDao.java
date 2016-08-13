//package com.tryking.EasyList.db;
//
//import android.content.Context;
//
//import com.j256.ormlite.dao.Dao;
//import com.j256.ormlite.stmt.PreparedDelete;
//import com.j256.ormlite.stmt.PreparedQuery;
//import com.j256.ormlite.stmt.QueryBuilder;
//import com.j256.ormlite.stmt.Where;
//
//import java.sql.SQLException;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Map;
//import java.util.Set;
//
///**
// * Created by Tryking on 2016/5/17.
// */
//public abstract class BaseDao<T, Integer> {
//    private DatabaseHelper mDatabaseHelper;
//    private Context mContext;
//
//    public BaseDao(Context mContext) {
//        this.mContext = mContext;
//        getHelper();
//    }
//
//    protected DatabaseHelper getHelper() {
//        if (mDatabaseHelper == null) {
//            mDatabaseHelper = DatabaseHelper.getHelper(mContext);
////            mDatabaseHelper = OpenHelperManager.getHelper(mContext, DatabaseHelper.class);
//        }
//        return mDatabaseHelper;
//    }
//
//    public abstract Dao<T, Integer> getDao() throws SQLException;
//
//    public int save(T t) throws java.sql.SQLException {
//        return getDao().create(t);
//    }
//
//    public List<T> query(PreparedQuery<T> preparedQuery) throws SQLException {
//        Dao<T, Integer> dao = getDao();
//        return dao.query(preparedQuery);
//    }
//
//    public List<T> query(String attributeName, String attributeValue) throws SQLException {
//        QueryBuilder<T, Integer> builder = getDao().queryBuilder();
//        builder.where().eq(attributeName, attributeValue);
//        PreparedQuery<T> preparedQuery = builder.prepare();
//        return query(preparedQuery);
//    }
//
//    public List<T> query(String[] attributeNames, String[] attributeValues) throws SQLException, InvalidParamsException {
//        if (attributeNames.length != attributeValues.length) {
//            throw new InvalidParamsException("params size is not equal");
//        }
//        QueryBuilder<T, Integer> builder = getDao().queryBuilder();
//        Where<T, Integer> wheres = builder.where();
//        for (int i = 0; i < attributeNames.length; i++) {
//            wheres.eq(attributeNames[i], attributeValues[i]);
//        }
//        PreparedQuery<T> preparedQuery = builder.prepare();
//        return query(preparedQuery);
//    }
//
//    public List<T> queryAll() throws SQLException {
//        Dao<T, Integer> dao = getDao();
//        return dao.queryForAll();
//    }
//
//    public T queryById(String idName, String idValue) throws SQLException {
//        List<T> tList = query(idName, idValue);
//        if (tList != null && !tList.isEmpty()) {
//            return tList.get(0);
//        } else {
//            return null;
//        }
//    }
//
//    public int delete(PreparedDelete<T> preparedDelete) throws SQLException {
//        Dao<T, Integer> dao = getDao();
//        return dao.delete(preparedDelete);
//    }
//
//    public int delete(T t) throws SQLException {
//        Dao<T, Integer> dao = getDao();
//        return dao.delete(t);
//    }
//
//    public int delete(List<T> tList) throws SQLException {
//        Dao<T, Integer> dao = getDao();
//        return dao.delete(tList);
//    }
//
//    public int delete(String[] attributeNames, String[] attributeValues) throws InvalidParamsException, SQLException {
//        List<T> tList = query(attributeNames, attributeValues);
//        if (tList != null && !tList.isEmpty()) {
//            return delete(tList);
//        }
//        return 0;
//    }
//
//    public int deleteById(String idName, String idValue) throws SQLException {
//        T t = queryById(idName, idValue);
//        if (t != null) {
//            return delete(t);
//        }
//        return 0;
//    }
//
//    public int update(T t) throws SQLException {
//        Dao<T, Integer> dao = getDao();
//        return dao.update(t);
//    }
//
//    public boolean isTableExists() throws SQLException {
//        return getDao().isTableExists();
//    }
//
//    public long countOf() throws SQLException {
//        return getDao().countOf();
//    }
//
//    public List<T> query(Map<String, Object> map, String order) throws SQLException {
//        QueryBuilder<T, Integer> queryBuilder = getDao().queryBuilder();
//        queryBuilder.orderBy(order, true);
//        if (!map.isEmpty()) {
//            Where<T, Integer> wheres = queryBuilder.where();
//            Set<String> keys = map.keySet();
//            List<String> keyss = new ArrayList<>();
//            keyss.addAll(keys);
//            for (int i = 0; i < keyss.size(); i++) {
//                if (i == 0) {
//                    wheres.eq(keyss.get(i), map.get(keyss.get(i)));
//                } else {
//                    wheres.and().eq(keyss.get(i), map.get(keyss.get(i)));
//                }
//            }
//        }
//        PreparedQuery<T> preparedQuery = queryBuilder.prepare();
//        return query(preparedQuery);
//    }
//
//    public List<T> query(Map<String, Object> map) throws SQLException {
//        QueryBuilder<T, Integer> queryBuilder = getDao().queryBuilder();
//        if (!map.isEmpty()) {
//            Where<T, Integer> wheres = queryBuilder.where();
//            Set<String> keys = map.keySet();
//            List<String> keyss = new ArrayList<>();
//            keyss.addAll(keys);
//            for (int i = 0; i < keyss.size(); i++) {
//                if (i == 0) {
//                    wheres.eq(keyss.get(i), map.get(keyss.get(i)));
//                } else {
//                    wheres.and().eq(keyss.get(i), map.get(keyss.get(i)));
//                }
//            }
//        }
//        PreparedQuery<T> preparedQuery = queryBuilder.prepare();
//        return query(preparedQuery);
//    }
//
//    public List<T> query(Map<String, Object> map, Map<String, Object> lowMap, Map<String, Object> highMap) throws SQLException {
//        QueryBuilder<T, Integer> queryBuilder = getDao().queryBuilder();
//        Where<T, Integer> wheres = queryBuilder.where();
//        if (!map.isEmpty()) {
//            Set<String> keys = map.keySet();
//            List<String> keyss = new ArrayList<>();
//            keyss.addAll(keys);
//            for (int i = 0; i < keyss.size(); i++) {
//                if (i == 0) {
//                    wheres.eq(keyss.get(i), map.get(keyss.get(i)));
//                } else {
//                    wheres.and().eq(keyss.get(i), map.get(keyss.get(i)));
//                }
//            }
//        }
//        if (!lowMap.isEmpty()) {
//            Set<String> keys = lowMap.keySet();
//            List<String> keyss = new ArrayList<>();
//            keyss.addAll(keys);
//            for (int i = 0; i < keyss.size(); i++) {
//                if (map.isEmpty()) {
//                    wheres.gt(keyss.get(i), lowMap.get(keyss.get(i)));
//                } else {
//                    wheres.and().gt(keyss.get(i), lowMap.get(keyss.get(i)));
//                }
//            }
//        }
//        if (!highMap.isEmpty()) {
//            Set<String> keys = highMap.keySet();
//            List<String> keyss = new ArrayList<>();
//            keyss.addAll(keys);
//            for (int i = 0; i < keyss.size(); i++) {
//                wheres.lt(keyss.get(i), highMap.get(keyss.get(i)));
//            }
//        }
//        PreparedQuery<T> preparedQuery = queryBuilder.prepare();
//        return query(preparedQuery);
//    }
//
//    public void close() {
//        getHelper().close();
//    }
//}
