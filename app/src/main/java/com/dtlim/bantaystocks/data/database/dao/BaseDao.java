package com.dtlim.bantaystocks.data.database.dao;

import android.content.ContentValues;
import android.database.Cursor;

import com.dtlim.bantaystocks.data.database.Database;
import com.squareup.sqlbrite.SqlBrite;

import java.util.List;

import rx.Observable;

/**
 * Created by dale on 6/29/16.
 */
public abstract class BaseDao<T> {
    protected Database mDatabase;

    protected abstract String getTableName();

    protected abstract ContentValues getContentValues(T object);

    protected abstract List<T> parseCursor(Cursor cursor);

    protected BaseDao(Database database) {
        mDatabase = database;
    }

    public Observable<SqlBrite.Query> query(String query) {
        return mDatabase.query(getTableName(), query);
    }

    public Observable<SqlBrite.Query> query(String query, String... args) {
        return mDatabase.query(getTableName(), query, args);
    }

    public long insert(T object) {
        return mDatabase.insert(getTableName(), getContentValues(object));
    }

    public int insert(List<T> objects) {
        ContentValues[] contentValues = new ContentValues[objects.size()];
        for (int i = 0; i < objects.size(); i++) {
            contentValues[i] = getContentValues(objects.get(i));
        }
        return mDatabase.insert(getTableName(), contentValues);
    }

    public int update(T object, String where, String[] whereArgs) {
        return mDatabase.update(getTableName(), getContentValues(object), where, whereArgs);
    }

    public int delete(String selection, String[] selectionArgs) {
        return mDatabase.delete(getTableName(), selection, selectionArgs);
    };

    public int deleteAll() {
        return mDatabase.delete(getTableName(), null, null);
    }
}
